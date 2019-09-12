package com.ucasp.enter.listener;

import com.ucasp.enter.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xdj
 * @version V1.0
 * @ProjectName: ucasp-enter-parse
 * @Description: 文件检查前置监听器
 * @Date 2019/9/10 10:08
 * Copyright (c)   xdj
 */
@Slf4j
@Component
public class FileCheckJobExecutionListener implements JobExecutionListener {

    private FileUtil fileUtil;
    @Value("${ftp.filePath.channel}")
    private String ftpPathChannel;

    @Value("${ftp.filePath.bank}")
    private String ftpPathBank;

    @Value("${ftp.fileTempOne}")
    private String fileTempOne;

    @Value("${ftp.filePath.localFilePath}")
    private String localFilePath;

    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.port}")
    private int port;
    @Value("${ftp.username}")
    private String username;
    @Value("${ftp.password}")
    private String password;

    @Override
    public void beforeJob(JobExecution jobExecution) {

        try {
            setFileUtil();
            List<String> names = this.fileUtil.listFTPFiles(this.fileTempOne);

            // 校验文件
            boolean checkFile = isCheckFile(names);
            if (!checkFile) {
                log.info("文件校验失败，退出当前批处理");
//                jobExecution.setStatus(BatchStatus.FAILED);
                jobExecution.stop();
            }

            // 存储文件到本地
            List<String> fileNames = this.fileUtil.splitFileName(names);
            downloadFileToLocal(fileNames);

        } catch (IOException e) {
            log.info("文件校验失败------->", e);
            e.printStackTrace();
        }
    }

    // 考虑是否维持连接到文件下载完成在断开
    private void downloadFileToLocal(List<String> names) {
        Set<String> fileNames = new HashSet<>(names);
        for (String name : fileNames) {
            String fileName = name + ".txt";
            try {
                this.fileUtil.downLoadFile(fileTempOne, localFilePath, fileName);
            } catch (IOException e) {
                log.info("下载文件失败文件名{}", fileName);
                throw new RuntimeException("File download failure");
            }
        }
    }

    private boolean isCheckFile(List<String> files) throws IOException {

        boolean checkFile;
        if (!files.isEmpty()) {
            checkFile = this.fileUtil.checkFile(files);
        } else {
            log.info("当前文件夹{}路径下没有文件", fileTempOne);
            throw new RuntimeException("要交验的文件路径出错，请检查后重新尝试！");
        }
        return checkFile;
    }

    private void setFileUtil() throws IOException {
        this.fileUtil = new FileUtil.Builder()
                .host(host)
                .port(port)
                .username(username)
                .password(password)
                .build();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // 批处理完成后删除临时文件
        try {
            List<String> names = this.fileUtil.listFTPFiles(this.fileTempOne);
            List<String> fileNames = this.fileUtil.splitFileName(names);
            Set<String> setNames = new HashSet<>(fileNames);
            for (String name : setNames) {
                File file = new File(localFilePath + File.separatorChar + name + ".txt");
                boolean delete = file.delete();
                if (!delete) {
                    log.info("删除临时文件失败文件名{}", file);
                }
                log.info("删除临时文件成功文件名{}", file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
