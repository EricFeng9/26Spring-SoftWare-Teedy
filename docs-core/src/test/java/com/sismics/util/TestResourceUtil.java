package com.sismics.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test of the resource utils.
 *
 * @author jtremeaux 
 */
public class TestResourceUtil {

    @Test
    public void listFilesTest() throws Exception {
        List<String> fileList = ResourceUtil.list(Test.class, "/junit/framework");
        Assert.assertTrue(fileList.contains("Test.class"));

        fileList = ResourceUtil.list(Test.class, "/junit/framework/");
        Assert.assertTrue(fileList.contains("Test.class"));

        fileList = ResourceUtil.list(Test.class, "junit/framework/");
        Assert.assertTrue(fileList.contains("Test.class"));

        fileList = ResourceUtil.list(Test.class, "junit/framework/");
        Assert.assertTrue(fileList.contains("Test.class"));
    }

    @Test
    public void listFilesWithFilterTest() throws Exception {
        // ##Practice8 : 覆盖 ResourceUtil 里 filter.accept 分支，验证过滤器只保留目标资源。
        List<String> fileList = ResourceUtil.list(Test.class, "/junit/framework", (dir, name) -> name.endsWith("Test.class"));
        Assert.assertTrue(fileList.contains("Test.class"));
        Assert.assertFalse(fileList.contains("Assert.class"));
    }
}
