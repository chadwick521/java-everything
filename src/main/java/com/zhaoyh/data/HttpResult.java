package com.zhaoyh.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Created by zhaoyh on 2020/7/15
 *
 * @author zhaoyh
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResult implements Serializable {

    private int httpCode;

    private String result;

}
