package com.zhaohaijie.webbox.common;

import org.junit.Test;

public class UCAPITest {

    @Test
    public void testPostRelay() throws Exception {
        UCAPI ucapi = new UCAPI("65464654654", "656465465");

        System.out.println(ucapi.postRelay());
    }
}
