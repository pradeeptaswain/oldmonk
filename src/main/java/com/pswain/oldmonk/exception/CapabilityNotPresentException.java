package com.pswain.oldmonk.exception;

/**
 * Throw this exception when the matching capability is not found.
 */
public class CapabilityNotPresentException extends Exception
{
    private static final long serialVersionUID = 1L;

    public CapabilityNotPresentException()
    {}

    public CapabilityNotPresentException(final String message)
    {
        super(message);
    }

    public CapabilityNotPresentException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
