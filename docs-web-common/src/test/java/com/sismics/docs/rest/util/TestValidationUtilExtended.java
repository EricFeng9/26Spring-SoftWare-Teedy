package com.sismics.docs.rest.util;

import com.sismics.rest.exception.ClientException;
import com.sismics.rest.util.ValidationUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * ##Practice8 : 扩展 ValidationUtil 的测试，专门覆盖长度校验、默认分支和异常分支，提升 Practice 8 覆盖率。
 */
public class TestValidationUtilExtended {

    @Test
    public void testValidateLengthSuccessAndNullableBranches() throws Exception {
        // ##Practice8 : 覆盖 strip 后成功返回，以及 nullable 允许空值的分支。
        Assert.assertEquals("abc", ValidationUtil.validateLength("  abc  ", "name", 1, 5, false));
        Assert.assertNull(ValidationUtil.validateLength(null, "name", 1, 5, true));
        Assert.assertEquals("", ValidationUtil.validateLength("   ", "name", 1, 5, true));
        Assert.assertEquals("value", ValidationUtil.validateStringNotBlank("  value ", "name"));
    }

    @Test
    public void testValidateLengthFailureBranches() throws Exception {
        // ##Practice8 : 覆盖必填、过短和过长分支。
        assertClientException(() -> ValidationUtil.validateLength(null, "name", 1, 5, false));
        assertClientException(() -> ValidationUtil.validateLength("a", "name", 2, 5, false));
        assertClientException(() -> ValidationUtil.validateLength("abcdef", "name", 1, 5, false));
    }

    @Test
    public void testValidateTagNameBranches() throws Exception {
        // ##Practice8 : 覆盖合法 tag、空格非法、冒号非法分支。
        ValidationUtil.validateTagName("valid_tag");
        assertClientException(() -> ValidationUtil.validateTagName("invalid tag"));
        assertClientException(() -> ValidationUtil.validateTagName("invalid:tag"));
    }

    @Test
    public void testValidateAlphaNumericAndUsernameBranches() throws Exception {
        // ##Practice8 : 覆盖字母数字校验和用户名校验的成功/失败路径。
        ValidationUtil.validateAlphanumeric("User_123", "username");
        ValidationUtil.validateUsername("user.name-test@example", "username");
        assertClientException(() -> ValidationUtil.validateAlphanumeric("bad-value!", "username"));
        assertClientException(() -> ValidationUtil.validateUsername("bad value!", "username"));
    }

    @Test
    public void testValidateIntegerAndLongBranches() throws Exception {
        // ##Practice8 : 覆盖数字解析成功与失败分支。
        Assert.assertEquals(Integer.valueOf(42), ValidationUtil.validateInteger("42", "count"));
        Assert.assertEquals(Long.valueOf(123456789L), ValidationUtil.validateLong("123456789", "count"));
        assertClientException(() -> ValidationUtil.validateInteger("NaN", "count"));
        assertClientException(() -> ValidationUtil.validateLong("not-a-long", "count"));
    }

    @Test
    public void testValidateDateBranches() throws Exception {
        // ##Practice8 : 覆盖日期解析、nullable 返回 null、以及非法日期分支。
        Date date = ValidationUtil.validateDate("1700000000000", "date", false);
        Assert.assertNotNull(date);
        Assert.assertNull(ValidationUtil.validateDate(null, "date", true));
        assertClientException(() -> ValidationUtil.validateDate(null, "date", false));
        assertClientException(() -> ValidationUtil.validateDate("invalid-date", "date", false));
    }

    private void assertClientException(ThrowingRunnable runnable) throws Exception {
        try {
            runnable.run();
            Assert.fail("Expected ClientException");
        } catch (ClientException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
