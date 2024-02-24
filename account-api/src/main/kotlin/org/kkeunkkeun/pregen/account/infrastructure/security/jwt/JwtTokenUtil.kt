package org.kkeunkkeun.pregen.account.infrastructure.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.kkeunkkeun.pregen.account.domain.AccountRole
import org.kkeunkkeun.pregen.account.infrastructure.AccountJpaRepository
import org.kkeunkkeun.pregen.account.infrastructure.config.AccountProperties
import org.kkeunkkeun.pregen.account.infrastructure.security.exception.FilterException
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.dto.JwtTokenResponse
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.refreshtoken.RefreshTokenService
import org.kkeunkkeun.pregen.common.presentation.ErrorStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenUtil(
    private val accountProperties: AccountProperties,
    private val refreshTokenService: RefreshTokenService,
    private val accountJpaRepository: AccountJpaRepository,
) {

    private val jwt = accountProperties.jwt
    private var secretKey: SecretKey? = null

    init {
        val decodedKey = Base64.getDecoder().decode(jwt.secret)
        secretKey = Keys.hmacShaKeyFor(decodedKey)
    }

    fun checkTokenFromCookie(tokenType: String, request: HttpServletRequest): Boolean {
        val cookieHeader = request.getHeader("cookie") ?: return false
        return cookieHeader.contains(tokenType)
    }

    fun getTokenFromCookie(tokenType: String, request: HttpServletRequest): String {
        val cookieHeader = request.getHeader("cookie")
            ?: throw FilterException(ErrorStatus.UNAUTHORIZED, "헤더에 쿠키가 존재하지 않습니다.")

        val tokenValue = cookieHeader
            .split("; ")
            .map { it.split("=") }
            .firstOrNull { it.first() == tokenType }
            ?.let { it.getOrNull(1) ?: throw FilterException(ErrorStatus.UNAUTHORIZED, "헤더에 토큰이 존재하지 않습니다.") }
            ?: throw FilterException(ErrorStatus.UNAUTHORIZED, "헤더에 토큰이 존재하지 않습니다.")

        return tokenValue
    }

    fun generateToken(email: String, role: String): JwtTokenResponse {
        val accessToken: String = generateAccessToken(email, role)
        val refreshToken: String = generateRefreshToken(email, role)

        val token = refreshTokenService.saveTokenInfo(email, accessToken, refreshToken)
        return JwtTokenResponse(
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken,
                )
    }

    fun generateAccessToken(email: String, role: String): String {
        val now = Date()
        val accessExpiredDate = Date(now.time + jwt.accessExpirationTime)
        val claims: Claims = Jwts.claims().setSubject(email)
        claims["role"] = AccountRole.isType(role).value

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(accessExpiredDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateRefreshToken(email: String, role: String): String {
        val now = Date()
        val refreshExpiredDate = Date(now.time + jwt.refreshExpirationTime)
        val claims: Claims = Jwts.claims().setSubject(email)
        claims["role"] = AccountRole.isType(role).value

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(refreshExpiredDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun verifyToken(token: String): Boolean {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            return claims.body.expiration.after(Date())
        } catch (e: ExpiredJwtException) {
            throw FilterException(ErrorStatus.INVALID_TOKEN, "토큰이 만료되었습니다.")
        } catch (e: Exception) {
            when (e) {
                is JwtException, is IllegalArgumentException -> throw FilterException(ErrorStatus.INVALID_TOKEN, "유효하지 않은 토큰입니다.")
                else -> throw e
            }
        }
    }

    fun generateTokenCookie(tokenType: String, jwtToken: String): Cookie {
        return Cookie(tokenType, jwtToken).apply {
            path = "/"
            isHttpOnly = true // 요청 외 클라이언트에서 쿠키를 읽을 수 없도록 설정
            secure = false // 아직 https 적용 안함. 이후에 적용하면 true로 변경
            when (tokenType) {
                "accessToken" -> maxAge = accountProperties.jwt.accessExpirationTime.toInt() / 1000
                "refreshToken" -> maxAge = accountProperties.jwt.refreshExpirationTime.toInt() / 1000
            }
        }
    }

    fun getAuthentication(accessToken: String): Authentication {
        val findAccount = accountJpaRepository.findByEmail(getUid(accessToken))
            ?: throw FilterException(ErrorStatus.UNAUTHORIZED, "존재하지 않는 계정입니다.")
        val user: UserDetails = User.builder()
            .username(findAccount.email)
            .password("")
            .authorities(SimpleGrantedAuthority("ROLE_${findAccount.role}"))
            .build()
        return UsernamePasswordAuthenticationToken(user, "", user.authorities)
    }

    fun getUid(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun getRole(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .get("role", String::class.java)
    }
}
