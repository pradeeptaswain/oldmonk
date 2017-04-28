package io.xschema.oldmonk.builder;

/**
 * Protocol used while creating Selenium Grid Url. If Selenium GRID is locally
 * running, then the url may look like this: "http://127.0.0.1:4444/wd/hub". If
 * it's protected by secure ssl, protocol will be 'https', else it will be
 * 'http'.
 */
public enum Protocol
{
    HTTP("http"), HTTPS("https");

    private String protocol;

    private Protocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getProtocol()
    {
        return protocol;
    }
}
