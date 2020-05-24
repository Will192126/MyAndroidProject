package com.will.lib_network.okhttp.request;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * get/post/文件上传
 */
public class CommonRequest {
    /**
     * 创建POST请求对象
     * @param url
     * @param params
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null);
    }

    /**
     * 创建POST请求对象
     * @param url
     * @param params
     * @param header
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params, RequestParams header) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()){
                bodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Headers.Builder headerBuilder = new Headers.Builder();
        if (header != null) {
            for (Map.Entry<String, String> entry : header.urlParams.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .headers(headerBuilder.build())
                .post(bodyBuilder.build())
                .build();
        return request;
    }

    /**
     * 创建GET请求对象
     * @param url
     * @param params
     * @returndou320
     */
    public static Request createGetRequest(String url, RequestParams params) {
        return createGetRequest(url, params, null);
    }

    /**
     * 创建GET请求对象
     * @param url
     * @param params
     * @param header
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params, RequestParams header) {
        StringBuilder builder = new StringBuilder(url).append("?");
        for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        Headers.Builder headerBuilder = new Headers.Builder();
        if (header != null) {
            for (Map.Entry<String, String> entry : header.urlParams.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(builder.toString())
                .headers(headerBuilder.build())
                .get()
                .build();
        return request;
    }

    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    /**
     * 创建文件上传请求
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createMultiPostRequest(String url, RequestParams params) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(FILE_TYPE, (File) entry.getValue()));
                } else if (entry.getValue() instanceof String) {
                    builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null, (String) entry.getValue()));
                }
            }
        }
        return new Request.Builder().url(url).post(builder.build()).build();
    }
}
