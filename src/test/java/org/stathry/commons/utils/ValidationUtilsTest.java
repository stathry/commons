/*
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.commons.utils;

import org.junit.Test;
import org.stathry.commons.convert.Bean3;
import org.stathry.commons.model.dto.ValidateResult;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author stathry@126.com
 * @date 2017年5月21日
 */
public class ValidationUtilsTest {

    @Test
    public void test1() {
        Bean3 b = new Bean3();
        b.setBirth("2017-05-21 18:38:00");
        ValidateResult r = ValidationUtils.validate(b);
        System.out.println(r);
        assertNotNull(r);
        assertTrue(r.isSuccess());
    }

    @Test
    public void test2() {
        Bean3 b = new Bean3();
        ValidateResult r = ValidationUtils.validate(b);
        System.out.println(r);
        assertNotNull(r);
        assertTrue(r.isSuccess());
    }

    @Test
    public void test3() {
        Bean3 b = new Bean3();
        b.setBirth("2017-05-41 18:38:00");
        ValidateResult r = ValidationUtils.validate(b);
        System.out.println(r);
        assertNotNull(r);
        assertFalse(r.isSuccess());
    }

    @Test
    public void test4() {
        Bean3 b = new Bean3();
        b.setBirth("2017");
        ValidateResult r = ValidationUtils.validate(b);
        System.out.println(r);
        assertNotNull(r);
        assertFalse(r.isSuccess());
        System.out.println("getFailedItems:" + r.getFailedItems());
    }

    @Test
    public void test5() {
        Bean3 b = new Bean3();
        b.setBirth("2017-05-41 18:38:00");
        ValidateResult r = ValidationUtils.validate(b, true);
        System.out.println(r);
        assertNotNull(r);
        assertFalse(r.isSuccess());
        System.out.println("getFailedDetailItems:" + r.getFailedDetailItems());
    }

}
