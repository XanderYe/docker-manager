package cn.xanderye.dockermanager.util;

import cn.xanderye.dockermanager.constant.Constant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * Created on 2020/11/24.
 *
 * @author XanderYe
 */
public class DockerUtil {

    public static boolean systemd = true;

    public static String containerPath = "/var/lib/docker/containers";

    private final static String CONFIG_V2_FILE = "config.v2.json";

    private final static String HOST_CONFIG_FILE = "hostconfig.json";

    /**
     * 检查docker配置
     * @return boolean
     * @author XanderYe
     * @date 2020/11/24
     */
    public static boolean checkDocker() {
        try {
            // 判断系统是否支持systemctl命令(已知wsl2有问题)
            String service = SystemUtil.execStr("systemctl status docker");
            systemd = !service.contains(Constant.SYSTEMD_ERROR);

            // 获取docker配置路径
            String res = SystemUtil.execStr("docker info");
            if (res.contains("command not found")) {
                return false;
            } else {
                String dockerRootDir = StringUtils.substringBetween(res, "Docker Root Dir:", "Debug").trim();
                containerPath = dockerRootDir + File.separator + "containers";
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取容器目录
     * @param
     * @return java.io.File[]
     * @author XanderYe
     * @date 2020/11/25
     */
    public static File[] getContainerFiles() {
        File containerFile = new File(containerPath);
        return containerFile.listFiles();
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

    /**
     * 根据id读取config.v2.json
     * @param id
     * @return com.alibaba.fastjson.JSONObject
     * @author XanderYe
     * @date 2020/11/25
     */
    public static JSONObject readConfigV2(String id) {
        String configV2Path = containerPath + File.separator + id + File.separator + CONFIG_V2_FILE;
        return readConfig(configV2Path);
    }

    /**
     * 根据id读取hostconfig.json
     * @param id
     * @return com.alibaba.fastjson.JSONObject
     * @author XanderYe
     * @date 2020/11/25
     */
    public static JSONObject readHostConfig(String id) {
        String hostConfigPath = containerPath + File.separator + id + File.separator + HOST_CONFIG_FILE;
        return readConfig(hostConfigPath);
    }

    /**
     * 写入config.v2.json
     * @param id
     * @param configV2
     * @return boolean
     * @author XanderYe
     * @date 2020/11/25
     */
    public static boolean writeConfigV2(String id, JSONObject configV2) {
        String configV2Path = containerPath + File.separator + id + File.separator + CONFIG_V2_FILE;
        try {
            FileUtil.copyFile(configV2Path, configV2Path + ".bak");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writeConfig(configV2, configV2Path);
    }

    /**
     * 写入hostconfig.json
     * @param id
     * @param hostConfig
     * @return boolean
     * @author XanderYe
     * @date 2020/11/25
     */
    public static boolean writeHostConfig(String id, JSONObject hostConfig) {
        String hostConfigPath = containerPath + File.separator + id + File.separator + HOST_CONFIG_FILE;
        try {
            FileUtil.copyFile(hostConfigPath, hostConfigPath + ".bak");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writeConfig(hostConfig, hostConfigPath);
    }
}
