package com.hanmj.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanmj.common.RespBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 权限不足时返回授权失败信息
 */
@Component
public class AuthenticationAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        RespBean error = RespBean.error("权限不足请联系管理员");
        out.write(new ObjectMapper().writeValueAsString(error));//对象转为json字符串
        out.flush();
        out.close();
    }
}
