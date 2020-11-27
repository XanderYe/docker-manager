package cn.xanderye.dockermanager.service.impl;

import cn.xanderye.dockermanager.service.DockerService;
import cn.xanderye.dockermanager.util.DockerUtil;
import cn.xanderye.dockermanager.util.SystemUtil;
import org.springframework.stereotype.Service;

/**
 * Created on 2020/11/25.
 *
 * @author XanderYe
 */
@Service
public class DockerServiceImpl implements DockerService {
    @Override
    public String startDocker() {
        String cmd = DockerUtil.systemd ? "systemctl start docker" : "service docker start";
        return SystemUtil.execStr(cmd);
    }

    @Override
    public String stopDocker() {
        String cmd = DockerUtil.systemd ? "systemctl stop docker" : "service docker stop";
        return SystemUtil.execStr(cmd);
    }

    @Override
    public String restartDocker() {
        String cmd = DockerUtil.systemd ? "systemctl restart docker" : "service docker restart";
        return SystemUtil.execStr(cmd);
    }
}
