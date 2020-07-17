package com.zhaoyh.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhaoyh.data.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhaoyh on 2020/7/15
 *
 * @author zhaoyh
 */
@Slf4j
public class HttpClientUtils {

    /**
     * CONNECT_TIMEOUT 设置连接超时时间，单位毫秒。
     */
    private static final int CONNECT_TIMEOUT = 8000;

    /**
     * SOCKET_TIMEOUT 请求获取数据的超时时间(即响应时间)，单位毫秒。
     */
    private static final int SOCKET_TIMEOUT = 10000;

    /**
     * 发起get请求
     * 默认什么都不带
     *
     * @param url
     * @return
     */
    public static HttpResult doHttpGetRequest(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
            String result = "";
            if (null != httpResponse.getEntity()) {
                result = EntityUtils.toString(httpResponse.getEntity());
            }
            return new HttpResult(httpStatusCode, result);
        } catch (IOException e) {
            log.error("doHttpGetRequest: {} failed!", url, e);
        } finally {
            try {
                if (null != httpResponse) {
                    httpResponse.close();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.error("close httpResource failed!", e);
            }
        }
        return new HttpResult();
    }

    /**
     * 发起get请求
     * 带请求头和请求参数
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static HttpResult doHttpGetRequest(String url, Map<String, String> headers, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            // 叠加参数
            if (null != params && params.size() > 0) {
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = uriBuilder.build();
            HttpGet httpGet = new HttpGet(uri);
            // 设置请求参数
            httpGet.setConfig(RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build());
            // 设置请求头
            if (null != headers && headers.size() > 0) {
                packageHeader(headers, httpGet);
            }
            // 返回体
            httpResponse = httpClient.execute(httpGet);
            if (null != httpResponse) {
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                String result = "";
                if (null != httpResponse.getEntity()) {
                    result = EntityUtils.toString(httpResponse.getEntity());
                }
                return new HttpResult(statusCode, result);
            }
        } catch (URISyntaxException e) {
            log.error("URIBuilder: {} failed!", url, e);
        } catch (IOException e) {
            log.error("httpGetExecute: {} failed!", url, e);
        } finally {
            try {
                if (null != httpResponse) {
                    httpResponse.close();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.error("close httpResource failed!", e);
            }
        }
        return new HttpResult();
    }

    /**
     * 封装请求头
     *
     * @param params
     * @param httpMethod
     */
    private static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (null != params && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 发起post请求
     * 带请求头和请求参数
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static HttpResult doHttpPostRequest(String url, Map<String, String> headers, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            // 创建http对象
            HttpPost httpPost = new HttpPost(url);
            // 设置请求参数
            httpPost.setConfig(RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build());
            // 设置请求头
            if (null != headers && headers.size() > 0) {
                packageHeader(headers, httpPost);
            }
            // 封装表单参数
            if (null != params && params.size() > 0) {
                packagePostFormParam(params, httpPost);
            }
            httpResponse = httpClient.execute(httpPost);
            if (null != httpResponse) {
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                String result = "";
                if (null != httpResponse.getEntity()) {
                    result = EntityUtils.toString(httpResponse.getEntity());
                }
                return new HttpResult(statusCode, result);
            }
        } catch (IOException e) {
            log.error("httpPostExecute: {} failed!", url, e);
        } finally {
            try {
                if (null != httpResponse) {
                    httpResponse.close();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.error("close httpResource failed!", e);
            }
        }
        return new HttpResult();
    }

    /**
     * 封装post表单请求参数
     *
     * @param params
     * @param httpMethod
     */
    private static void packagePostFormParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        // 设置到请求的http对象中
        httpMethod.setEntity(new UrlEncodedFormEntity(nameValuePairList, StandardCharsets.UTF_8));
    }

    /**
     * 发起post请求
     * 带请求头
     * 请求参数为json
     *
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static HttpResult doHttpPostRequest(String url, Map<String, String> headers, JSONObject json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            // 创建http对象
            HttpPost httpPost = new HttpPost(url);
            // 设置请求参数
            httpPost.setConfig(RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build());
            // 设置请求头
            if (null != headers && headers.size() > 0) {
                packageHeader(headers, httpPost);
            }
            // 封装json参数
            StringEntity entity = new StringEntity(json.toJSONString(), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
            if (null != httpResponse) {
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                String result = "";
                if (null != httpResponse.getEntity()) {
                    result = EntityUtils.toString(httpResponse.getEntity());
                }
                return new HttpResult(statusCode, result);
            }
        } catch (IOException e) {
            log.error("httpPostExecute: {} failed!", url, e);
        } finally {
            try {
                if (null != httpResponse) {
                    httpResponse.close();
                }
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.error("close httpResource failed!", e);
            }
        }
        return new HttpResult();
    }
}
