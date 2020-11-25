package cn.xanderye.dockermanager.config;

import cn.xanderye.dockermanager.enums.ErrorCodeEnum;
import cn.xanderye.dockermanager.exception.BusinessException;
import cn.xanderye.dockermanager.util.DockerUtil;
import cn.xanderye.dockermanager.util.SystemUtil;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created on 2020/11/24.
 *
 * @author XanderYe
 */
@Configuration
public class DockerConfiguration {

    @PostConstruct
    public void init() {
        String user = SystemUtil.execStr("whoami");
        if (!"root".equals(user)) {
            throw new BusinessException(ErrorCodeEnum.RUN_WITH_ROOT);
        }
        if (!DockerUtil.checkDocker()) {
            throw new BusinessException(ErrorCodeEnum.INSTALL_DOCKER_FIRST);
        }
    }
}
