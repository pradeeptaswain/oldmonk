package com.pswain.oldmonk.profile;

import java.io.File;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Create browser profile for chrome. Usually this is represented as Chrome
 * Options in WebDriver for chrome browser.
 */
public class ChromeBrowserProfile extends BrowserProfile
{
    protected ChromeOptions options;

    public ChromeBrowserProfile()
    {
        options = new ChromeOptions();
    }

    @Override
    public Object createProfile()
    {
        return options;
    }

    /**
     * Add chrome extensions
     * 
     * @param extensions
     *            - List of chrome extensions
     * @return - this profile instance
     */
    public ChromeBrowserProfile addExtensions(File... extensions)
    {
        options.addExtensions(extensions);
        return this;
    }

    /**
     * Add custom chrome profile
     * 
     * @param profilePath
     *            - path to chrome profile directory
     * @return - this profile instance
     */
    public ChromeBrowserProfile addCustomProfile(String profilePath)
    {
        options.addArguments("user-data-dir=" + profilePath);
        return this;
    }

    /**
     * Start chrome browser in maximized mode
     * 
     * @return - this profile instance
     */
    public ChromeBrowserProfile startMaximized()
    {
        options.addArguments("start-maximized");
        return this;
    }

    /**
     * Set chrome binary file path
     * 
     * @param binaryExecutablePath
     *            - executable chrome binary path
     * @return - this profile instance
     */
    public ChromeBrowserProfile setBinaryExecutablePath(String binaryExecutablePath)
    {
        options.setBinary(binaryExecutablePath);
        return this;
    }

    /**
     * Set browser preferences
     * 
     * @param prefs
     *            - A key value pair of prefrences and values
     * @return - this profile instance
     */
    public ChromeBrowserProfile setPreference(Map<String, Object> prefs)
    {
        options.setExperimentalOption("prefs", prefs);
        return this;
    }
}
