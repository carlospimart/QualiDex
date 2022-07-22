	package com.qualitestgroup.data_extract_demo.damoregroup;
	
	import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

				
public class OnTestFailure extends TestListenerAdapter {
	private static final Log logger = LogFactory.getLog(OnTestFailure.class);	
	
	@Override
	public void onTestFailure(ITestResult result) {
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		String methodName = result.getName();
		if(!result.isSuccess()){		
			try {
				String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()+"/target/surefire-reports/failure_screenshots";
				
				File destFile = new File(reportDirectory+"/failure_screenshots/"+methodName+"_"+formater.format(calendar.getTime())+".png");
				Reporter.log("<a href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");

			} catch (Exception e) {
				logger.info("Error" +e);
			}

		}			
	}
}
			
		
	
		