package io.xschema.oldmonk.profile;

/**
 * Create a browser profile. For firefox it's Firefox Profile, For Chrome, it's
 * Chrome Options and For IE, it's IE Desired Capabilities. As creating browser
 * profile is different for different browsers, this class aims at providing a
 * common way to build browser profile.
 */
public abstract class BrowserProfile
{
    // Create browser profile
    public abstract Object createProfile();
}
