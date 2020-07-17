package com.zhaoyh.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

/**
 * Created by zhaoyh on 2020/7/16
 *
 * 文件帮助类
 *
 * @author zhaoyh
 */
@Slf4j
public class FileUtils {

    /**
     * 按行读文件内容
     *
     * @param fileFullPath
     */
    public static List<String> readFileContent(String fileFullPath) {
        Path path = Paths.get(fileFullPath);
        List<String> contentList = null;
        try {
            contentList = Files.readAllLines(path);
        } catch (IOException e) {
            log.error("readFileContent: {} failed!", fileFullPath, e);
        }
        return contentList;
    }

    /**
     * 读取resource下的配置文件信息
     *
     * @param resourceConfigPath config/config.json
     * @return
     */
    public static JSONObject readJsonFromResource(String resourceConfigPath) {
        InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(resourceConfigPath);
        try {
            if (Objects.nonNull(inputStream)) {
                return JSON.parseObject(inputStream, StandardCharsets.UTF_8, null);
            }
        } catch (IOException e) {
            log.error("readJsonFromResource: {} failed!", resourceConfigPath, e);
        }
        return null;
    }

    /**
     * 按行读配置文件内容
     *
     * @param resourceConfigPath
     */
    public static List<String> readContentFromResource(String resourceConfigPath) {
        InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(resourceConfigPath);
        try {
            if (Objects.nonNull(inputStream)) {
                return IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("readContentFromResource: {} failed!", resourceConfigPath, e);
        }
        return null;
    }

    /**
     * 写文件
     *
     * @param stringList
     * @param targetFilePath
     */
    private static void writeListToFile(List<String> stringList, String targetFilePath) {
        Path path = Paths.get(targetFilePath);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.write(path, stringList, StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("writeListToFile: {} failed!", targetFilePath, e);
        }
    }

    /**
     * 写文件
     *
     * @param content
     * @param targetFilePath
     */
    public static void writeStringToFile(String content, String targetFilePath) {
        try {
            Path path = Paths.get(targetFilePath);
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("writeStringToFile: {} failed!", targetFilePath, e);
        }
    }

}
