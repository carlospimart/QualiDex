package main.java.com.qualitestgroup.data_extract_demo.damoregroup;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.asserts.Assertion;

public class Asserter {

    public Softassert validateAssert = new Softassert();

    private Assertion verifyAssert = new Assertion();


    //Constructor
    public Asserter() {
    };


    public void validateTrue(boolean condition) {

        validateAssert.assertTrue(condition);
    }

    public void validateTrue(boolean condition, String message) {
        validateAssert.assertTrue(condition, message);
    }

    public void validateFalse(boolean condition) {
        validateAssert.assertFalse(condition);
    }

    public void validateFalse(boolean condition, String message) {
        validateAssert.assertFalse(condition, message);
    }

    public boolean validateCondition(String actual, String expected) {
        return actual.toLowerCase().equals(expected.toLowerCase());
    }

    public void validateContainsCondition(String actual, String expected) {

        if (actual.toLowerCase().contains(expected.toLowerCase()) || expected.toLowerCase().contains(actual.toLowerCase())) {
            validateAssert.assertTrue(true);
        } else {
            validateAssert.assertEquals(actual, expected);
        }
    };

    public void validateContainsCondition(String actual, String expected, String message) {

        if (actual.toLowerCase().contains(expected.toLowerCase()) || expected.toLowerCase().contains(actual.toLowerCase())) {
            validateAssert.assertTrue(true, message);
        } else {
            validateAssert.assertEquals(actual, expected, message);
        }

    }

    public void validateCondition(Date actual, Date expected) {
        validateAssert.assertEquals(actual, expected);
    }

    public void validateCondition(List<?> actual, List<?> expected) {
        validateAssert.assertEquals(actual, expected);
    }

    public void validateCondition(List<?> actual, List<?> expected, String message) {
        validateAssert.assertEquals(actual, expected, message);
    }

    public void validateCondition(String actual, String expected, String message) {
        validateAssert.assertEquals(actual, expected, message);
    }

    public void validateCondition(int actual, int expected) {
        validateAssert.assertEquals(actual, expected);
    }

    public void validateCondition(int actual, int expected, String message) {
        validateAssert.assertEquals(actual, expected, message);
    }

    public void validateCondition(Map<String, String> actual, Map<String, String> expected, String message) {
        validateAssert.assertEquals(actual, expected, message);
    }

    public void verifyTrue(boolean val) {
        verifyAssert.assertTrue(val);
    }

    public void verifyTrue(boolean val, String str) {
        verifyAssert.assertTrue(val, str);
    }

    public void verifyFail() {
        verifyAssert.fail();
    }

    public void verifyFail(String str) {
        verifyAssert.fail(str);
    }

    public void verifyFail(String str, Exception e) {
        verifyFail("Message --> " + str + " Exception ---> " + e);
    }

    public void verifyFalse(boolean val) {
        verifyAssert.assertFalse(val);
    }

    public void verifyFalse(boolean val, String str) {
        verifyAssert.assertFalse(val, str);
    }

    public void verifyCondition(String actual, String expected) {
        verifyAssert.assertEquals(actual, expected);
    }

    public void verifyCondition(String actual, String expected, String message) {
        verifyAssert.assertEquals(actual, expected, message);
    }

    public void verifyCondition(int actual, int expected) {
        verifyAssert.assertEquals(actual, expected);
    }

    public void verifyCondition(int actual, int expected, String message) {
        verifyAssert.assertEquals(actual, expected, message);
    }

}

