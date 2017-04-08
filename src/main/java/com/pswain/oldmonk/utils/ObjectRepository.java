package com.pswain.oldmonk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Load all properties file which contain element locators.
 */
public class ObjectRepository
{
    private static final Logger LOGGER   = LoggerFactory.getLogger(ProjectConfigurator.class);

    private static Properties   props    = new Properties();
    private static boolean      isLoaded = false;

    /*
     * All files ending with '.properties' or '.PROPERTIES' present in specified
     * directory will be treated as locator files.
     */
    private static String       objectRepositoryDirectory;

    public static void setRepositoryDirectory(String objectRepositoryDir)
    {
        objectRepositoryDirectory = objectRepositoryDir;
    }

    /**
     * Load all locator files
     * 
     * @return - Properties containing all locators in name/value pair
     * @throws IOException
     *             - throw this exception, if locator files are not found
     */
    public static Properties initialize() throws IOException
    {
        if (!isLoaded)
        {
            File objRepositoryDir = new File(objectRepositoryDirectory);

            File[] locatorFiles = objRepositoryDir.listFiles();
            for (File f : locatorFiles)
            {
                if (f.getName().endsWith(".properties") || f.getName().endsWith(".PROPERTIES"))
                {
                    LOGGER.info("Loading locator file: " + f.getName());

                    try
                    {
                        props.load(new FileInputStream(f));
                    } catch (IOException e)
                    {
                        throw new IOException("Unable to load properties file " + f.getName() + "!");
                    }
                }

                isLoaded = true;
            }
        }

        return props;
    }
}
