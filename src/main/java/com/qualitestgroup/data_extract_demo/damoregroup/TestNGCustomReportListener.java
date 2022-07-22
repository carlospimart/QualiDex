package com.qualitestgroup.data_extract_demo.damoregroup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.IInvokedMethod;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

public class TestNGCustomReportListener implements IReporter {

	private static final Log logger = LogFactory.getLog(TestNGCustomReportListener.class);
	private PrintWriter writer;
	private int mrow;
	private static Integer msummethodIndex = 0;
	private static Integer mdetmethodIndex = 0;
	private String reportTitle = "Qualidex Report";
	private String automationSuite = "Automation Suite";
	private static int msuitetestIndex = 0;
	private static int mdettestIndex = 0;
	private static String timeStamp = new SimpleDateFormat(
			"yyyy" + File.separator + "MMM" + File.separator + "dd hh_mm_aa").format(new Date());
	public static String logoPath = System.getProperty("user.dir")+ "\\QDLogos\\logoQD.PNG";

	protected PrintWriter createWriter(String outdir) throws IOException {
		new File(outdir).mkdirs();
		return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "Custom-Report.html"))));
	}

	/**
	 * Creates a table showing the highlights of each test method with links to the
	 * method details
	 */
	protected void generateMethodSummaryReport(List<ISuite> suites) {

		startResultSummaryTable();
		for (ISuite suite : suites) {
			if (!suite.getName().contains(automationSuite)) {

				Map<String, ISuiteResult> r = suite.getResults();
				for (ISuiteResult r2 : r.values()) {
					ITestContext testContext = r2.getTestContext();
					String testName = testContext.getName();
					resultSummary(suite, testContext.getFailedConfigurations(), testName, "Failed TestCases",
							" (configuration methods)");
					resultSummary(suite, testContext.getFailedTests(), testName, "Failed TestCases", "");
					resultSummary(suite, testContext.getSkippedConfigurations(), testName, "skipped",
							" (configuration methods)");
					resultSummary(suite, testContext.getSkippedTests(), testName, "skipped", "");
					resultSummary(suite, testContext.getPassedTests(), testName, "Passed TestCases", "");
				}
			}
		}
		writer.println("</thead>");
		writer.println("</table>");
	}

	/** Creates a section showing known results for each method */
	protected void generateMethodDetailReport(List<ISuite> suites) {

		for (ISuite suite : suites) {
			if (!suite.getName().contains(automationSuite)) {
				Map<String, ISuiteResult> r = suite.getResults();
				for (ISuiteResult r2 : r.values()) {
					ITestContext testContext = r2.getTestContext();
					if (!r.isEmpty()) {
						writer.println("<h1>&nbsp;</h1>");
						++mdettestIndex;
						writer.println("<h4 id=\"test" + mdettestIndex + "\">" + testContext.getName() + " </h4>");
					}
					resultDetail(testContext.getFailedConfigurations());
					resultDetail(testContext.getFailedTests());
					resultDetail(testContext.getSkippedConfigurations());
					resultDetail(testContext.getSkippedTests());
					resultDetail(testContext.getPassedTests());
				}
			}
		}
	}

	/**
	 * @param tests
	 */
	@SuppressWarnings({})
	private void resultSummary(ISuite suite, IResultMap tests, String testname, String style, String details) {

		if (tests.getAllResults().size() > 0) {
			StringBuilder buff = new StringBuilder();
			String lastClassName = "";
			int mq = 0;
			for (ITestNGMethod method : getMethodSet(tests, suite)) {
				++msummethodIndex;
				ITestClass testClass = method.getTestClass();
				String className = testClass.getName();
				if (mq == 0) {
					String id = (msummethodIndex == null ? null
							: "MethodSummaryTest" + Integer.toString(msummethodIndex));
					titleRow(testname + " &#8212; " + style + details, 5, id);

				}
				if (!className.equalsIgnoreCase(lastClassName)) {
					mq = 0;
					buff.setLength(0);
					lastClassName = className;
				}
				Set<ITestResult> resultSet = tests.getResults(method);
				long end = Long.MIN_VALUE;
				long start = Long.MAX_VALUE;
				long startMS = 0;
				for (ITestResult testResult : tests.getResults(method)) {
					if (testResult.getEndMillis() > end) {
						end = testResult.getEndMillis() / 1000;
					}
					if (testResult.getStartMillis() < start) {
						startMS = testResult.getStartMillis();
						start = startMS / 1000;
					}
				}
				new SimpleDateFormat("hh:mm:ss");
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(startMS);

				mq += 1;
				String testInstanceName = resultSet.toArray(new ITestResult[] {})[0].getTestName();
				buff.append("<td><a href=\"#MethodsSummaryIndex" + msummethodIndex + "\"" + ">" + qualifiedName(method)
						+ " " + "</a>" + (null == testInstanceName ? "" : "<br>(" + testInstanceName + ")") + "</td>"
						+ "<td class=\"\">" + timeConversion(end - start) + "</td>" + "</tr>");
			}
			if (mq > 0) {
				writer.print("<tr class=\"\" \"" + ">");
				writer.println(buff);
			}
		}
	}

	private String timeConversion(long seconds) {

		final int MINUTES_IN_AN_HOUR = 60;
		final int SECONDS_IN_A_MINUTE = 60;

		int minutes = (int) (seconds / SECONDS_IN_A_MINUTE);
		seconds -= minutes * SECONDS_IN_A_MINUTE;

		int hours = minutes / MINUTES_IN_AN_HOUR;
		minutes -= hours * MINUTES_IN_AN_HOUR;

		return prefixZeroToDigit(hours) + ":" + prefixZeroToDigit(minutes) + ":" + prefixZeroToDigit((int) seconds);
	}

	private String prefixZeroToDigit(int num) {
		int number = num;
		if (number <= 9) {
			return "0" + number;
		} else {
			return "" + number;
		}
	}

	/** Starts and defines columns result summary table */
	private void startResultSummaryTable() {
		tableStart("Test Case Summary Report");
		writer.print("</tr></thead></table>");
		writer.print("<table  class=\"table table-bordered\" style=\"width:100%\"><thead>");
		writer.println("<tr style=\"background-color:#85C1E9; color:black\">" + "<th>Scenario</th>"
				+ "<th>Execution Time (hh:mm:ss) </th></tr>");
		writer.print("</thead>");
		mrow = 0;

	}

	private String qualifiedName(ITestNGMethod method) {
		StringBuilder addon = new StringBuilder();
		String[] groups = method.getGroups();
		int length = groups.length;
		if (length > 0 && !"basic".equalsIgnoreCase(groups[0])) {
			addon.append("(");
			for (int i = 0; i < length; i++) {
				if (i > 0) {
					addon.append(", ");
				}
				addon.append(groups[i]);
			}
			addon.append(")");
		}

		return "<b>" + method.getMethodName() + "</b> " + addon;
	}

	private synchronized void resultDetail(IResultMap tests) {

		Set<ITestResult> testResults = tests.getAllResults();
		List<ITestResult> testResultsList = new ArrayList<ITestResult>(testResults);
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		System.setProperty("java.util.Collections.useLegacyMergeSort", "true");
		Collections.sort(testResultsList, new TestResultsSorter());
		for (ITestResult result : testResultsList) {
			ITestNGMethod method = result.getMethod();
			++mdetmethodIndex;
			String cname = method.getTestClass().getName();
			writer.println("<h5 id=\"MethodsSummaryIndex" + mdetmethodIndex + "\">" + cname + ":"
					+ method.getMethodName() + "</h5>");
			generateResult(result, method);
			writer.println("<p class=\"totop\"><a href=\"#summary\">back to summary</a></p>");
		}
	}

	private void generateResult(ITestResult ans, ITestNGMethod method) {
		ans.getParameters();

		tableStart("Test Case Detailed Report");

		List<String> msgs = Reporter.getOutput(ans);
		boolean hasReporterOutput = msgs.size() > 0;
		Throwable exception = ans.getThrowable();
		boolean hasThrowable = exception != null;

		if (hasReporterOutput || hasThrowable) {
			writer.print("<table  class=\"table table-bordered\" style=\"width:100%;\">");
			writer.print("<col style=\"width:8%\"> " + "<col style=\"width:30%\">" + "<col style=\"width:54%\">"
					+ "<col style=\"width:8%\">");
			writer.print("<thead>");
			writer.print("<tr style=\"background-color:#C8C8FA;color:black\">");
			String[] col = { "Test Case ID", "Expected", "Actual", "Status" };
			for (String p : col) {
				writer.println("<th>" + Utils.escapeHtml(Utils.toString(p)) + "</th>");
			}
			writer.print("</tr>");
			writer.print("</thead >");
		} else {
			writer.println("<div>");
		}
		if (hasReporterOutput) {
			writer.print("<tbody style=\"width:100%;\">");
			boolean softAssert = false;

			for (int i = 0; i < msgs.size(); ++i) {
				writer.println("<tr>");
				if (i == 0) {
					writer.println("<td>" + method.getMethodName() + "</td>");

				} else {
					writer.println("<td>" + "" + "</td>");

				}
				if (!msgs.get(i).contains("<a href=")) {
					writer.println("<td>" + msgs.get(i) + "</td>");
					softAssert = true;
					++i;
				} else {
					softAssert = false;
				}
				if (softAssert) {
					if (i < msgs.size()) {
						if (i != msgs.size() - 1) {
							if (msgs.get(i + 1).contains("<a href=")) {
								writer.println("<td>" + msgs.get(i) + "</td>");
								writer.println("<td bgcolor=\"#FF0000\"> Failed </td>");
								++i;
								writer.println("<td>");
								ans.getAttribute("softAssert");
								writer.println("</td>");
								++i;
							} else if (msgs.get(i).contains("<a href=")) {
								boolean wantsMinimalOutput = ans.getStatus() == ITestResult.SUCCESS;
								writer.println("<td>");
								generateExceptionReport(exception, method);
								writer.println("</td>");
								writer.println("<td bgcolor=\"#E40505\">"
										+ (wantsMinimalOutput ? "Expected Exception" : "Failure") + "</td>");
							} else {
								writer.println("<td>" + msgs.get(i) + "</td>");
								writer.println("<td bgcolor=\"#1CD84F\"> Pass </td>");
								writer.println("<td>" + "" + "</td>");
							}

						} else {
							if (msgs.get(i).contains("<a href=")) {
								boolean wantsMinimalOutput = ans.getStatus() == ITestResult.SUCCESS;
								writer.println("<td>");
								generateExceptionReport(exception, method);
								writer.println("</td>");
								writer.println("<td bgcolor=\"#E40505\">"
										+ (wantsMinimalOutput ? "Expected Exception" : "Failure") + "</td>");
							} else {
								writer.println("<td>" + msgs.get(i) + "</td>");
								writer.println("<td bgcolor=\"#1CD84F\"> Pass </td>");
								writer.println("<td>" + "" + "</td>");
								writer.println("<td>" + "" + "</td>");
							}
						}
					}

				}
				writer.println("</tr>");
			}
			writer.print("</tbody>");
		}

		writer.println("</table>");
	}

	protected void generateExceptionReport(Throwable exception, ITestNGMethod method) {
		if (exception == null) {
			writer.print("Exception occured in method" + method);
		} else if (exception.getMessage() != null) {
			writer.print(exception.getMessage());
		} else {
			writer.print("Exception occured in method" + method);
		}
	}

	

	/**
	 * Since the methods will be sorted chronologically, we want to return the
	 * ITestNGMethod from the invoked methods.
	 */
	private Collection<ITestNGMethod> getMethodSet(IResultMap tests, ISuite suite) {

		List<IInvokedMethod> r = Lists.newArrayList();
		List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
		for (IInvokedMethod im : invokedMethods) {
			if (tests.getAllMethods().contains(im.getTestMethod())) {
				r.add(im);
			}
		}

		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		System.setProperty("java.util.Collections.useLegacyMergeSort", "true");
		Collections.sort(r, new TestSorter());
		List<ITestNGMethod> result = Lists.newArrayList();

		// Add all the invoked methods
		for (IInvokedMethod m : r) {
			for (ITestNGMethod temp : result) {
				if (!temp.equals(m.getTestMethod()))
					result.add(m.getTestMethod());
			}
		}

		Collection<ITestNGMethod> allMethodsCollection = tests.getAllMethods();
		List<ITestNGMethod> allMethods = new ArrayList<ITestNGMethod>(allMethodsCollection);
		Collections.sort(allMethods, new TestMethodSorter());

		for (ITestNGMethod m : allMethods) {
			if (!result.contains(m)) {
				result.add(m);
			}
		}
		return result;
	}

	public void generateSuiteSummaryReport(List<ISuite> suites) {
		writer.print("<h2 id=\"summary\"></h2>");
		tableStart("Suite Summary Report");
		writer.print("</tr></thead></table>");
		writer.print("<table  class=\"table table-bordered\" style=\"width:100%\"><thead>");
		writer.print("<tr style=\"background-color:#85C1E9; color:black\"><b>");
		tableColumnStart("Test");
		tableColumnStart("Test Cases<br/>Passed");
		tableColumnStart("# skipped");
		tableColumnStart("# failed");
		tableColumnStart("Start<br/>Time");
		tableColumnStart("End<br/>Time");
		tableColumnStart("Total Time<br/>(hh:mm:ss)");

		writer.println("</b></tr>");
		writer.print("</thead>");
		new DecimalFormat("#,##0.0");
		int qtytests = 0;
		int qtypassm = 0;
		int qtyskip = 0;
		long timestart = Long.MAX_VALUE;
		int qtyfail = 0;
		long timeend = Long.MIN_VALUE;
		msuitetestIndex = 1;
		for (ISuite suite : suites) {
			if (!suite.getName().contains(automationSuite)) {
				if (suites.size() >= 1) {
					titleRow(suite.getName(), 10);
				}
				Map<String, ISuiteResult> tests = suite.getResults();
				for (ISuiteResult r : tests.values()) {
					qtytests += 1;
					ITestContext overview = r.getTestContext();

					startSummaryRow(overview.getName());
					int q = getMethodSet(overview.getPassedTests(), suite).size();
					qtypassm += q;
					summaryCell(q, Integer.MAX_VALUE);
					q = getMethodSet(overview.getSkippedTests(), suite).size();
					qtyskip += q;
					summaryCell(q, 0);
					q = getMethodSet(overview.getFailedTests(), suite).size();
					qtyfail += q;
					summaryCell(q, 0);

					SimpleDateFormat summaryFormat = new SimpleDateFormat("hh:mm:ss");
					summaryCell(summaryFormat.format(overview.getStartDate()), true);
					writer.println("</td>");

					summaryCell(summaryFormat.format(overview.getEndDate()), true);
					writer.println("</td>");

					timestart = Math.min(overview.getStartDate().getTime(), timestart);
					timeend = Math.max(overview.getEndDate().getTime(), timeend);
					summaryCell(
							timeConversion(
									(overview.getEndDate().getTime() - overview.getStartDate().getTime()) / 1000),
							true);

					writer.println("</tr>");
					msuitetestIndex++;
				}
			}
		}
		if (qtytests > 1) {
			writer.println("<tr class=\"total\"><td>Total</td>");
			summaryCell(qtypassm, Integer.MAX_VALUE, "1CD84F");
			summaryCell(qtyskip, 0, "F58E0C");
			summaryCell(qtyfail, 0, "E40505");
			summaryCell(" ", true);
			summaryCell(" ", true);
			summaryCell(timeConversion(((timeend - timestart) / 1000)), true);
			writer.println("</tr>");
		}

		writer.println("</table>");
	}

	@SuppressWarnings("unused")
	private void summaryCell(String[] val) {
		StringBuilder b = new StringBuilder();
		for (String v : val) {
			b.append(v + " ");
		}
		summaryCell(b.toString(), true);
	}

	private void summaryCell(String v, boolean isgood) {
		writer.print("<td class=\"numi\"" + (isgood ? "" : "_attn") + "><center>" + v + "</center></td>");
	}

	private void summaryCell(String v, boolean isgood, String colour) {
		writer.print("<td bgcolor=\"#" + colour + "\" class=\"numi\"" + (isgood ? "" : "_attn") + "><center>" + v
				+ "</center></td>");
	}

	private void startSummaryRow(String label) {
		mrow += 1;
		writer.print("<tr" + (mrow % 2 == 0 ? " class=\"stripe\"" : "")
				+ "><td style=\"text-align:left;padding-right:2em\"><a href=\"#test" + msuitetestIndex + "\"><b>"
				+ label + "</b></a>" + "</td>");

	}

	private void summaryCell(int v, int maxexpected) {
		summaryCell(String.valueOf(v), v <= maxexpected);
	}

	private void summaryCell(int v, int maxexpected, String colour) {
		summaryCell(String.valueOf(v), v <= maxexpected, colour);
	}

	private void tableStart(String tableHeader) {
		writer.println("<table style= \"width:100%;\">");
		writer.print("<thead>");
		writer.println("<tr style=\"background-color:#808080;color:white\"><th>" + tableHeader + "</th>");
		mrow = 0;
	}

	private void tableColumnStart(String label) {
		writer.print("<th>" + label + "</th>");
	}

	private void titleRow(String label, int cq) {
		titleRow(label, cq, null);
	}

	private void titleRow(String label, int cq, String id) {
		writer.print("<tr bgcolor=\"#C8C8FA\" ");
		if (id != null) {
			writer.print(" id=\"" + id + "\"");
		}
		writer.println("><th colspan=" + "\"" + cq + "\">" + label + "</th></tr>");
		mrow = 0;
	}

	protected void writeReportTitle(String title) {
		writer.print("" + "<table id= \"summary\" class=\"table\" style=\"width:100%;height:100%;\">"
				+ " <tbody style = \\\"background-color:rgb(240, 240, 240); width:100%; height:100%;\\\">"
				+ "<tr align=\"center\">" + "<td style=\"border: none;padding-left: 197px;\" height=\"80\">"
				+ "<h1 style=\"color:#1515E9\">" + title + " - " + getDateAsString() + "</h1>" + "</td>"
				+ "<td style=\"border: none;\" height=\"20\"><img height=\"130\" width=\"130\" src= "+logoPath+"></td>"
				+ "</tr>" + "</tbody>" + "</table>");
	}


	public void writeReportSummary(List<ISuite> suites) {
		new DecimalFormat("#,##0.0");
		int qtytests = 0;
		int qtypassm = 0;
		long timestart = Long.MAX_VALUE;
		int qtyfail = 0;
		long timeend = Long.MIN_VALUE;
		String totaltime = "";
		msuitetestIndex = 1;
		for (ISuite suite : suites) {
			if (!suite.getName().contains(automationSuite)) {
				Map<String, ISuiteResult> tests = suite.getResults();
				for (ISuiteResult r : tests.values()) {
					ITestContext overview = r.getTestContext();

					int q = getMethodSet(overview.getPassedTests(), suite).size();
					qtypassm += q;
					q = getMethodSet(overview.getFailedTests(), suite).size();
					qtyfail += q;

					timestart = Math.min(overview.getStartDate().getTime(), timestart);
					timeend = Math.max(overview.getEndDate().getTime(), timeend);
					totaltime = timeConversion(
							(overview.getEndDate().getTime() - overview.getStartDate().getTime()) / 1000);
					msuitetestIndex++;
					qtytests = qtypassm + qtyfail;
				}
			}
		}
		writer.print("" + "<table class=\"table table-bordered\" align=\"center\" style=\"width:50%\" >" + "<thead>"
				+ "<tr>" + "<td width=\"80%\"><b>Documents Scanned</b></td>" + "<td width=\"20%\">" + qtytests + "</td>"
				+ "</tr>" + "<tr>" + "<td \"width:50%\"=\"\"><b>PASS</b></td>" + "<td>" + qtypassm + "</td>" + "</tr>"
				+ "<tr>" + "<td \"width:50%\"=\"\"><b>FAIL</b></td>" + "<td>" + qtyfail + "</td>" + "</tr>" + "<tr>"
				+ "<td \"width:50%\"=\"\"><b>Total Time (hh:mm:ss)</b></td>" + "<td>" + totaltime + "</td>" + "</tr>"
				+ "</thead>" + "</table>");

		writer.println("</table>");
	}

	private String getDateAsString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/** Starts HTML stream */
	protected void startHtml(PrintWriter out) {
		out.println("<!DOCTYPE html>\r\n" + "<html lang=\"en\">\r\n" + "<head>\r\n" + "<meta charset=\"utf-8\">\r\n"
				+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\r\n"
				+ "<title>QUALIDEX Report</title>\r\n"
				+ "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\r\n"
				+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\">\r\n"
				+ "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\r\n"
				+ "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\"></script>\r\n"
				+ "<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\r\n"
				+ "<style type=\"text/css\">\r\n" + "    .bs-example{\r\n" + "     margin: 20px;\r\n" + "    }\r\n"
				+ "</style>\r\n" + "</head>"
				+ "<body style = \"background-color:rgb(240, 240, 240); width:100%; height:100%;\" >"
				+ "<div class=\"bs-example\">");

	}

	/** Finishes HTML stream */
	protected void endHtml(PrintWriter out) {
		out.println("</body></html>");
	}

	// ~ Inner Classes --------------------------------------------------------
	/** Arranges methods by classname and method name */
	private class TestSorter implements Comparator<IInvokedMethod> {
		// ~ Methods
		// -------------------------------------------------------------

		/** Arranges methods by classname and method name */
		public int compare(IInvokedMethod obj1, IInvokedMethod obj2) {
			return obj1.getTestMethod().getTestClass().getName()
					.compareTo(obj2.getTestMethod().getTestClass().getName());
		}
	}

	private class TestMethodSorter implements Comparator<ITestNGMethod> {
		public int compare(ITestNGMethod obj1, ITestNGMethod obj2) {
			int r = obj1.getTestClass().getName().compareTo(obj2.getTestClass().getName());
			if (r == 0) {
				r = obj1.getMethodName().compareTo(obj2.getMethodName());
			}
			return r;
		}
	}

	private class TestResultsSorter implements Comparator<ITestResult> {
		public int compare(ITestResult obj1, ITestResult obj2) {
			int result = obj1.getTestClass().getName().compareTo(obj2.getTestClass().getName());
			if (result == 0) {
				result = obj1.getMethod().getMethodName().compareTo(obj2.getMethod().getMethodName());
			}
			return result;
		}
	}

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outdir) {

		try {
			outdir = outdir.replace("test-output", "ResultOutput" + File.separator + timeStamp.split(" ")[0]
					+ File.separator + timeStamp.split(" ")[1]);
			File file = new File(outdir);
			if (!file.exists()) {
				file.mkdirs();
			}
			writer = createWriter(outdir);
		} catch (IOException e) {
			logger.info("Unable to create output file " + e);
			return;
		}

		startHtml(writer);
		writeReportTitle(reportTitle);
		writeReportSummary(suites);
		generateMethodSummaryReport(suites);
		generateMethodDetailReport(suites);
		endHtml(writer);
		writer.flush();
		writer.close();

	}
}
