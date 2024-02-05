package org.kkeunkkeun.pregen.account.infrastructure.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.kkeunkkeun.pregen.account.domain.AccountRole
import org.kkeunkkeun.pregen.account.infrastructure.AccountRepository
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
    private val accountRepository: AccountRepository,
) {

    private val jwt = accountProperties.jwt
    private var secretKey: SecretKey? = null

    init {
        val decodedKey = Base64.getDecoder().decode(jwt.secret)
        secretKey = Keys.hmacShaKeyFor(decodedKey)
    }

    fun extractToken(token: String?): String? {
        if (token != null) {
            if (!token.startsWith("Bearer ")) {
                throw IllegalArgumentException("Invalid token")
            }
            return token.split(" ")[1].trim()
        }
        return null;
    }

    fun generateToken(email: String, role: String): JwtTokenResponse {
        val accessToken: String = generateAccessToken(email, role)
        val refreshToken: String = generateRefreshToken(email, role)

        val token = refreshTokenService.saveTokenInfo(email, accessToken, refreshToken)
        return JwtTokenResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    tokenType = "Bearer",
                    expiresIn = accountProperties.jwt.accessExpirationTime,
                )
    }

    fun generateAccessToken(email: String, role: String): String {
        val now = Date()
        val accessExpiredDate = Date(now.time + jwt.accessExpirationTime)
        val claims: Claims = Jwts.claims().setSubject(email)
        claims["role"] = AccountRole.isType(role).value

        return Jwts.builder()
            .setClaims(claims)          // role 정보
            .setIssuedAt(now)           // 토큰 발행 시간 정보
            .setExpiration(accessExpiredDate) // 토큰 만료 시간
            .signWith(secretKey, SignatureAlgorithm.HS256) // 암호화 알고리즘, secret 값 세팅
            .compact()
    }

    fun generateRefreshToken(email: String, role: String): String {
        val now = Date()
        val refreshExpiredDate = Date(now.time + jwt.refreshExpirationTime)
        val claims: Claims = Jwts.claims().setSubject(email)
        claims["role"] = AccountRole.isType(role).value

        return Jwts.builder()
            .setClaims(claims)          // role 정보
            .setIssuedAt(now)           // 토큰 발행 시간 정보
            .setExpiration(refreshExpiredDate) // 토큰 만료 시간
            .signWith(secretKey, SignatureAlgorithm.HS256) // 암호화 알고리즘, secret 값 세팅
            .compact()
    }

    fun verifyToken(token: String): Boolean {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token.removePrefix("Bearer ").trim())
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

    fun getAuthentication(accessToken: String): Authentication {
        val findAccount = (accountRepository.findByEmail(getUid(accessToken))
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
            .parseClaimsJws(token.removePrefix("Bearer ").trim())
            .body
            .subject
    }

    fun getRole(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token.removePrefix("Bearer ").trim())
            .body
            .get("role", String::class.java)
    }
}