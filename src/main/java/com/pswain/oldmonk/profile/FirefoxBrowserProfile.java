package com.pswain.oldmonk.profile;

import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * Create browser profile for firefox. Usually this is represented as Firefox
 * Profile in WebDriver for firefox browser.
 */
public class FirefoxBrowserProfile extends BrowserProfile
{
    protected FirefoxProfile profile;

    public FirefoxBrowserProfile()
    {
        profile = new FirefoxProfile();
    }

    @Override
    public Object createProfile()
    {
        return profile;
    }

    /**
     * Accept untrusted certificates
     * 
     * @param acceptUntrustedSsl
     *            - boolean value specifying whether to accept untrusted
     *            certificates or not!
     * @return - this profile instance
     */
    public FirefoxBrowserProfile setAcceptUntrustedCertificates(boolean acceptUntrustedSsl)
    {
        profile.setAcceptUntrustedCertificates(acceptUntrustedSsl);
        return this;
    }

    /**
     * @param untrustedIssuer
     * @return
     */
    public FirefoxBrowserProfile setAssumeUntrustedCertificateIssuer(boolean untrustedIssuer)
    {
        profile.setAssumeUntrustedCertificateIssuer(untrustedIssuer);
        return this;
    }

    /**
     * Whether to show download manager while downloading files.
     * 
     * @param showDownloadManager
     *            - boolean value specifying whether to show download manager!
     * @return - this profile instance
     */
    public FirefoxBrowserProfile showDownloadManagerWhenStarting(boolean showDownloadManager)
    {
        profile.setPreference("browser.download.manager.showWhenStarting", showDownloadManager);
        return this;
    }

    /**
     * Set default download directory
     * 
     * @param downloadDirectory
     *            - path to download directory
     * @return - this profile instance
     */
    public FirefoxBrowserProfile setDownloadDirectory(String downloadDirectory)
    {
        profile.setPreference("browser.download.dir", downloadDirectory);
        return this;
    }

    /**
     * While downloading files, do not show dialog for asking permission to save
     * file for specified files.
     * 
     * @param filesToSaveDirectly
     *            - for which file types, we do not want the browser to ask
     *            permission.
     * @return - this profile instance
     */
    public FirefoxBrowserProfile neverAskAndSaveFile(String filesToSaveDirectly)
    {
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", filesToSaveDirectly);
        return this;
    }
}
