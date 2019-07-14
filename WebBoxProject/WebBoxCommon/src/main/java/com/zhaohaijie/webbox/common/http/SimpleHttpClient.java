package com.zhaohaijie.webbox.common.http;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.InputStream;

public class SimpleHttpClient {
    public static InputStream execute(HttpRequestBase request) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return httpClient.execute(request).getEntity().getContent();
    }

    public static String executeWithResponseAsString(HttpRequestBase request) throws Exception {
        return IOUtils.toString(execute(request));
    }

    public static String postWithResponseAsString(HttpPost request) throws Exception {
        return executeWithResponseAsString(request);
    }

    public static String getWithResponseAsString(HttpGet request) throws Exception {
        return executeWithResponseAsString(request);
    }
}
