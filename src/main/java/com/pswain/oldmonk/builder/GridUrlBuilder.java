package com.pswain.oldmonk.builder;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Url builder for Selenium GRID.
 */
public abstract class GridUrlBuilder
{
    /**
     * Add protocol to the Selenium GRID url builder.
     * 
     * @param protocol
     *            - can be 'http' or 'https'
     * @return - instance of the class extending the abstract builder
     */
    public abstract GridUrlBuilder addProtocol(Protocol protocol);

    /**
     * Add selenium hub host to the Selenium GRID url builder.
     * 
     * @param host
     *            - If Selenium GRID is set up on local machine, it should be
     *            '127.0.0.1', else it should be the ip of the machine where
     *            Selenium GRID is set up.
     * @return - instance of the class extending the abstract builder
     */
    public abstract GridUrlBuilder addSeleniumHubHost(String host);

    /**
     * Add selenium hub port to the Selenium GRID url builder.
     * 
     * @param port
     *            - port where Selenium GRID is running. Default port is 4444
     * @return - instance of the class extending the abstract builder
     */
    public abstract GridUrlBuilder addSeleniumHubPort(int port);

    /**
     * Build the Selenium GRID url
     * 
     * @return - Selenium GRID url
     * @throws MalformedURLException
     *             - throw this exception if, grid url is malformed
     */
    public abstract URL build() throws MalformedURLException;
}