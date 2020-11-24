package cn.xanderye.dockermanager.util;

import cn.xanderye.dockermanager.entity.Container;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created on 2020/11/24.
 *
 * @author XanderYe
 */
public class DockerUtil {

    public static String path = "/var/lib/docker/containers";

    private final static String CONFIG_V2_FILE = "config.v2.json";

    private final static String HOST_CONFIG_FILE = "hostconfig.json";

    /**
     * 检查docker配置
     * @return boolean
     * @author XanderYe
     * @date 2020/11/24
     */
    public static boolean checkContainerPath() {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 获取所有容器配置
     * @param
     * @return java.util.List<cn.xanderye.dockermanager.entity.Container>
     * @author XanderYe
     * @date 2020/11/24
     */
    public static List<Container> getContainerList() throws IOException {
        File containerFile = new File(path);
        File[] containers = containerFile.listFiles();
        List<Container> containerList = new ArrayList<>();
        if (FileUtil.isNotEmpty(containers)) {
            for (File container : containers) {
                if (container.isDirectory()) {
                    File[] files = container.listFiles();
                    if (FileUtil.isNotEmpty(files)) {
                        JSONObject configV2 = null;
                        JSONObject hostConfig = null;
                        for (File file : files) {
                            if (file.isFile()) {
                                if (CONFIG_V2_FILE.equals(file.getName())) {
                                    configV2 = readConfig(file.getAbsolutePath());
                                } else if (HOST_CONFIG_FILE.equals(file.getName())) {
                                    hostConfig = readConfig(file.getAbsolutePath());
                                }
                            }
                        }
                        if (null != configV2 && null != hostConfig) {
                            Container ctn = parseConfig(configV2, hostConfig);
                            containerList.add(ctn);
                        }
                    }
                }
            }
        } else {
            throw new FileNotFoundException("路径错误，未获取到容器配置");
        }
        return containerList;
    }

    /**
     * 保存配置
     * @param containerList
     * @return void
     * @author XanderYe
     * @date 2020/11/24
     */
    public static void saveContainerList(List<Container> containerList) throws IOException {
        for (Container container : containerList) {
            saveContainer(container);
        }
    }

    /**
     * 解析配置
     * @param configV2
     * @param hostConfig
     * @return cn.xanderye.dockermanager.entity.Container
     * @author XanderYe
     * @date 2020/11/24
     */
    public static Container parseConfig(JSONObject configV2, JSONObject hostConfig) {
        Container container = new Container();
        container.setId(configV2.getString("ID"));
        container.setName(configV2.getString("Name").substring(1));
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
                String hostPort = portBinds.getJSONArray(key).getJSONObject(0).getString("HostPort");
                portBinding.setPort(port);
                portBinding.setHostPort(hostPort);
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

    /**
     * 保存单个容器配置
     * @param container
     * @return void
     * @author XanderYe
     * @date 2020/11/24
     */
    public static void saveContainer(Container container) throws IOException {
        String configV2Path = path + File.separator + container.getId() + File.separator + CONFIG_V2_FILE;
        String hostConfigPath = path + File.separator + container.getId() + File.separator + HOST_CONFIG_FILE;
        // 备份
        FileUtil.copyFile(configV2Path, configV2Path + ".bak");
        FileUtil.copyFile(hostConfigPath, hostConfigPath + ".bak");
        JSONObject configV2 = readConfig(configV2Path);
        JSONObject hostConfig = readConfig(hostConfigPath);

        // 设置环境变量
        List<Container.Env> envList = container.getEnvList();
        JSONArray envArray = new JSONArray();
        if (null != envList && !envList.isEmpty()) {
            for (Container.Env env : envList) {
                envArray.add(env.getKey() + "=" + env.getValue());
            }
        }
        configV2.put("Env", envArray);

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
                conf.put("RW", true);
                conf.put("Name", "");
                conf.put("Driver", "");
                conf.put("Type", "bind");
                conf.put("Propagation", "rprivate");
                JSONObject spec = new JSONObject(true);
                spec.put("Type", "bind");
                spec.put("Source", mountPoint.getSource());
                spec.put("Target", mountPoint.getTarget());
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
        if (null != portBindingList && !portBindingList.isEmpty()) {
            for (Container.PortBinding portBinding : portBindingList) {
                JSONObject host = new JSONObject(true);
                host.put("HostIp", "");
                host.put("HostPort", portBinding.getHostPort());
                JSONArray hostArray = new JSONArray();
                hostArray.add(host);
                portBindings.put(portBinding.getPort() + "/" + portBinding.getType(), hostArray);
            }
        }
        hostConfig.put("PortBindings", portBindings);

        // 设置容器是否自动重启
        JSONObject restartPolicy = hostConfig.getJSONObject("RestartPolicy");
        restartPolicy.put("Name", container.getRestartPolicy());
        hostConfig.put("RestartPolicy", restartPolicy);

        writeConfig(configV2, configV2Path);
        writeConfig(hostConfig, hostConfigPath);
    }

    /**
     * 读取配置
     * @param path
     * @return com.alibaba.fastjson.JSONObject
     * @author XanderYe
     * @date 2020/11/24
     */
    public static JSONObject readConfig(String path) {
        File file = new File(path);
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String s = bufferedReader.readLine();
            return JSON.parseObject(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写入配置
     * @param jsonObject
     * @param path
     * @return boolean
     * @author XanderYe
     * @date 2020/11/24
     */
    public static boolean writeConfig(JSONObject jsonObject, String path) {
        File file = new File(path);
        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(jsonObject.toJSONString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
