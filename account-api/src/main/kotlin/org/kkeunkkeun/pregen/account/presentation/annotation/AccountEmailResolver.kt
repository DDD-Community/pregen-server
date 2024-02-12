package org.kkeunkkeun.pregen.account.presentation.annotation

import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * AccountEmail 어노테이션이 부착된 파라미터에 요청 계정의 email을 주입해줌.
 */
class AccountEmailResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasAnnotation = parameter.hasParameterAnnotation(AccountEmail::class.java)
        val isString = String::class.java.isAssignableFrom(parameter.parameterType)

        return hasAnnotation && isString
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        return SecurityContextHolder.getContext().authentication.name
    }
}