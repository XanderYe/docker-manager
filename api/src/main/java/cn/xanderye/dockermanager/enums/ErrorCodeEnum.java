package cn.xanderye.dockermanager.enums;

import lombok.Getter;

/**
 *
 * @author XanderYe
 * @date 2018-11-05
 */
@Getter
public enum ErrorCodeEnum {
    // 错误码
    LOGIN_ERROR(10000, "用户名或密码错误"),
    AUTHORIZATION_FAILED(10001, "账号认证异常"),
    RUN_WITH_ROOT(10100, "请使用root账户启动"),
    INSTALL_DOCKER_FIRST(10101, "请先安装docker"),
    PATH_ERROR(10102, "路径配置错误，无法获取到容器"),
    PARAMETER_ERROR(10103, "参数错误"),
    PARAMETER_EMPTY(10104, "参数不为空");

    private int code;
    private String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
