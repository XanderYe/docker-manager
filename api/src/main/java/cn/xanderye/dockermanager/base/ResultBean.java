package cn.xanderye.dockermanager.base;

import cn.xanderye.dockermanager.enums.ErrorCodeEnum;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 * @author XanderYe
 * @date 2019-01-10
 */
@Data
@Accessors(chain = true)
public class ResultBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String msg = "success";

    private int code = 0;
    private T data;

    public ResultBean() {
    }

    public ResultBean(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public ResultBean(T data) {
        this.data = data;
    }

    public static <T> ResultBean success(T data) {
        return new ResultBean<T>().setData(data);
    }

    public static ResultBean error(ErrorCodeEnum errorCode) {
        return new ResultBean(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> ResultBean error(String msg) {
        return new ResultBean<>().setCode(1).setMsg(msg);
    }

    public String toJSONString() {
        return JSON.toJSON(this).toString();
    }
}
