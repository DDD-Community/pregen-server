package org.kkeunkkeun.pregen.account.infrastructure.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.kkeunkkeun.pregen.account.infrastructure.config.AccountProperties
import org.kkeunkkeun.pregen.common.infrastructure.RedisService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenProvider(
    private val accountProperties: AccountProperties,
    private val jwtTokenUtil: JwtTokenUtil,
    private val redisService: RedisService,
) {

    private val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accountProperties.jwt.secret))

    fun createdJwtToken(username: String, authorities: String): JwtTokenResponse {
        val createdTime: Date = Date()
        val claims = Jwts.claims().setSubject(username)
        claims["roles"] = authorities

        val accessToken: String = createdAccessToken(claims, Date(createdTime.time + accountProperties.jwt.accessExpirationTime))
        val refreshToken: String = createdRefreshToken(claims, Date(createdTime.time + accountProperties.jwt.refreshExpirationTime))

        return JwtTokenResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    tokenType = "Bearer",
                    expiresIn = accountProperties.jwt.accessExpirationTime,
                )
    }

    private fun createdAccessToken(claims: Claims, expiredDate: Date): String {
        return Jwts.builder()
            .setClaims(claims)          // role 정보
            .setExpiration(expiredDate) // 토큰 만료 시간
            .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘, secret 값 세팅
            .compact()
    }

    private fun createdRefreshToken(claims: Claims, expiredDate: Date): String {
        val refreshToken = Jwts.builder()
            .setClaims(claims)          // role 정보
            .setExpiration(expiredDate) // 토큰 만료 시간
            .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘, secret 값 세팅
            .compact()
        jwtTokenUtil.setRefreshToken(refreshToken, claims.subject)
        return refreshToken
    }

    fun getAuthenticationByAccessToken(accessToken: String): Authentication {
        val claims: Claims
        try {
            claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .body
        } catch (e: ExpiredJwtException) {
            throw IllegalArgumentException("Expired access token")
        } catch (e: Exception) {
            when (e) {
                is JwtException, is IllegalArgumentException -> throw IllegalArgumentException("Invalid access token")
                else -> throw e
            }
        }

        val authorities: Collection<GrantedAuthority> =
            claims["roles"].toString().split(",")
                .map { SimpleGrantedAuthority(it) }

        val userDetails: UserDetails = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(userDetails, "", authorities)
    }

    fun validateAccessToken(accessToken: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
            return true
        } catch (e: ExpiredJwtException) {
            throw IllegalArgumentException("Expired access token")
        } catch (e: Exception) {
            when (e) {
                is JwtException, is IllegalArgumentException -> throw IllegalArgumentException("Invalid access token")
                else -> throw e
            }
        }
    }

    fun validateRefreshToken(refreshToken: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
            return true
        } catch (e: ExpiredJwtException) {
            throw IllegalArgumentException("Expired refresh token")
        } catch (e: Exception) {
            when (e) {
                is JwtException, is IllegalArgumentException -> throw IllegalArgumentException("Invalid refresh token")
                else -> throw e
            }
        }
    }
}