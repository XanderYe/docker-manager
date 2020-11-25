package cn.xanderye.dockermanager.controller;

import cn.xanderye.dockermanager.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2020/11/25.
 *
 * @author XanderYe
 */
@RestController
@RequestMapping("container")
public class ContainerController {
    @Autowired
    private ContainerService containerService;

}
