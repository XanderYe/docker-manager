package cn.xanderye.dockermanager.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created on 2020/11/24.
 *
 * @author XanderYe
 */
@Data
public class Container {

    private String id;

    private String name;

    private String state;

    private String image;

    private String networkMode;

    private String restartPolicy;

    private Date startTime;

    private List<PortBinding> portBindingList;

    private List<MountPoint> mountPointList;

    private List<Env> envList;

    @Data
    public static class PortBinding {

        private String type;

        private String port;

        private String hostPort;
    }

    @Data
    public static class MountPoint {

        private String source;

        private String target;

        private Boolean readOnly;
    }

    @Data
    public static class Env {

        private String key;

        private String value;
    }
}
