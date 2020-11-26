package cn.xanderye.dockermanager.interceptor;

import cn.xanderye.dockermanager.constant.Constant;
import cn.xanderye.dockermanager.enums.ErrorCodeEnum;
import cn.xanderye.dockermanager.exception.BusinessException;
import cn.xanderye.dockermanager.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Created by Xander on 2018-11-05.
 */
@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String remoteAddr = request.getRemoteAddr();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        //不拦截OPTION请求
        if (method.equals(RequestMethod.OPTIONS.name())) {
            return true;
        }

        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(ErrorCodeEnum.AUTHORIZATION_FAILED);
        }
        String username;
        try {
            username = JwtUtil.decode(token);
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.AUTHORIZATION_FAILED);
        }
        if (!Constant.IGNORED_URI_LIST.contains(uri)) {
            log.info("remoteAddr={},  method={}, uri={}, username={}", remoteAddr, method, uri, username);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
