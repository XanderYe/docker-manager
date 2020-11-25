package cn.xanderye.dockermanager.controller;

import cn.xanderye.dockermanager.base.ResultBean;
import cn.xanderye.dockermanager.service.DockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2020/11/25.
 *
 * @author XanderYe
 */
@RestController
@RequestMapping("docker")
public class DockerController {
    @Autowired
    private DockerService dockerService;

    @PostMapping("start")
    public ResultBean start() {
        return new ResultBean<>(dockerService.startDocker());
    }

    @PostMapping("stop")
    public ResultBean stop() {
        return new ResultBean<>(dockerService.stopDocker());
    }

    @PostMapping("restart")
    public ResultBean restartDocker() {
        return new ResultBean<>(dockerService.restartDocker());
    }
}
