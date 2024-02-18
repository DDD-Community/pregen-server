package org.kkeunkkeun.pregen.account.infrastructure.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.kkeunkkeun.pregen.account.domain.AccountRole
import org.kkeunkkeun.pregen.account.infrastructure.AccountJpaRepository
import org.kkeunkkeun.pregen.account.infrastructure.config.AccountProperties
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.dto.JwtTokenResponse
import org.kkeunkkeun.pregen.account.infrastructure.security.jwt.refreshtoken.RefreshTokenService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
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

    fun getTokenFromCookie(tokenType: String, request: HttpServletRequest): String {
        // 'cookie' 헤더 값을 가져옵니다.
        val cookieHeader = request.getHeader("cookie")
            ?: throw IllegalArgumentException("쿠키 헤더가 요청에 존재하지 않습니다.")

        // 쿠키 헤더에서 토큰 타입에 해당하는 값을 파싱
        val tokenValue = cookieHeader
            .split("; ")
            .map { it.split("=") } // 각 쿠키를 "=" 기호를 사용하여 키와 값으로 분리
            .firstOrNull { it.first() == tokenType } // 토큰 타입에 해당하는 쌍을 찾음
            ?.let { it.getOrNull(1) ?: throw IllegalArgumentException("쿠키 값이 '$tokenType'로 올바르게 시작하지 않습니다.") }
            ?: throw IllegalArgumentException("$tokenType 토큰이 쿠키 값에 존재하지 않습니다.")

        // tokenType에 해당하는 토큰 값을 반환합니다.
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
            throw IllegalArgumentException("Expired access token")
        } catch (e: Exception) {
            when (e) {
                is JwtException, is IllegalArgumentException -> throw IllegalArgumentException("Invalid access token")
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
                "accessToken" -> maxAge = 15 * 60
                "refreshToken" -> maxAge = 15 * 24 * 60 * 60
            }
        }
    }

    fun getAuthentication(accessToken: String): Authentication {
        val findAccount = (accountJpaRepository.findByEmail(getUid(accessToken))
            ?: throw IllegalArgumentException("존재하지 않는 계정입니다."))
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
