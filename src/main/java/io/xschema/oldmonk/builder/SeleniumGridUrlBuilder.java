package io.xschema.oldmonk.builder;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Build the Selenium GRID url.
 */
public class SeleniumGridUrlBuilder extends GridUrlBuilder
{
    private String url = "";

    @Override
    public GridUrlBuilder addProtocol(Protocol protocol)
    {
        url += protocol.getProtocol() + "://";
        return this;
    }

    @Override
    public GridUrlBuilder addSeleniumHubHost(String host)
    {
        url += host + ":";
        return this;
    }

    @Override
    public GridUrlBuilder addSeleniumHubPort(int port)
    {
        url += port;
        return this;
    }

    /**
     * Build the GRID url.
     */
    @Override
    public URL build() throws MalformedURLException
    {
        return new URL(url + "/wd/hub");
    }
}
