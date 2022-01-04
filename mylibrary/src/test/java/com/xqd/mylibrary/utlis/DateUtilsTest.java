package com.xqd.mylibrary.utlis;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by 谢邱东 on 2022/1/4 16:09.
 * NO bug
 */
public class DateUtilsTest {

    @Test
    public void getTime() {
        assertEquals("2021-01-20 07:54:43",DateUtils.getTime(1611100483000l));
    }
}