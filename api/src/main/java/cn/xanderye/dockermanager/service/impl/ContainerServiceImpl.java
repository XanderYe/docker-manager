package cn.xanderye.dockermanager.service.impl;

import cn.xanderye.dockermanager.entity.Container;
import cn.xanderye.dockermanager.enums.ErrorCodeEnum;
import cn.xanderye.dockermanager.exception.BusinessException;
import cn.xanderye.dockermanager.service.ContainerService;
import cn.xanderye.dockermanager.util.DockerUtil;
import cn.xanderye.dockermanager.util.FileUtil;
import cn.xanderye.dockermanager.util.SystemUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created on 2020/11/25.
 *
 * @author XanderYe
 */
@Service
public class ContainerServiceImpl implements ContainerService {

    @Override
    public Container parseStatus(JSONObject configV2, JSONObject hostConfig) {
        Container container = new Container();
        container.setId(configV2.getString("ID"));
        container.setName(configV2.getString("Name").substring(1));
        JSONObject state = configV2.getJSONObject("State");
        if (state.getBoolean("Running")) {
            container.setState("运行中");
        } else if (state.getBoolean("Paused")) {
            container.setState("暂停中");
        } else if (state.getBoolean("Restarting")) {
            container.setState("重启中");
        } else {
            container.setState("已停止");
        }
        try {
            String dateString = state.getString("StartedAt").replace("T", " ");
            dateString = dateString.substring(0, dateString.lastIndexOf("."));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            container.setStartTime(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject config = configV2.getJSONObject("Config");
        String image = config.getString("Image");
        if (!image.contains(":")) {
            image += ":latest";
        }
        container.setImage(image);
        return container;
    }

    @Override
    public Container parseConfig(JSONObject configV2, JSONObject hostConfig) {
        Container container = new Container();
        container.setId(configV2.getString("ID"));
        container.setName(configV2.getString("Name").substring(1));
        JSONObject state = configV2.getJSONObject("State");
        if (state.getBoolean("Running")) {
            container.setState("运行中");
        } else if (state.getBoolean("Paused")) {
            container.setState("暂停中");
        } else if (state.getBoolean("Restarting")) {
            container.setState("重启中");
        } else {
            container.setState("已停止");
        }

        JSONObject config = configV2.getJSONObject("Config");
        JSONArray envArray = config.getJSONArray("Env");

        // 获取环境变量
        List<Container.Env> envList = new ArrayList<>();
        if (!envArray.isEmpty()) {
            for (int i = 0; i < envArray.size(); i++) {
                Container.Env env = new Container.Env();
                String envString = envArray.getString(i);
                String[] con = envString.split("=");
                env.setKey(con[0]);
                env.setValue(con.length == 1 ? "" : con[1]);
                envList.add(env);
            }
        }
        container.setEnvList(envList);

        // 获取映射目录
        List<Container.MountPoint> mountPointList = new ArrayList<>();
        JSONObject mountPoints = configV2.getJSONObject("MountPoints");
        Set<String> mountPointsKeySet = mountPoints.keySet();
        if (!mountPoints.isEmpty()) {
            for (String key : mountPointsKeySet) {
                JSONObject jsonObject = mountPoints.getJSONObject(key);
                Container.MountPoint mountPoint = new Container.MountPoint();
                mountPoint.setSource(jsonObject.getString("Source"));
                mountPoint.setTarget(jsonObject.getString("Destination"));
                mountPoint.setReadOnly(!jsonObject.getBoolean("RW"));
                mountPointList.add(mountPoint);
            }
        }
        container.setMountPointList(mountPointList);

        // 获取映射端口
        JSONObject portBinds = hostConfig.getJSONObject("PortBindings");
        List<Container.PortBinding> portBindingList = new ArrayList<>();
        Set<String> portBindsKeySet = portBinds.keySet();
        if (!portBindsKeySet.isEmpty()) {
            for (String key : portBindsKeySet) {
                Container.PortBinding portBinding = new Container.PortBinding();
                String port = key.split("/")[0];
                String type = key.split("/")[1];
                String hostPort = portBinds.getJSONArray(key).getJSONObject(0).getString("HostPort");
                portBinding.setPort(port);
                portBinding.setHostPort(hostPort);
                portBinding.setType(type);
                portBindingList.add(portBinding);
            }
        }
        container.setPortBindingList(portBindingList);

        // 获取容器网络模式
        String networkMode = hostConfig.getString("NetworkMode");
        container.setNetworkMode(networkMode);

        // 获取容器是否自动重启
        String restartPolicy = hostConfig.getJSONObject("RestartPolicy").getString("Name");
        container.setRestartPolicy(restartPolicy);
        return container;
    }

    @Override
    public Container getContainer(String id) {
        JSONObject configV2 = DockerUtil.readConfigV2(id);
        JSONObject hostConfig = DockerUtil.readHostConfig(id);
        if (null != configV2 && null != hostConfig) {
            return parseConfig(configV2, hostConfig);
        }
        return null;
    }

    @Override
    public List<Container> getContainerStatusList() {
        File[] containers = DockerUtil.getContainerFiles();
        List<Container> containerList = new ArrayList<>();
        if (FileUtil.isNotEmpty(containers)) {
            for (File container : containers) {
                if (container.isDirectory()) {
                    String id = container.getName();
                    JSONObject configV2 = DockerUtil.readConfigV2(id);
                    JSONObject hostConfig = DockerUtil.readHostConfig(id);
                    Container ctn = parseStatus(configV2, hostConfig);
                    containerList.add(ctn);
                }
            }
        } else {
            throw new BusinessException(ErrorCodeEnum.PATH_ERROR);
        }
        return containerList;
    }

    @Override
    public List<Container> getContainerConfigList() {
        File[] containers = DockerUtil.getContainerFiles();
        List<Container> containerList = new ArrayList<>();
        if (FileUtil.isNotEmpty(containers)) {
            for (File container : containers) {
                if (container.isDirectory()) {
                    String id = container.getName();
                    JSONObject configV2 = DockerUtil.readConfigV2(id);
                    JSONObject hostConfig = DockerUtil.readHostConfig(id);
                    Container ctn = parseConfig(configV2, hostConfig);
                    containerList.add(ctn);
                }
            }
        } else {
            throw new BusinessException(ErrorCodeEnum.PATH_ERROR);
        }
        return containerList;
    }

    @Override
    public void saveContainer(Container container) {
        String id = container.getId();

        JSONObject configV2 = DockerUtil.readConfigV2(id);
        JSONObject hostConfig = DockerUtil.readHostConfig(id);

        JSONObject config = configV2.getJSONObject("Config");

        // 设置环境变量
        List<Container.Env> envList = container.getEnvList();
        JSONArray envArray = new JSONArray();
        if (null != envList && !envList.isEmpty()) {
            for (Container.Env env : envList) {
                envArray.add(env.getKey() + "=" + env.getValue());
            }
        }
        config.put("Env", envArray);

        // 设置映射路径
        List<Container.MountPoint> mountPointList = container.getMountPointList();
        JSONObject configMountPoints = new JSONObject(true);
        JSONArray hostConfigBinds = new JSONArray();
        if (null != mountPointList && !mountPointList.isEmpty()) {
            for (Container.MountPoint mountPoint : mountPointList) {
                // 设置config.v2.json
                JSONObject conf = new JSONObject(true);
                conf.put("Source", mountPoint.getSource());
                conf.put("Destination", mountPoint.getTarget());
                conf.put("Name", "");
                conf.put("Driver", "");
                conf.put("Type", "bind");
                conf.put("Propagation", "rprivate");
                JSONObject spec = new JSONObject(true);
                spec.put("Type", "bind");
                spec.put("Source", mountPoint.getSource());
                spec.put("Target", mountPoint.getTarget());
                if (mountPoint.getReadOnly()) {
                    conf.put("RW", false);
                    conf.put("Relabel", "ro");
                    spec.put("ReadOnly", true);
                } else {
                    conf.put("RW", true);
                    conf.put("Relabel", "rw");
                }
                conf.put("Spec", spec);
                conf.put("SkipMountpointCreation", false);
                configMountPoints.put(mountPoint.getTarget(), conf);
                //设置hostconfig.json
                hostConfigBinds.add(mountPoint.getSource() + ":" + mountPoint.getTarget());
            }
        }
        configV2.put("MountPoints", configMountPoints);
        hostConfig.put("Binds", hostConfigBinds);

        // 设置映射端口
        List<Container.PortBinding> portBindingList = container.getPortBindingList();
        JSONObject portBindings = new JSONObject(true);
        JSONObject exposedPorts = new JSONObject(true);
        if (null != portBindingList && !portBindingList.isEmpty()) {
            for (Container.PortBinding portBinding : portBindingList) {
                String name = portBinding.getPort() + "/" + portBinding.getType();
                exposedPorts.put(name, new JSONObject());
                JSONObject host = new JSONObject(true);
                host.put("HostIp", "");
                host.put("HostPort", portBinding.getHostPort());
                JSONArray hostArray = new JSONArray();
                hostArray.add(host);
                portBindings.put(name, hostArray);
            }
        }
        config.put("ExposedPorts", exposedPorts);
        hostConfig.put("PortBindings", portBindings);

        // 设置容器是否自动重启
        JSONObject restartPolicy = hostConfig.getJSONObject("RestartPolicy");
        restartPolicy.put("Name", container.getRestartPolicy());
        hostConfig.put("RestartPolicy", restartPolicy);

        configV2.put("Config", config);

        boolean needStop = null == container.getState() || "Running".equals(container.getState());
        if (needStop) {
            // 修改配置前停止容器
            stopContainer(container.getId());
        }
        DockerUtil.writeConfigV2(id, configV2);
        DockerUtil.writeHostConfig(id, hostConfig);
        if (needStop) {
            // 重启启动停止容器
            startContainer(container.getId());
        }
    }

    @Override
    public void saveContainerList(List<Container> containerList) {
        for (Container container : containerList) {
            saveContainer(container);
        }
    }

    @Override
    public String startContainer(String id) {
        return SystemUtil.execStr("docker start " + id);
    }

    @Override
    public String stopContainer(String id) {
        return SystemUtil.execStr("docker stop " + id);
    }

    @Override
    public String restartContainer(String id) {
        return SystemUtil.execStr("docker restart " + id);
    }
}
