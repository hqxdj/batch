package com.ucasp.enter.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: ucasp-enter-parse
 * @Description: 文件工具类
 * @Date 2019/9/9 14:19
 * Copyright (c)   xdj
 */
@Slf4j
public class FileUtil {

    private String host;

    private int port;

    private String username;

    private String password;

    private int bufferSize = 10 * 1024 * 1024;

    private int soTimeout = 15000;

    private FTPClient ftp;

    private FileUtil(String host, int port, String username, String password, int bufferSize,
                     FTPClientConfig config, int defaultTimeout, int dataTimeout, int connectTimeout,
                     int controlKeepAliveTimeout, int soTimeout) throws IOException {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.bufferSize = bufferSize;
        this.soTimeout = soTimeout;
        this.ftp = new FTPClient();
        if (config != null) {
            this.ftp.configure(config);
        }
        ftp.setControlEncoding("UTF-8");
//        ftp.setControlEncoding("GBK");
//        ftp.setControlEncoding("gb2312");
        ftp.enterLocalPassiveMode();
        ftp.setDefaultTimeout(defaultTimeout);
        ftp.setConnectTimeout(connectTimeout);
        ftp.setDataTimeout(dataTimeout);
        // ftp.setSendDataSocketBufferSize(1024 * 256);
        if (this.bufferSize > 0) {
            ftp.setBufferSize(this.bufferSize);
        }

        // keeping the control connection alive
        ftp.setControlKeepAliveTimeout(controlKeepAliveTimeout);// 每大约5分钟发一次noop，防止大文件传输导致的控制连接中断
    }

    public FTPClient getFtp() {
        return ftp;
    }

    public void setFtp(FTPClient ftp) {
        this.ftp = ftp;
    }

    // 连接
    public FileUtil connect() throws IOException {
        if (!this.ftp.isConnected()) {
            this.ftp.connect(this.host, this.port);
            int replyCode = this.ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.info("ftp服务器返回码 {}", replyCode);
                throw new IllegalStateException("连接ftp服务器失败，返回码" + replyCode);
            }
        }
        this.ftp.setSoTimeout(this.soTimeout);
        return this;
    }

    // 登录
    public FileUtil login() throws IOException {
        boolean suc = this.ftp.login(this.username, this.password);
        if (!suc) {
            throw new IllegalStateException("登录ftp服务器失败");
        }
        return this;
    }

    public FileUtil logout() throws IOException {
        if (this.ftp != null) {
            this.ftp.logout();
        }
        return this;
    }

    public void disConnect() throws IOException {
        if (this.ftp.isConnected()) {
            this.ftp.disconnect();
        }
    }

    // 列举出当前目录的文件列表
    public List<String> listFTPFiles(String filePath) throws IOException {

        FileUtil fileUtil = this.connect().login();
        FTPClient ftp = fileUtil.getFtp();
        ftp.enterLocalPassiveMode();
        boolean directory = ftp.changeWorkingDirectory(filePath);
        if (!directory) {
            log.info("修改目录失败，没有权限");
            throw new RuntimeException("change directory failure");
        }
        String[] listNames = ftp.listNames();
        this.logout().disConnect();
        return Arrays.asList(listNames);
    }

    // 循环比对文件是否都上传完成
    public boolean checkFile(List<String> list) {

        List<String> names = splitFileName(list);

        List<String> result = names.stream()
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!result.isEmpty()) {
            log.info("存在未上传完成的文件{}", result);
            return false;
        }
        return true;
    }

    public List<String> splitFileName(List<String> list) {
        return list.stream()
                .map((x) -> {
                    String[] split = x.split("\\.");
                    return split[0];
                })
                .collect(Collectors.toList());
    }

    // 文件下载
    public void downLoadFile(String ftpPath, String localPath, String fileName) throws IOException {
        FileUtil fileUtil = this.connect().login();
        fileUtil.ftp.enterLocalPassiveMode();
        fileUtil.ftp.setControlEncoding("UTF-8");
        fileUtil.ftp.setFileType(FTP.BINARY_FILE_TYPE);
        fileUtil.ftp.changeWorkingDirectory(ftpPath);

        File localFile = new File(localPath + File.separatorChar + fileName);
        OutputStream os = new FileOutputStream(localFile);
        fileUtil.ftp.retrieveFile(fileName, os);
        os.close();
        fileUtil.logout().disConnect();
    }

    public static class Builder {

        private String host;
        private int port = 21;
        private String username;
        private String password;
        private int bufferSize = 1024 * 1024;
        private FTPClientConfig config;
        private int defaultTimeout = 15000;
        private int connectTimeout = 15000;
        private int dataTimeout = 15000;
        private int controlKeepAliveTimeout = 300;
        private int soTimeout = 15000;

        public Builder() {
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder bufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }

        public Builder config(FTPClientConfig config) {
            this.config = config;
            return this;
        }

        public Builder defaultTimeout(int defaultTimeout) {
            this.defaultTimeout = defaultTimeout;
            return this;
        }

        public Builder connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder dataTimeout(int dataTimeout) {
            this.dataTimeout = dataTimeout;
            return this;
        }

        public Builder soTimeout(int soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public Builder controlKeepAliveTimeout(int controlKeepAliveTimeout) {
            this.controlKeepAliveTimeout = controlKeepAliveTimeout;
            return this;
        }

        public FileUtil build() throws IOException {
            FileUtil instance = new FileUtil(this.host, this.port, this.username, this.password,
                    this.bufferSize, this.config, this.defaultTimeout, this.dataTimeout, this.connectTimeout,
                    this.controlKeepAliveTimeout, this.soTimeout);
            return instance;
        }
    }


}
