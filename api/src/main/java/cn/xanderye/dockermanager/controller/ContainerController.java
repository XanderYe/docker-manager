package cn.xanderye.dockermanager.controller;

import cn.xanderye.dockermanager.base.ResultBean;
import cn.xanderye.dockermanager.entity.Container;
import cn.xanderye.dockermanager.service.ContainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020/11/25.
 *
 * @author XanderYe
 */
@Slf4j
@RestController
@RequestMapping("container")
public class ContainerController {
    @Autowired
    private ContainerService containerService;

    @GetMapping("test")
    public ResultBean test() {
        List<Container> containerList = containerService.getContainerConfigList();
        Container container = null;
        for (Container container1 : containerList) {
            if (container1.getName().contains("nginx")) {
                container = container1;
            }
        }
        log.info("获取到容器配置：{}", container.toString());
        List<Container.PortBinding> portBindingList = new ArrayList<>();
        Container.PortBinding portBinding = new Container.PortBinding();
        portBinding.setPort("80");
        portBinding.setHostPort("80");
        portBinding.setType("tcp");
        portBindingList.add(portBinding);
        container.setPortBindingList(portBindingList);
        List<Container.MountPoint> mountPointList = new ArrayList<>();
        Container.MountPoint mountPoint = new Container.MountPoint();
        mountPoint.setSource("/mnt/d/test");
        mountPoint.setTarget("/test");
        mountPointList.add(mountPoint);
        container.setMountPointList(mountPointList);
        log.info("修改后的容器配置：{}", container.toString());
        containerService.saveContainer(container);
        return new ResultBean();
    }
}
