package com.pswain.oldmonk.core;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;
import org.testng.reporters.util.StackTraceTools;
import org.testng.xml.XmlSuite;

import com.pswain.oldmonk.utils.ProjectConfigurator;

/**
 * Reporter API class to assist in generating custom test results reports.
 */
public class ReporterAPI
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReporterAPI.class.getName());

    private String              suiteName;
    private String              parallelRunParam;

    private List<ISuite>        suites;

    private Properties          config;

    public ReporterAPI(List<XmlSuite> arg0, List<ISuite> suites, String outputDir) throws IOException
    {
        this.suites = suites;

        for (ISuite suite : suites)
        {
            suiteName = suite.getName();
            parallelRunParam = suite.getParallel();
        }

        config = ProjectConfigurator.initializeProjectConfigurationsFromFile("project.properties");
    }

    public String getSuiteName()
    {
        return suiteName;
    }

    public List<String> getAllTestNames()
    {
        List<String> allTestNames = new ArrayList<String>();

        for (ISuite suite : suites)
        {
            suiteName = suite.getName();
            parallelRunParam = suite.getParallel();

            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                allTestNames.add(r.getTestContext().getName());
            }
        }

        return allTestNames;
    }

    public List<String> getIncludedModulesForTest(String testName)
    {
        ITestContext overview = null;

        List<String> includedModules = new ArrayList<String>();
        String includedModule = "";
        String modulesCommaSeparated = config.getProperty("application.modules").replaceAll("\\s+", "");

        for (ISuite suite : suites)
        {
            suiteName = suite.getName();

            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                if (overview.getName().equalsIgnoreCase(testName))
                {
                    String[] allDefinedModules = null;

                    if (modulesCommaSeparated == null || modulesCommaSeparated.trim().length() == 0)
                    {
                        Assert.fail("ERROR - Modules are not found in properties file");
                    } else
                    {
                        allDefinedModules = new String[modulesCommaSeparated.length()];
                        allDefinedModules = modulesCommaSeparated.split(",");
                    }

                    ITestNGMethod[] allTestMethods = overview.getAllTestMethods();

                    for (ITestNGMethod testngMethod : allTestMethods)
                    {
                        String[] modules = testngMethod.getGroups();
                        for (String module : modules)
                        {
                            for (String moduleName : allDefinedModules)
                            {
                                if (module.equalsIgnoreCase(moduleName))
                                {
                                    if (!(includedModule.contains(module)))
                                    {
                                        includedModules.add(module);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return includedModules;
    }

    public List<String> getIncludedGroupsForTest(String testName)
    {
        ITestContext overview = null;

        List<String> includedGroups = new ArrayList<String>();
        String includedGroup = "";
        String groupsCommaSeparated = config.getProperty("test.groups").replaceAll("\\s+", "");

        for (ISuite suite : suites)
        {
            suiteName = suite.getName();

            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                if (overview.getName().equalsIgnoreCase(testName))
                {

                    String[] allDefinedGroups = null;

                    if (groupsCommaSeparated == null || groupsCommaSeparated.trim().length() == 0)
                    {
                        Assert.fail("ERROR - Test Groups are not found in properties file");
                    } else
                    {
                        allDefinedGroups = new String[groupsCommaSeparated.length()];
                        allDefinedGroups = groupsCommaSeparated.split(",");
                    }

                    ITestNGMethod[] allTestMethods = overview.getAllTestMethods();

                    for (ITestNGMethod testngMethod : allTestMethods)
                    {
                        String[] groups = testngMethod.getGroups();
                        for (String group : groups)
                        {
                            for (String groupName : allDefinedGroups)
                            {
                                if (group.equalsIgnoreCase(groupName))
                                {
                                    if (!(includedGroup.contains(group)))
                                    {
                                        includedGroups.add(group);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return includedGroups;
    }

    public String getReportGenerationDateAndTime()
    {
        Calendar currentdate = Calendar.getInstance();

        String reportGenerationDateAndTime = null;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a z");
        reportGenerationDateAndTime = formatter.format(currentdate.getTime());

        TimeZone obj = TimeZone.getTimeZone("IST");

        formatter.setTimeZone(obj);
        reportGenerationDateAndTime = formatter.format(currentdate.getTime());

        return reportGenerationDateAndTime;
    }

    public String getParallelRunParameter()
    {
        return parallelRunParam;
    }

    public int getTotalNumberOfMethodsForSuite()
    {
        return getTotalPassedMethodsForSuite() + getTotalFailedMethodsForSuite() + getTotalSkippedMethodsForSuite();
    }

    public int getTotalPassedMethodsForSuite()
    {
        int totalNumberOfPassedMethodsForSuite = 0;

        ITestContext overview = null;

        for (ISuite suite : suites)
        {
            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                totalNumberOfPassedMethodsForSuite += overview.getPassedTests().getAllMethods().size();
            }
        }

        return totalNumberOfPassedMethodsForSuite;
    }

    public int getTotalFailedMethodsForSuite()
    {
        int totalNumberOfFailedMethodsForSuite = 0;

        ITestContext overview = null;

        for (ISuite suite : suites)
        {
            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                totalNumberOfFailedMethodsForSuite += overview.getFailedTests().getAllMethods().size();
            }
        }

        return totalNumberOfFailedMethodsForSuite;
    }

    public int getTotalSkippedMethodsForSuite()
    {
        int totalNumberOfSkippedMethodsForSuite = 0;

        ITestContext overview = null;

        for (ISuite suite : suites)
        {
            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                totalNumberOfSkippedMethodsForSuite += overview.getSkippedTests().getAllMethods().size();
            }
        }

        return totalNumberOfSkippedMethodsForSuite;
    }

    public String getSuccessPercentageForSuite()
    {
        String successPercentageForSuite = "";

        NumberFormat nf = NumberFormat.getInstance();

        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(true);

        try
        {
            successPercentageForSuite = nf
                    .format(((float) getTotalPassedMethodsForSuite() / (float) (getTotalPassedMethodsForSuite()
                            + getTotalFailedMethodsForSuite() + getTotalSkippedMethodsForSuite())) * 100);
        } catch (NumberFormatException realCause)
        {
            LOGGER.debug(realCause.getMessage());
        }

        return successPercentageForSuite;
    }

    public int getTotalPassedMethodsForTest(final String testName)
    {
        int totalNumberOfPassedMethodsForTest = 0;

        ITestContext overview = null;

        for (ISuite suite : suites)
        {
            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                if (testName.equalsIgnoreCase(overview.getName()))
                {
                    totalNumberOfPassedMethodsForTest = overview.getPassedTests().getAllMethods().size();
                    break;
                }
            }
        }

        return totalNumberOfPassedMethodsForTest;
    }

    public int getTotalFailedMethodsForTest(String testName)
    {
        int totalNumberOfFailedMethodsForTest = 0;

        ITestContext overview = null;

        for (ISuite suite : suites)
        {
            suiteName = suite.getName();

            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                if (testName.equalsIgnoreCase(overview.getName()))
                {
                    totalNumberOfFailedMethodsForTest = overview.getFailedTests().getAllMethods().size();
                    break;
                }
            }
        }

        return totalNumberOfFailedMethodsForTest;
    }

    public int getTotalSkippedMethodsForTest(final String testName)
    {
        int totalNumberOfSkippedMethodsForTest = 0;
        ITestContext overview = null;

        for (ISuite suite : suites)
        {
            suiteName = suite.getName();

            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();
                if (testName.equalsIgnoreCase(overview.getName()))
                {
                    totalNumberOfSkippedMethodsForTest = overview.getSkippedTests().getAllMethods().size();
                    break;
                }
            }
        }

        return totalNumberOfSkippedMethodsForTest;
    }

    public String getSuccessPercentageForTest(String testName)
    {
        String successPercentageForTest = null;

        NumberFormat nf = NumberFormat.getInstance();

        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(true);

        int totalPassedMethodsForTest = 0;
        int totalFailedMethodsForTest = 0;
        int totalSkippedMethodsForTest = 0;

        ITestContext overview = null;

        for (ISuite suite : suites)
        {
            suiteName = suite.getName();

            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();
                if (testName.equalsIgnoreCase(overview.getName()))
                {
                    totalPassedMethodsForTest = overview.getPassedTests().getAllMethods().size();
                    totalFailedMethodsForTest = overview.getFailedTests().getAllMethods().size();
                    totalSkippedMethodsForTest = overview.getSkippedTests().getAllMethods().size();

                    break;
                }
            }
        }

        try
        {
            successPercentageForTest = nf.format(((float) totalPassedMethodsForTest
                    / (float) (totalPassedMethodsForTest + totalFailedMethodsForTest + totalSkippedMethodsForTest))
                    * 100);
        } catch (NumberFormatException realCause)
        {
            LOGGER.debug(realCause.getMessage());
        }

        return successPercentageForTest;
    }

    public Map<String, String> getAllParametersForTest(String testName)
    {
        ITestContext overview = null;

        for (ISuite suite : suites)
        {
            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                if (testName.equalsIgnoreCase(overview.getName()))
                {
                    return overview.getCurrentXmlTest().getAllParameters();
                }
            }
        }

        return null;
    }

    public String getParameterValueForTest(String testName, String parameter)
    {
        ITestContext overview = null;

        for (ISuite suite : suites)
        {
            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                if (testName.equalsIgnoreCase(overview.getName()))
                {
                    return overview.getCurrentXmlTest().getParameter(parameter);
                }
            }
        }

        return null;
    }

    public Long getMethodExecutionTime(String testName, String methodName)
    {
        long startTime = Long.MAX_VALUE;
        long endTime = Long.MIN_VALUE;

        Iterator<ITestResult> it2 = getMethodResults(testName, methodName).iterator();

        while (it2.hasNext())
        {
            ITestResult result = it2.next();

            startTime = result.getStartMillis();
            endTime = result.getEndMillis();

            break;
        }

        return (endTime - startTime);
    }

    public String getMethodExecutionStatus(String testName, String methodName)
    {
        Iterator<ITestResult> it2 = getMethodResults(testName, methodName).iterator();

        String executionStatus = null;

        while (it2.hasNext())
        {
            ITestResult result = it2.next();

            if (result.getStatus() == ITestResult.SUCCESS)
            {
                executionStatus = "PASS";
            } else if (result.getStatus() == ITestResult.FAILURE)
            {
                executionStatus = "FAIL";
            } else if (result.getStatus() == ITestResult.SKIP)
            {
                executionStatus = "SKIP";
            }

            break;
        }

        return executionStatus;
    }

    public int getTotalNumberOfMethodsForTest(String testName)
    {
        return getTotalFailedMethodsForTest(testName) + getTotalPassedMethodsForTest(testName)
                + getTotalSkippedMethodsForTest(testName);
    }

    public Set<ITestResult> getMethodResults(final String testName, final String methodName)
    {
        ITestContext overview = null;

        Set<ITestResult> testResult = null;

        for (ISuite suite : suites)
        {
            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                if (testName.equalsIgnoreCase(overview.getName()))
                {
                    ITestNGMethod[] testMethods = overview.getAllTestMethods();
                    for (ITestNGMethod method : testMethods)
                    {
                        if (method.getMethodName().equalsIgnoreCase(methodName))
                        {
                            if (!(overview.getPassedTests().getResults(method).isEmpty()))
                            {
                                testResult = overview.getPassedTests().getResults(method);
                                break;
                            } else if (!(overview.getFailedTests().getResults(method).isEmpty()))
                            {
                                testResult = overview.getFailedTests().getResults(method);
                                break;
                            } else if (!(overview.getSkippedTests().getResults(method).isEmpty()))
                            {
                                testResult = overview.getSkippedTests().getResults(method);
                                break;
                            }
                        }
                    }
                }
            }
        }

        return testResult;
    }

    public String getTestFailureStackTrace(final String testName, final String methodName)
    {
        ITestContext overview = null;

        Set<ITestResult> testResult = null;

        for (ISuite suite : suites)
        {
            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                if (testName.equalsIgnoreCase(overview.getName()))
                {
                    ITestNGMethod[] testMethods = overview.getAllTestMethods();
                    for (ITestNGMethod method : testMethods)
                    {
                        if (method.getMethodName().equalsIgnoreCase(methodName))
                        {
                            if (!(overview.getPassedTests().getResults(method).isEmpty()))
                            {
                                testResult = overview.getPassedTests().getResults(method);
                            } else if (!(overview.getFailedTests().getResults(method).isEmpty()))
                            {
                                testResult = overview.getFailedTests().getResults(method);
                            } else if (!(overview.getSkippedTests().getResults(method).isEmpty()))
                            {
                                testResult = overview.getSkippedTests().getResults(method);
                            }

                            Iterator<ITestResult> it2 = testResult.iterator();

                            while (it2.hasNext())
                            {
                                ITestResult result = it2.next();
                                return generateExceptionReport(result.getThrowable(), method);
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private String generateExceptionReport(Throwable exception, ITestNGMethod method)
    {
        return generateExceptionReport(exception, method, exception.getLocalizedMessage());
    }

    private String generateExceptionReport(Throwable exception, ITestNGMethod method, String title)
    {
        StackTraceElement[] s1 = exception.getStackTrace();
        Throwable t2 = exception.getCause();

        StringBuilder exceptionStackTrace = new StringBuilder();

        if (t2 == exception)
        {
            t2 = null;
        }
        int maxlines = Math.min(100, StackTraceTools.getTestRoot(s1, method));
        for (int x = 0; x <= maxlines; x++)
        {
            exceptionStackTrace.append((x > 0 ? "<br/>at " : "") + Utils.escapeHtml(s1[x].toString()));
        }
        if (maxlines < s1.length)
        {
            exceptionStackTrace.append("<br/>" + (s1.length - maxlines) + " lines not shown");
        }
        if (t2 != null)
        {
            generateExceptionReport(t2, method, "Caused by " + t2.getLocalizedMessage());
        }

        return exceptionStackTrace.toString();
    }

    public String getFailedTestScreenshotPath(final String testName, final String methodName)
    {
        Iterator<ITestResult> it2 = getMethodResults(testName, methodName).iterator();
        List<String> reporterOutput = new ArrayList<String>();

        String screenshotFileLink = "";

        while (it2.hasNext())
        {
            ITestResult result = it2.next();

            reporterOutput = Reporter.getOutput(result);
            break;
        }

        for (String line : reporterOutput)
        {
            if (line.contains("[Console Log] Screenshot saved in"))
            {
                screenshotFileLink = line.substring(line.indexOf("in") + 3, line.length());
                break;
            }
        }

        return screenshotFileLink;
    }

    public ITestNGMethod[] getAllMethodsForTest(String testName)
    {
        ITestContext overview = null;

        for (ISuite suite : suites)
        {
            suiteName = suite.getName();

            Map<String, ISuiteResult> tests = suite.getResults();

            for (ISuiteResult r : tests.values())
            {
                overview = r.getTestContext();

                if (overview.getName().equalsIgnoreCase(testName))
                {
                    return overview.getAllTestMethods();
                }
            }
        }

        return null;
    }
}
