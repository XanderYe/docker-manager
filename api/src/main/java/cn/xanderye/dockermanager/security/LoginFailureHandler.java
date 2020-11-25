package cn.xanderye.dockermanager.security;

import cn.xanderye.dockermanager.base.ResultBean;
import cn.xanderye.dockermanager.enums.ErrorCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created on 2020/8/8.
 *
 * @author XanderYe
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setStatus(200);
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(ResultBean.error(ErrorCodeEnum.LOGIN_ERROR).toJSONString());
        writer.flush();
        writer.close();
    }
}
