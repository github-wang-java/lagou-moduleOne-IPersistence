package com.lagou.io;

import java.io.InputStream;

public class Resources {

    /**
     * 根据路径将配置文件加载为字节输入流，并存到内存中
     *
     * Resources.class.getClassLoader():获取类加载器
     * @param path
     * @return
     */
    public static InputStream getResourceAsSteam(String path){
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }

}
