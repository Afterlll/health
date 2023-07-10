package com.itheima.health.utils;

import java.io.File;

public class PathUtils {
    public static String getRealPathWithResources(String path) {
        String filePath = null;
        String os = System.getProperty("os.name");
        System.out.println(os);
        if (os != null && os.toLowerCase().startsWith("windows")) {
            filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + path + File.separator;
        } else if (os != null && os.toLowerCase().startsWith("linux")) {
            filePath = PathUtils.class.getClassLoader().getResource("").getPath() + path + File.separator;
        }
        System.out.println(filePath);
        return filePath;
    }
}
