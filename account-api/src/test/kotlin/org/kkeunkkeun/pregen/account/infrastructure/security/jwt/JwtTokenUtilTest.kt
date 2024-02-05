//package org.kkeunkkeun.pregen.account.infrastructure.security.jwt
//
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.kkeunkkeun.pregen.account.infrastructure.util.AccountTestSupport
//import org.springframework.beans.factory.annotation.Autowired
//
//class JwtTokenUtilTest(
//    @Autowired
//    private val jwtTokenUtil: JwtTokenUtil,
//): AccountTestSupport() {
//
//    private val secretKey = "tetsSecretKey"
//
//    @DisplayName("token을 생성할 수 있다.")
//    @Test
//    fun generateAccessToken() {
//        //given
//        val email = "test@test.com"
//        val role = "ROLE_MEMBER"
//
//        //when
//        val generateToken = jwtTokenUtil.generateToken(email, role)
//
//        //then
//        val token = refreshTokenRepository.findByAccessToken(generateToken.accessToken) ?: throw IllegalArgumentException()
//        assertThat(token.email).isEqualTo(email)
//    }
//}