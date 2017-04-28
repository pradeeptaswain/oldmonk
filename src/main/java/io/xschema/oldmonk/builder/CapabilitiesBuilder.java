package io.xschema.oldmonk.builder;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.xschema.oldmonk.exception.CapabilityNotPresentException;
import io.xschema.oldmonk.profile.BrowserProfile;

/**
 * Build the capabilities for browsers, which will be passed to the driver
 * factory to create appropriate driver based on capabilities.
 */
public abstract class CapabilitiesBuilder
{
    /**
     * Add browser to the capability builder.
     * 
     * @param browser
     *            - can be 'firefox', 'chrome' or 'ie'
     * @return - this builder instance
     */
    public abstract CapabilitiesBuilder addBrowser(String browser);

    /**
     * Add browser version to the capability builder
     * 
     * @param version
     *            - browser version
     * @return - this builder instance
     */
    public abstract CapabilitiesBuilder addVersion(String version);

    /**
     * Add platform to the capability builder
     * 
     * @param platform
     *            - can be 'windows', 'mac' or 'linux'
     * @return - this builder instance
     */
    public abstract CapabilitiesBuilder addPlatform(String platform);

    /**
     * Add browser profile to capability builder
     * 
     * @param browserProfile
     *            - can be firefox profile, chrome options or ie desired
     *            capabilities
     * @return - this builder instance
     * @throws CapabilityNotPresentException
     *             - throw this exception, if capabilities can not be created
     *             based on the parameters passed. For ex. trying to craete
     *             capability for unknown browser
     */
    public abstract CapabilitiesBuilder addBrowserProfile(BrowserProfile browserProfile)
            throws CapabilityNotPresentException;

    /**
     * Add proxy support in browser
     * 
     * @param host
     *            - proxy host
     * @param port
     *            - proxy port
     * @return - this builder instance
     */
    public abstract CapabilitiesBuilder addProxy(String host, int port);

    /**
     * Build the desired capability.
     */
    public abstract DesiredCapabilities build();

    /**
     * Add browser executable driver path. For chrome, add path to chromedriver
     * and for firefox add path to geckodriver
     * 
     * @param path
     *            - path of executable driver.
     * @return - this builder instance
     */
    public abstract CapabilitiesBuilder addBrowserDriverExecutablePath(String path);
}
