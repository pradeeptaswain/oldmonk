package io.xschema.oldmonk.exception;

/**
 * Throw this exception when property is not found in properties file. For
 * example framework will throw exception when locator is used with
 * Selenium/WebDriver commands but it's not present in object repository.
 */
public class PropertyNotFoundException extends Exception
{
    private static final long serialVersionUID = 1L;

    public PropertyNotFoundException()
    {}

    public PropertyNotFoundException(final String message)
    {
        super(message);
    }

    public PropertyNotFoundException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
