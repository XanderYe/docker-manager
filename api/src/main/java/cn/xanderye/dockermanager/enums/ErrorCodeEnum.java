package cn.xanderye.dockermanager.enums;

import lombok.Getter;

/**
 *
 * @author XanderYe
 * @date 2018-11-05
 */
@Getter
public enum ErrorCodeEnum {
    /*错误码*/
    ACCOUNT_EXPIRED(10100, "用户信息已过期"),
    USER_CHECK_FAIL(10101, "用户信息校验失败！"),
    EMPTY_CODE(10102, "登录凭证不存在！"),
    ACCOUNT_OR_PASSWORD_ERROR(10103, "用户名或密码错误！"),
    ACCOUNT_AUTH_ERROR(10104, "账号认证异常！"),
    ACCOUNT_DISABLED(10105, "账号被禁用！"),
    PARAMETER_ERROR(10202, "参数错误！"),
    PARAMETER_EMPTY(10203, "参数不为空！"),
    CAPTCHA_ERROR(10204, "验证码错误！");
    private int code;
    private String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
