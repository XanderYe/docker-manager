package cn.xanderye.dockermanager.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020/11/26.
 *
 * @author 叶振东
 */
public class Constant {

    public final static String CONTAINER_ERROR = "Error";

    public final static List<String> IGNORED_URI_LIST = new ArrayList<String>(){{
        add("/container/getContainerStatusList");
    }};
}
