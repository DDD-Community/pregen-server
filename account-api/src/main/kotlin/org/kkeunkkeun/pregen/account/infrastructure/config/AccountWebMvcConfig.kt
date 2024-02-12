package org.kkeunkkeun.pregen.account.infrastructure.config

import org.kkeunkkeun.pregen.account.presentation.annotation.AccountEmailResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Account 관련 custom web mvc configuration을 부착
 */
@Configuration
class AccountWebMvcConfig: WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(AccountEmailResolver())
    }
}