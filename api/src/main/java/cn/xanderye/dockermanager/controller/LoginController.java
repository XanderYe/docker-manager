package cn.xanderye.dockermanager.controller;

import cn.xanderye.dockermanager.base.ResultBean;
import cn.xanderye.dockermanager.enums.ErrorCodeEnum;
import cn.xanderye.dockermanager.exception.BusinessException;
import cn.xanderye.dockermanager.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2020/11/26.
 *
 * @author XanderYe
 */
@RestController
@RequestMapping
public class LoginController {
    @Value("${user.username}")
    private String sysUsername;

    @Value("${user.password}")
    private String sysPassword;

    @PostMapping("login")
    public ResultBean login(String username, String password) {
        if (StringUtils.isAnyEmpty(username, password)) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_EMPTY);
        }
        if (!sysUsername.equals(username) || !sysPassword.equals(password)) {
            throw new BusinessException(ErrorCodeEnum.LOGIN_ERROR);
        }
        String jwt = JwtUtil.encode(username);
        return new ResultBean<>(jwt);
    }
}
