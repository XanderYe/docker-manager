package cn.xanderye.dockermanager.service.impl;

import cn.xanderye.dockermanager.service.DockerService;
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
        return SystemUtil.execStr("systemctl start docker");
    }

    @Override
    public String stopDocker() {
        return SystemUtil.execStr("systemctl stop docker");
    }

    @Override
    public String restartDocker() {
        return SystemUtil.execStr("systemctl restart docker");
    }
}
