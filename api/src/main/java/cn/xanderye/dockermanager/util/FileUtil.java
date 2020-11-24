package cn.xanderye.dockermanager.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Created on 2020/9/1.
 *
 * @author XanderYe
 */
public class FileUtil {
    /**
     * 复制单文件
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @return void
     * @author XanderYe
     * @date 2020/9/1
     */
    public static void copyFile(String sourcePath, String targetPath) throws IOException {
        try (FileInputStream fis = new FileInputStream(sourcePath);
             FileOutputStream fos = new FileOutputStream(targetPath)) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = fis.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }
        }
    }

    /**
     * 复制文件夹
     * @param sourcePath 源文件夹路径
     * @param targetPath 目标文件夹路径
     * @return void
     * @author XanderYe
     * @date 2020/9/1
     */
    public static void copyDictionary(String sourcePath, String targetPath) throws IOException {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            throw new FileNotFoundException();
        }
        File targetFile = new File(targetPath);
        targetFile.mkdirs();
        File[] fileList = sourceFile.listFiles();
        if (fileList != null && fileList.length > 0) {
            for (File f : fileList) {
                if (f.isDirectory()) {
                    copyDictionary(f.getAbsolutePath(), targetPath + File.separator + f.getName());
                } else {
                    copyFile(f.getAbsolutePath(), targetPath + File.separator + f.getName());
                }
            }
        }
    }

    /**
     * 删除文件夹
     * @param filePath
     * @return void
     * @author XanderYe
     * @date 2020/9/1
     */
    public static void deleteDictionary(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(FileUtil::deleteFile);
        }
    }

    /**
     * 文件夹是否为空
     * @param files
     * @return boolean
     * @author XanderYe
     * @date 2020/11/24
     */
    public static boolean isNotEmpty(File[] files) {
        return null != files && files.length > 0;
    }

    /**
     * 删除文件
     * @param filePath
     * @return void
     * @author XanderYe
     * @date 2020/9/1
     */
    private static void deleteFile(Path filePath) {
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
