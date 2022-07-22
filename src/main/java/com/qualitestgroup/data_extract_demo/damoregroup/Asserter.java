package com.qualitestgroup.data_extract_demo.damoregroup;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.asserts.Assertion;

public class Asserter {

	public static final Softassert validateAssert = new Softassert();
	private static Assertion verifyAssert = new Assertion();
			
	public static void validateTrue(boolean val ){
		validateAssert.assertTrue(val);		
	}	
	public static  void validateTrue(boolean val, String str ){
		validateAssert.assertTrue(val, str);
	}				
	public static  void validateFalse(boolean val ){
		validateAssert.assertFalse(val);
	}
	public static void validateFalse(boolean val, String str ){
		validateAssert.assertFalse(val, str);
	}	
	public static void validateCondition(String actual, String expected ){
		validateAssert.assertEquals(actual, expected);
	}
	
	public static void validateContainsCondition(String actual, String expected ){
		
		if(actual.toLowerCase().contains(expected.toLowerCase()) || expected.toLowerCase().contains(actual.toLowerCase())){
			validateAssert.assertTrue(true);
		}else{
			validateAssert.assertEquals(actual, expected);
		}
		
	}
	
public static void validateContainsCondition(String actual, String expected ,String message){
		
		if(actual.toLowerCase().contains(expected.toLowerCase()) || expected.toLowerCase().contains(actual.toLowerCase())){
			validateAssert.assertTrue(true, message);
		}else{
			validateAssert.assertEquals(actual, expected, message);
		}
		
	}
	public static void validateCondition(Date actual, Date expected ){
		validateAssert.assertEquals(actual, expected);
	}
	public static void validateCondition(List<?> actual, List<?> expected ){
		validateAssert.assertEquals(actual, expected);
	}
	
	public static void validateCondition(List<?> actual, List<?> expected, String message ){
		validateAssert.assertEquals(actual, expected, message);
	}
	public static void validateCondition(String actual, String expected,String message ){
		validateAssert.assertEquals(actual, expected, message);
	}
	public static void validateCondition(int actual, int expected ){
		validateAssert.assertEquals(actual, expected);
	}	
	public static void validateCondition(int actual, int expected,String message ){
		validateAssert.assertEquals(actual, expected, message);
	}	
	public static void validateCondition(Map<String,String> actual, Map<String,String> expected,String message ){
		validateAssert.assertEquals(actual, expected, message);
	}	
	
	public static void verifyTrue(boolean val ){
		verifyAssert.assertTrue(val);
	}	
	public static  void verifyTrue(boolean val, String str ){
		verifyAssert.assertTrue(val, str);
	}		
	public static void verifyFail( ){
		verifyAssert.fail();
	}
	public static void verifyFail(String str ){
		verifyAssert.fail(str);
	}
	public static void verifyFail(String str, Exception e ){
		verifyFail("Message --> "+str+" Exception ---> " + e);
	}				
	public static  void verifyFalse(boolean val ){
		verifyAssert.assertFalse(val);
	}
	public static void verifyFalse(boolean val, String str ){
		verifyAssert.assertFalse(val, str);
	}	
	public static void verifyCondition(String actual, String expected ){
		verifyAssert.assertEquals(actual, expected);
	}	
	public static void verifyCondition(String actual, String expected,String message ){
		verifyAssert.assertEquals(actual, expected, message);
	}
	public static void verifyCondition(int actual, int expected ){
		verifyAssert.assertEquals(actual, expected);
	}
	public static void verifyCondition(int actual, int expected,String message ){
		verifyAssert.assertEquals(actual, expected, message);
	}
	
}

