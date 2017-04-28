package io.xschema.oldmonk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Load the project configuration file. Generally it's a property file named as
 * application.properties or project.properties, wich contains all application
 * specifi configurations.
 */
public class ProjectConfigurator
{
    private static final Logger LOGGER   = LoggerFactory.getLogger(ProjectConfigurator.class);

    private static Properties   props    = new Properties();
    private static boolean      isLoaded = false;

    /**
     * Load the project configuration file.
     * 
     * @param configFileName
     *            - name of config file ending with '.properties' or
     *            '.PROPERTIES'
     * @return - properties containing all configurations in name/value pair.
     * @throws IOException
     *             - throw this exception, if configuration file is not found
     */
    public static Properties initializeProjectConfigurationsFromFile(String configFileName) throws IOException
    {
        if (!(configFileName.endsWith(".properties") || configFileName.endsWith(".PROPERTIES")))
        {
            throw new IllegalArgumentException("Project configuration file should have '.properties' extension!");
        }

        try
        {
            if (!isLoaded)
            {
                LOGGER.info("Loading project configuration file '" + configFileName + "'");
                props.load(new FileInputStream(new File(configFileName)));
                isLoaded = true;
            }
        } catch (IOException e)
        {
            throw new IOException("Unable to load configuration file '" + configFileName + "'!");
        }

        return props;
    }
}
