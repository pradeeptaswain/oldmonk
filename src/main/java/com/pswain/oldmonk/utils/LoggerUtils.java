package com.pswain.oldmonk.utils;

import org.slf4j.MDC;

/**
 * Utility class to generate log files for each test class.
 */
public class LoggerUtils
{
    public static final String TEST_NAME = "testname";

    /**
     * Adds the test name to MDC so that sift appender can use it and log the
     * new log events to a different file
     * 
     * @param name
     *            name of the new log file
     * @throws Exception
     */
    public static void startTestLogging(final String name)
    {
        MDC.put(TEST_NAME, name);
    }

    /**
     * Removes the key (log file name) from MDC
     * 
     * @return name of the log file, if one existed in MDC
     */
    public static String stopTestLogging()
    {
        final String name = MDC.get(TEST_NAME);
        MDC.remove(TEST_NAME);
        return name;
    }
}
