package io.xschema.oldmonk.exception;

/**
 * Throw this exception when the locator strategies do not match with the
 * locator startegies provided by Selenium/WebDriver.
 */
public class InvalidLocatorStrategyException extends Exception
{
    private static final long serialVersionUID = 1L;

    public InvalidLocatorStrategyException()
    {}

    public InvalidLocatorStrategyException(final String message)
    {
        super(message);
    }

    public InvalidLocatorStrategyException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
