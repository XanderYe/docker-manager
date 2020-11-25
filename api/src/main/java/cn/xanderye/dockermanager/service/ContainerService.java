package cn.xanderye.dockermanager.service;

import cn.xanderye.dockermanager.entity.Container;
import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created on 2020/11/25.
 *
 * @author XanderYe
 */
public interface ContainerService {

    /**
     * 解析容器状态
     * @param configV2
     * @param hostConfig
     * @return cn.xanderye.dockermanager.entity.Container
     * @author XanderYe
     * @date 2020/11/25
     */
    Container parseStatus(JSONObject configV2, JSONObject hostConfig);

    /**
     * 解析容器配置
     * @param configV2
     * @param hostConfig
     * @return cn.xanderye.dockermanager.entity.Container
     * @author XanderYe
     * @date 2020/11/25
     */
    Container parseConfig(JSONObject configV2, JSONObject hostConfig);

    /**
     * 根据容器id获取容器
     * @param id
     * @return cn.xanderye.dockermanager.entity.Container
     * @author XanderYe
     * @date 2020/11/25
     */
    Container getContainer(String id);

    /**
     * 获取所有容器
     * @param
     * @return java.util.List<cn.xanderye.dockermanager.entity.Container>
     * @author XanderYe
     * @date 2020/11/25
     */
    List<Container> getContainerStatusList();

    /**
     * 获取所有容器详细配置
     * @param
     * @return java.util.List<cn.xanderye.dockermanager.entity.Container>
     * @author XanderYe
     * @date 2020/11/25
     */
    List<Container> getContainerConfigList();

    /**
     * 保存容器配置
     * @param container
     * @return void
     * @author XanderYe
     * @date 2020/11/25
     */
    void saveContainer(Container container);

    /**
     * 批量保存容器配置
     * @param containerList
     * @return void
     * @author XanderYe
     * @date 2020/11/25
     */
    void saveContainerList(List<Container> containerList);

    /**
     * 启动容器
     * @param
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/25
     */
    String startContainer(String id);

    /**
     * 停止容器
     * @param
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/25
     */
    String stopContainer(String id);

    /**
     * 重启容器
     * @param
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/25
     */
    String restartContainer(String id);
}
