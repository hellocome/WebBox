package com.zhaohaijie.webbox.common.http.auth;

import java.util.Base64;

public class UsernamePasswordHttpCred implements HttpCred {
    private final String username;
    private final String password;
    private final String credString;

    public UsernamePasswordHttpCred(String username, String password) {
        this.username = username;
        this.password = password;

        this.credString = "Basic " + new String(Base64.getEncoder().encode((username + ':' + password).getBytes()));
    }

    @Override
    public String getCredString() {
        return this.credString;
    }
}
