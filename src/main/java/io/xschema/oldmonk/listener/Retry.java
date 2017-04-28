package io.xschema.oldmonk.listener;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import io.xschema.oldmonk.utils.ProjectConfigurator;

/**
 * Class to re-run test cases before marking it as failed. 'max.retry.count'
 * property defines how many times the test case will be executed before marking
 * it as failed.
 */
public class Retry implements IRetryAnalyzer
{
    private static final Logger LOGGER     = LoggerFactory.getLogger(Retry.class.getName());

    private int                 retryCount = 1;
    private Properties          config;

    @Override
    public boolean retry(final ITestResult result)
    {
        try
        {
            config = ProjectConfigurator.initializeProjectConfigurationsFromFile("project.properties");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        int maxRetryCount = Integer.parseInt(config.getProperty("max.retry.count"));

        if (retryCount <= maxRetryCount)
        {
            LOGGER.info("Retry: " + retryCount + ", Rerunning Failed Test: " + result.getTestClass().getName());
            retryCount++;

            return true;
        }

        return false;
    }
}
