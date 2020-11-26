package cn.xanderye.dockermanager.controller;

import cn.xanderye.dockermanager.base.ResultBean;
import cn.xanderye.dockermanager.constant.Constant;
import cn.xanderye.dockermanager.entity.Container;
import cn.xanderye.dockermanager.enums.ErrorCodeEnum;
import cn.xanderye.dockermanager.exception.BusinessException;
import cn.xanderye.dockermanager.service.ContainerService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取容器状态
     * @param
     * @return cn.xanderye.dockermanager.base.ResultBean
     * @author XanderYe
     * @date 2020/11/25
     */
    @GetMapping("getContainerStatusList")
    public ResultBean getContainerStatusList() {
        return new ResultBean<>(containerService.getContainerStatusList());
    }

    /**
     * 获取所有容器配置
     * @param
     * @return cn.xanderye.dockermanager.base.ResultBean
     * @author XanderYe
     * @date 2020/11/25
     */
    @GetMapping("getContainerConfigList")
    public ResultBean getContainerConfigList() {
        return new ResultBean<>(containerService.getContainerConfigList());
    }

    /**
     * 获取容器配置
     * @param id
     * @return cn.xanderye.dockermanager.base.ResultBean
     * @author XanderYe
     * @date 2020/11/25
     */
    @GetMapping("getContainerConfig")
    public ResultBean getContainerConfig(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_EMPTY);
        }
        Container container = containerService.getContainer(id);
        if (null == container) {
            throw new BusinessException("容器id" + id + "不存在");
        }
        return new ResultBean<>(container);
    }

    /**
     * 保存容器配置
     * @param container
     * @return cn.xanderye.dockermanager.base.ResultBean
     * @author XanderYe
     * @date 2020/11/25
     */
    @PostMapping("saveContainer")
    public ResultBean saveContainer(@RequestBody Container container) {
        if (null == container || null == container.getId()) {
            throw new BusinessException(ErrorCodeEnum.PARAMETER_EMPTY);
        }
        containerService.saveContainer(container);
        return new ResultBean<>();
    }

    @PostMapping("start")
    public ResultBean start(@RequestBody JSONObject params) {
        String id = params.getString("id");
        String res = containerService.startContainer(id);
        if (res.contains(Constant.CONTAINER_ERROR)) {
            return ResultBean.error(res);
        }
        return new ResultBean<>(res);
    }

    @PostMapping("stop")
    public ResultBean stop(@RequestBody JSONObject params) {
        String id = params.getString("id");
        String res = containerService.stopContainer(id);
        if (res.contains(Constant.CONTAINER_ERROR)) {
            return ResultBean.error(res);
        }
        return new ResultBean<>(res);
    }

    @PostMapping("restart")
    public ResultBean restart(@RequestBody JSONObject params) {
        String id = params.getString("id");
        String res = containerService.restartContainer(id);
        if (res.contains(Constant.CONTAINER_ERROR)) {
            return ResultBean.error(res);
        }
        return new ResultBean<>(res);
    }
}
