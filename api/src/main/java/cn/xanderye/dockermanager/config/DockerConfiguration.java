package cn.xanderye.dockermanager.config;

import cn.xanderye.dockermanager.entity.Container;
import cn.xanderye.dockermanager.util.DockerUtil;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created on 2020/11/24.
 *
 * @author XanderYe
 */
@Configuration
public class DockerConfiguration {


    private static List<Container> containerList;

    @PostConstruct
    public void init() throws FileNotFoundException {
        if (!DockerUtil.checkContainerPath()) {
            throw new FileNotFoundException("请先安装docker");
        }

    }
}
