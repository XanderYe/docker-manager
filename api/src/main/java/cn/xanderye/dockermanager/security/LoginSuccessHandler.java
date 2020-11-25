package cn.xanderye.dockermanager.security;

import cn.xanderye.dockermanager.base.ResultBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setStatus(200);
        PrintWriter writer = httpServletResponse.getWriter();
        ResultBean<String> resultBean = new ResultBean<>();
        resultBean.setMsg("登录成功");
        writer.write(resultBean.toJSONString());
        writer.flush();
        writer.close();
    }
}
