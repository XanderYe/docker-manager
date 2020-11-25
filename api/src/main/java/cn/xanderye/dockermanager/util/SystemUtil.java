package cn.xanderye.dockermanager.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created on 2020/11/5.
 *
 * @author XanderYe
 */
public class SystemUtil {

    /**
     * 空格
     */
    public static final String BREAK = " ";
    /**
     * Tab
     */
    public static final String TAB = "    ";
    /**
     * Windows换行符
     */
    public static final String WINDOWS_LINE_BREAK = "\r\n";
    /**
     * UNIX换行符
     */
    public static final String UNIX_LINE_BREAK = "\r";

    /**
     * 调用cmd方法，默认GBK编码
     * @param cmdStr
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/5
     */
    public static String execStr(String cmdStr) {
        return execStr(getCharset(), cmdStr);
    }

    /**
     * 调用cmd方法
     * @param charset
     * @param cmds
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/5
     */
    public static String execStr(Charset charset, String...cmds) {
        if (1 == cmds.length) {
            if (cmds[0] == null || "".equals(cmds[0])) {
                throw new NullPointerException("Empty command !");
            }
            cmds = cmds[0].split(BREAK);
        }
        Process process = null;
        try {
            process = new ProcessBuilder(cmds).redirectErrorStream(true).start();
            InputStream is = process.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(is, charset));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = buffer.readLine()) != null) {
                sb.append(line).append(getLineBreak());
            }
            is.close();
            return sb.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != process) {
                process.destroy();
            }
        }
        return null;
    }

    /**
     * 判断系统环境
     * @param
     * @return boolean
     * @author XanderYe
     * @date 2020/11/5
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 获取系统字符编码
     * @param
     * @return java.nio.charset.Charset
     * @author XanderYe
     * @date 2020/11/5
     */
    public static Charset getCharset() {
        return isWindows() ? Charset.forName("GBK") : Charset.defaultCharset();
    }

    /**
     * 获取系统换行符
     * @param
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/5
     */
    public static String getLineBreak() {
        return isWindows() ? WINDOWS_LINE_BREAK : UNIX_LINE_BREAK;
    }
}
