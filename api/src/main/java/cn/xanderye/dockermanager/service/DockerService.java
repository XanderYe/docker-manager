package cn.xanderye.dockermanager.service;

/**
 * Created on 2020/11/25.
 *
 * @author XanderYe
 */
public interface DockerService {

    /**
     * 启动docker
     * @param
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/25
     */
    String startDocker();

    /**
     * 停止docker
     * @param
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/25
     */
    String stopDocker();

    /**
     * 重启docker
     * @param
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/25
     */
    String restartDocker();
}
