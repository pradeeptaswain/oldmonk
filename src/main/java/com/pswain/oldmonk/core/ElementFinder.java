package com.pswain.oldmonk.core;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.pswain.oldmonk.exception.InvalidLocatorStrategyException;
import com.pswain.oldmonk.exception.PropertyNotFoundException;
import com.pswain.oldmonk.utils.ObjectRepository;

/**
 * Find WebElement or List of WebElements identified through the element
 * locator.
 */
public class ElementFinder
{
    static Properties props;

    static
    {
        try
        {
            props = ObjectRepository.initialize();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Return a single element identified by the locator.
     * 
     * @param driver
     *            - WebDriver instance
     * @param locator
     *            - locator name in object repository
     * @return - WebElement
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public static WebElement findElement(WebDriver driver, String locator)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        By by = find(locator);

        return driver.findElement(by);
    }

    /**
     * Return a list of element identified by the locator.
     * 
     * @param driver
     *            - WebDriver instance
     * @param locator
     *            - locator name in object repository
     * @return - List<WebElement>
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public static List<WebElement> findElements(WebDriver driver, String locator)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        By by = find(locator);

        return driver.findElements(by);
    }

    /**
     * Return a By instance, which is used by findElement()/findElements()
     * method to retrieve the element(s). The locators in object repository are
     * specified as locator_name=LocatorStrategy,ActualLocatorValue, where
     * LocatorStrategy can be 'ID', 'NAME',
     * 'CSS_SELECTOR','XPATH','LINK_TEXT','PARTIAL_LINK_TEXT', 'CLASS_NAME',
     * 'TAG_NAME'. Ex. mail_link=XPATH,//a[text()='Mail'].
     * 
     * @param locator
     *            - locator name in object repository
     * @return - By instance
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public static By find(String locator) throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        locator = props.getProperty(locator);

        if (locator.isEmpty() || locator == null)
        {
            throw new PropertyNotFoundException("Locator value can not be null or empty!");
        }

        LocatorStrategy strategy = LocatorStrategy.valueOf(locator.split(",")[0]);
        String actualLocator = locator.split(",")[1];

        By by = null;

        switch (strategy)
        {
            case ID:
                by = By.id(actualLocator);
                break;
            case XPATH:
                by = By.xpath(actualLocator);
                break;
            case NAME:
                by = By.name(actualLocator);
                break;
            case TAG_NAME:
                by = By.tagName(actualLocator);
                break;
            case CSS_SELECTOR:
                by = By.cssSelector(actualLocator);
                break;
            case CLASS_NAME:
                by = By.className(actualLocator);
                break;
            case LINK_TEXT:
                by = By.linkText(actualLocator);
                break;
            case PARTIAL_LINK_TEXT:
                by = By.partialLinkText(actualLocator);
                break;
            default:
                throw new InvalidLocatorStrategyException("Unknown locator strategy '" + strategy + "'");
        }

        return by;
    }
}

/**
 * Enum representing all possible locator strategies for WebDriver.
 */
enum LocatorStrategy
{
    ID, XPATH, CSS_SELECTOR, TAG_NAME, NAME, CLASS_NAME, LINK_TEXT, PARTIAL_LINK_TEXT
}
