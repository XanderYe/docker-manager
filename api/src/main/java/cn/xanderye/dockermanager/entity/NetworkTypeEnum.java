package cn.xanderye.dockermanager.entity;

/**
 * Created on 2020/11/24.
 *
 * @author 叶振东
 */
public enum NetworkTypeEnum {

    NONE(0),

    BRIDGE(1),

    NET(2);

    private Integer value;

    NetworkTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
