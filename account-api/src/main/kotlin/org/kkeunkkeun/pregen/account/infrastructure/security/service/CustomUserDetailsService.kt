//package org.kkeunkkeun.pregen.account.infrastructure.security
//
//import org.kkeunkkeun.pregen.account.infrastructure.AccountRepository
//import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.userdetails.User
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.stereotype.Service
//
//@Service("userDetailsService")
//class CustomUserDetailsService(
//    private val accountRepository: AccountRepository,
//): UserDetailsService {
//
//    @Override
//    override fun loadUserByUsername(username: String): UserDetails {
//        val account = accountRepository.findByEmail(username)
//            .orElseThrow { throw NotFoundException() }
//        val authoritites: List<GrantedAuthority> = MutableList(1) {
//            SimpleGrantedAuthority("ROLE_" + account.role.name)
//        }
//
//    }
//}