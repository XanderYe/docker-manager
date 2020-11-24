package cn.xanderye.dockermanager.util;

import cn.xanderye.dockermanager.entity.Container;
import cn.xanderye.dockermanager.entity.NetworkTypeEnum;
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
 * @author 叶振东
 */
public class DockerUtil {

    private final static String CONFIG_V2_FILE = "config.v2.json";

    private final static String HOST_CONFIG_FILE = "hostconfig.json";

    /**
     * 检查docker配置
     * @param path
     * @return boolean
     * @author yezhendong
     * @date 2020/11/24
     */
    public static boolean checkContainerPath(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static List<Container> getContainerList(String path) {
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
        }
        return containerList;
    }

    /**
     * 解析配置
     * @param configV2
     * @param hostConfig
     * @return cn.xanderye.dockermanager.entity.Container
     * @author yezhendong
     * @date 2020/11/24
     */
    public static Container parseConfig(JSONObject configV2, JSONObject hostConfig) {
        Container container = new Container();
        container.setId(configV2.getString("ID"));
        container.setName(configV2.getString("Name"));
        JSONObject config = configV2.getJSONObject("Config");
        JSONArray envArray = config.getJSONArray("Env");
        // 设置环境变量
        List<Container.Env> envList = new ArrayList<>();
        if (!envArray.isEmpty()) {
            for (int i = 0; i < envArray.size(); i++) {
                Container.Env env = new Container.Env();
                String envString = envArray.getString(i);
                String[] con = envString.split("=");
                env.setKey(con[0]);
                env.setValue(con[1]);
                envList.add(env);
            }
        }
        container.setEnvList(envList);

        // 设置映射目录
        List<Container.MountPoint> mountPointList = new ArrayList<>();
        JSONObject mountPoints = configV2.getJSONObject("MountPoints");
        Set<String> mountPointsKeySet = mountPoints.keySet();
        if (!mountPoints.isEmpty()) {
            for (String key : mountPointsKeySet) {
                JSONObject jsonObject = mountPoints.getJSONObject(key);
                Container.MountPoint mountPoint = new Container.MountPoint();
                mountPoint.setSource(jsonObject.getString("Source"));
                mountPoint.setDestination(jsonObject.getString("Destination"));
                mountPointList.add(mountPoint);
            }
        }
        container.setMountPointList(mountPointList);

        // 设置映射端口
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

        JSONObject networks = configV2.getJSONObject("NetworkSettings").getJSONObject("Networks");
        Set<String> networksKeySet = networks.keySet();
        NetworkTypeEnum networkTypeEnum = NetworkTypeEnum.NONE;
        if (!networksKeySet.isEmpty()) {
            networkTypeEnum = "bridge".equals(networksKeySet.iterator().next()) ? NetworkTypeEnum.BRIDGE : NetworkTypeEnum.NET;
        }
        container.setNetworkType(networkTypeEnum.getValue());
        return container;
    }

    /**
     * 读取配置
     * @param path
     * @return com.alibaba.fastjson.JSONObject
     * @author yezhendong
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
     * @author yezhendong
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
