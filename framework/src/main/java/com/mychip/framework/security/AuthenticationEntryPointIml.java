package com.mychip.framework.security;

import com.alibaba.fastjson.JSON;

import com.mychip.common.core.domain.AjaxResult;
import com.mychip.common.utils.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

import com.mychip.common.constant.HttpStatus;
import com.mychip.common.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 认证失败处理类 返回未授权
 * @author zhouyu
 */
@Component
public class AuthenticationEntryPointIml implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }
}
