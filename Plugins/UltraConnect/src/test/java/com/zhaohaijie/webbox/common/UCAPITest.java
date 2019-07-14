package com.zhaohaijie.webbox.common;

import org.junit.Test;

public class UCAPITest {

    @Test
    public void testPostRelay() throws Exception {
        UCAPI ucapi = new UCAPI("624772977943", "25802580");

        System.out.println(ucapi.postRelay());
    }
}
