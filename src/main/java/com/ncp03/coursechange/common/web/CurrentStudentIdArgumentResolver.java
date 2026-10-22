package com.ncp03.coursechange.common.web;

import com.ncp03.coursechange.common.error.ApiException;
import com.ncp03.coursechange.common.error.ErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentStudentIdArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String HEADER_NAME = "X-Student-Id";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentStudentId.class)
                && Long.class.equals(parameter.getParameterType());
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return parse(webRequest.getHeader(HEADER_NAME));
    }

    static Long parse(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new ApiException(ErrorCode.MISSING_STUDENT_ID);
        }
        try {
            long id = Long.parseLong(raw.trim());
            if (id < 1) {
                throw new ApiException(ErrorCode.INVALID_STUDENT_ID);
            }
            return id;
        } catch (NumberFormatException e) {
            throw new ApiException(ErrorCode.INVALID_STUDENT_ID);
        }
    }
}
