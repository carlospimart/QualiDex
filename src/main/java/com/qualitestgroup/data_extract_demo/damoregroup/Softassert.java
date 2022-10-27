package main.java.com.qualitestgroup.data_extract_demo.damoregroup;

import java.util.Map;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Maps;

public class Softassert extends SoftAssert {

	 public static final Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();
	 
	 @Override
	  protected void doAssert(IAssert<?> a){
	    onBeforeAssert(a);
	    try {
	      a.doAssert();
	      onAssertSuccess(a);
	    } catch (AssertionError ex) {	    
	    	onAssertFailure(a, ex);
	    	ITestResult result = Reporter.getCurrentTestResult();
	    	Reporter.log("Assertion Failed due to "+ex, true);
	    	new OnTestFailure().onTestFailure(result);
	    	result.setAttribute("softAssert", ex.getCause());
	    	
	    	m_errors.put(ex, a);
	    	
	    } finally {
	      onAfterAssert(a);
	    }
	  }
	 
	 
	 public void assertAll() {
		    if (!m_errors.isEmpty()) {
		    	 StringBuilder sb = null;
		    	if(m_errors.keySet().size()>1){
		    	sb = new StringBuilder("Test case failed due to Multiple Assertion failures");
		    	}else{
		    		sb = new StringBuilder("Test case failed due to "+m_errors.keySet().toString().replace("java.lang.AssertionError:", "").replace("[", "").split("expected")[0]);
		    	}
		     
		      boolean first = true;
		      for (Map.Entry<AssertionError, IAssert<?>> ae : m_errors.entrySet()) {
		        if (first) {
		          first = false;
		        } else {
		          sb.append(",");
		        }
		        sb.append("\n\t");
		        sb.append(ae.getKey().getMessage());
		      }
		      throw new AssertionError(sb.toString());
		    }
		  }


}
