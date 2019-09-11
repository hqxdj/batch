package com.ucasp.enter.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: ucasp-enter-parse
 * @Description:
 * @Date 2019/9/9 15:09
 * Copyright (c)   xdj
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUtilTest {

    private String hostName = "192.168.1.79";
    private int port = 21;
    private String userName = "ftpuser";
    private String password = "ftpuser";
    private String ftpPath = "/pub/channel";
    private String localPath = "D:/train";


//    @Test
//    public void downLoadFile() throws IOException {
//
//        FileUtil.downLoadFile(hostName,port,userName,password,ftpPath,localPath,"train.txt");
//    }

    @Test
    public void testListFTPFile() throws IOException {
        FileUtil fileUtil = new FileUtil.Builder()
                .host(hostName)
                .port(port)
                .username(userName)
                .password(password)
                .build();
        List<String> files = fileUtil.listFTPFiles(ftpPath);

        System.out.println(files);


    }
}