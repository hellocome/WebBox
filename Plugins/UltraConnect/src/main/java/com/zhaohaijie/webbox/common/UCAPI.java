package com.zhaohaijie.webbox.common;

import com.sun.tools.corba.se.idl.InvalidArgument;
import com.zhaohaijie.webbox.common.http.SimpleHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class UCAPI {
    private static Logger logger = LogManager.getLogger();
    private static String RELAY_URL = "/xquery/1.0/relay.json";

    private final String serialNumber;
    private final String appVersionName;
    private final String appType;
    private final String passcode;
    private final String languageTag;
    private final String primaryServerUrl;
    private final GlobalData globalData;

    public UCAPI(String serialNumber, String passcode) {
        this.serialNumber = serialNumber;
        this.passcode = passcode;

        this.globalData = GlobalData.getGlobalDataInstance();
        this.appVersionName = this.globalData.getAppVersionName();
        this.appType = this.globalData.getAppType();
        this.languageTag = this.globalData.toLanguageTag();
        this.primaryServerUrl = this.globalData.getPrimaryServerUrl();
    }

    public String postRelay() throws Exception {
        String requestUrl = this.primaryServerUrl + RELAY_URL;
        String dateString = this.globalData.getDateString();
        String signature = globalData.getSignature(dateString, RELAY_URL, this.serialNumber);

        if (StringUtils.isEmpty(signature)) {
            throw new InvalidArgument("serial number can't empty");
        }

        URI uri = new URIBuilder(requestUrl)
                .addParameter("lang", this.languageTag)
                .build();

        HttpPost httpPost = new HttpPost(uri);

        final List<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("apptype", this.appType));
        postParameters.add(new BasicNameValuePair("appver", this.appVersionName));
        postParameters.add(new BasicNameValuePair("id", this.serialNumber));
        postParameters.add(new BasicNameValuePair("passcode", this.passcode));
        postParameters.add(new BasicNameValuePair("signature", signature));
        postParameters.add(new BasicNameValuePair("lang", this.languageTag));
        postParameters.add(new BasicNameValuePair("date", dateString));

        httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

        return SimpleHttpClient.postWithResponseAsString(httpPost);
    }

}