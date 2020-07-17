package com.zhaoyh.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

/**
 * Created by zhaoyh on 2020/7/15
 *
 * 时间帮助
 *
 * @author zhaoyh
 */
@Slf4j
public class TimeUtils {

    public static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

}
