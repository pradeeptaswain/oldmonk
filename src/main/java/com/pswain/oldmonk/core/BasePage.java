package com.pswain.oldmonk.core;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pswain.oldmonk.exception.InvalidLocatorStrategyException;
import com.pswain.oldmonk.exception.PropertyNotFoundException;
import com.pswain.oldmonk.utils.ObjectRepository;

/**
 * Wraps the raw selenium API. All page model classes should extend BasePage.
 * The page java files, instead of using selenium command directly, should use
 * the wrapped methods of BasePage instead.
 */
public class BasePage
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class.getName());

    private WebElement          element;
    private List<WebElement>    elements;

    /*
     * WebDriver instance is available to all page classes which extend
     * BasePage. Useful in some situations, where you need to directly use
     * driver instance, instead of using the wrapper methods.
     */
    protected WebDriver         driver;
    protected Properties        props;

    public BasePage(WebDriver driver) throws IOException
    {
        this.driver = driver;

        // Initialize the object repository.
        props = ObjectRepository.initialize();
    }

    public void click(String locator) throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        click(locator, null, false);
    }

    public void click(String locator, String replacement)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        click(locator, replacement, false);
    }

    public void click(String locator, boolean ignoreNoSuchElementException)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        click(locator, null, ignoreNoSuchElementException);
    }

    /**
     * Click on an element.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @param ignoreNoSuchElementException
     *            - if set to true, then no exceptions will be thrown when
     *            element is not found, Suitable for situations where you want
     *            the test to progress despite of the result that click action
     *            succeeded or not!
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public void click(String locator, String replacement, boolean ignoreNoSuchElementException)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        if (ignoreNoSuchElementException)
        {
            try
            {
                element = ElementFinder.findElement(driver, locator);
            } catch (NoSuchElementException ex)
            {
                return;
            }
        } else
        {
            element = ElementFinder.findElement(driver, locator);

            WebDriverWait wait = new WebDriverWait(driver, 15);
            wait.until(ExpectedConditions.elementToBeClickable(element));

            element.click();

            LOGGER.info("Successfully clicked on element '" + locator + "' with locator value '"
                    + props.getProperty(locator) + "'");
        }
    }

    public void type(String locator, String textToType)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        type(locator, null, textToType);
    }

    /**
     * Type text on on element.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @param textToType
     *            - text to type
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public void type(String locator, String replacement, String textToType)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        element = ElementFinder.findElement(driver, locator);

        element.sendKeys(textToType);
        LOGGER.info("Successfully typed text '" + textToType + "' on element '" + locator + "' with locator value '"
                + props.getProperty(locator) + "'");
    }

    public int getXpathCount(String locator) throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        return getXpathCount(locator, null);
    }

    /**
     * Get total number of elements matching a locator.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @return - total number of elements matching a locator
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public int getXpathCount(String locator, String replacement)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        elements = ElementFinder.findElements(driver, locator);

        int size = elements.size();

        LOGGER.info("Element size for element '" + locator + "' with locator value '" + props.getProperty(locator)
                + "' is " + size);

        return size;
    }

    public boolean isElementDisplayed(String locator) throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        return isElementDisplayed(locator, null);
    }

    /**
     * Return whether an element is displayed or not!
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @return - boolean value specifying whether element is displayed or not!
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public boolean isElementDisplayed(String locator, String replacement)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        element = ElementFinder.findElement(driver, locator);

        if (null == element)
        {
            return false;
        }

        boolean isElementDisplayed = element.isDisplayed();

        LOGGER.info("Display property for element '" + locator + "' with locator value '" + props.getProperty(locator)
                + "' is " + isElementDisplayed);

        return isElementDisplayed;
    }

    public void clear(String locator) throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        clear(locator, null);
    }

    /**
     * Clear an element.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public void clear(String locator, String replacement)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        element = ElementFinder.findElement(driver, locator);

        element.clear();

        LOGGER.info("Successfully cleared element '" + locator + "' with locator value '" + props.getProperty(locator)
                + "'");
    }

    public void executeJavaScript(String locator, String script)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        executeJavaScript(locator, null, script);
    }

    /**
     * Execute java script on element.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @param script
     *            - script to execute
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public void executeJavaScript(String locator, String replacement, String script)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        element = ElementFinder.findElement(driver, locator);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(script, element);
    }

    public void selectOptionByText(String locator, String optionText)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        selectOptionByText(locator, null, optionText);
    }

    /**
     * Select option by option text.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @param optionText
     *            - dropdown option text
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public void selectOptionByText(String locator, String replacement, String optionText)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        element = ElementFinder.findElement(driver, locator);

        Select dropdown = new Select(element);
        dropdown.selectByVisibleText(optionText);

        LOGGER.info("Successfully selected option '" + optionText + "' from element '" + locator
                + "' with locator value '" + props.getProperty(locator) + "'");
    }

    public void selectOptionByIndex(String locator, int optionIndex)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        selectOptionByIndex(locator, null, optionIndex);
    }

    /**
     * Select option by option index.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @param optionIndex
     *            - index of dropdown option
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public void selectOptionByIndex(String locator, String replacement, int optionIndex)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        element = ElementFinder.findElement(driver, locator);

        Select dropdown = new Select(element);
        dropdown.selectByIndex(optionIndex);

        LOGGER.info("Successfully selected option with index " + optionIndex + "' from element '" + locator
                + "' with locator value '" + props.getProperty(locator) + "'");
    }

    public void mouseOver(String locator) throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        mouseOver(locator, null);
    }

    /**
     * Mouse over on an element.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public void mouseOver(String locator, String replacement)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        element = ElementFinder.findElement(driver, locator);

        Actions action = new Actions(driver);
        action.moveToElement(element).click().perform();

        LOGGER.info("Successfully hovered on element '" + locator + "' with locator value '"
                + props.getProperty(locator) + "'");
    }

    public String getText(String locator) throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        return getText(locator, null);
    }

    /**
     * Get text from an element.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @return - element text
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public String getText(String locator, String replacement)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        element = ElementFinder.findElement(driver, locator);

        String text = element.getText();

        LOGGER.info("Text for element '" + locator + "' with locator value '" + props.getProperty(locator) + "' is "
                + text);

        return text;
    }

    public String getAttribute(String locator, String attr)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        return getAttribute(locator, null, attr);
    }

    /**
     * Get element attribute.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @param attr
     *            - element atrribute whose value is to needed
     * @return - element attribute
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public String getAttribute(String locator, String replacement, String attr)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        element = ElementFinder.findElement(driver, locator);

        String attribute = element.getAttribute(attr);

        LOGGER.info("Attribute value for '" + attr + "' of element '" + locator + "' with locator value '"
                + props.getProperty(locator) + "' is '" + attribute + "'");

        return attribute;
    }

    public String getValue(String locator) throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        return getValue(locator, null);
    }

    /**
     * Get value from a text field element.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @return - element value
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public String getValue(String locator, String replacement)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        element = ElementFinder.findElement(driver, locator);

        String value = element.getAttribute("value");

        LOGGER.info("Value for element '" + locator + "' with locator value '" + props.getProperty(locator) + "' is '"
                + value + "'");

        return value;
    }

    /**
     * Refresh the browser.
     */
    public void refresh()
    {
        driver.navigate().refresh();

        LOGGER.info("Successfully refreshed browser");
    }

    /**
     * Press the back button on browser.
     */
    public void pressBrowserBackButton()
    {
        driver.navigate().back();
        LOGGER.info("Successfully navigated back in browser");
    }

    /**
     * Press the forward button on browser.
     */
    public void pressBrowserForwardButton()
    {
        driver.navigate().forward();
        LOGGER.info("Successfully navigated forward in browser");
    }

    /**
     * Sleep for specified period of time measured in seconds.
     * 
     * @param timeInSeconds
     *            - sleep time in seconds
     */
    public void sleep(int timeInSeconds)
    {
        try
        {
            LOGGER.info("Sleeping for '" + timeInSeconds + "' seconds...");
            Thread.sleep(timeInSeconds);
        } catch (InterruptedException ex)
        {
            // Do-Nothing
        }
    }

    public void waitForElementToBePresent(String locator, int waitTimeInSeconds)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        waitForElementToBePresent(locator, null, waitTimeInSeconds);
    }

    /**
     * Wait for element to be visible.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @param waitTimeInSeconds
     *            - timeout period
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public void waitForElementToBePresent(String locator, String replacement, int waitTimeInSeconds)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        By by = ElementFinder.find(locator);

        WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));

        LOGGER.info("Successfully waited for element '" + locator + "' with locator value '"
                + props.getProperty(locator) + "'");
    }

    /**
     * Switch to a frame by name.
     * 
     * @param frameName
     *            - name of frame
     */
    public void switchToFrameByName(String frameName)
    {
        driver.switchTo().frame(frameName);
        LOGGER.info("Successfully switched to frame identified by name '" + frameName + "'");
    }

    /**
     * Switch to a frame by number.
     * 
     * @param frameNumber
     *            - frame number
     */
    public void switchToFrameByNumber(int frameNumber)
    {
        driver.switchTo().frame(frameNumber);
        LOGGER.info("Successfully switched to frame identified by number '" + frameNumber + "'");
    }

    public void switchToFrameByFrameElement(String locator)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        switchToFrameByFrameElement(locator, null);
    }

    /**
     * Switch to a frame identified by webdriver as web element.
     * 
     * @param locator
     *            - element locator
     * @param replacement
     *            - if element contains dynamic part, i.e. '$value' in locator
     *            part, then '$value' part will be replaced by replacement value
     * @throws PropertyNotFoundException
     *             - throw this exception when declared locator is not found in
     *             object repository
     * @throws InvalidLocatorStrategyException
     *             - throw this exception when locator strategy is wrong. Valid
     *             locator strategies are 'ID', 'XPATH', 'NAME', 'CSS_SELECTOR',
     *             'CLASS_NAME', 'LINK_TEXT', 'PARTIAL_LINK_TEXT' and 'TAG_NAME'
     */
    public void switchToFrameByFrameElement(String locator, String replacement)
            throws PropertyNotFoundException, InvalidLocatorStrategyException
    {
        if (replacement != null)
        {
            if (locator.contains("$value"))
            {
                locator = locator.replace("$value", replacement);
            }
        }

        element = ElementFinder.findElement(driver, locator);

        driver.switchTo().frame(element);
        LOGGER.info("Successfully switched to frame identified by element '" + props.getProperty(locator) + "'");
    }

    /**
     * Switch ro window by window name or window id.
     * 
     * @param windowName
     *            - name/id of window
     */
    public void switchToWindow(String windowName)
    {
        driver.switchTo().window(windowName);
        LOGGER.info("Successfully switched to window identified by name '" + windowName + "'");
    }

    /**
     * Switch to parent window.
     */
    public void switchToParentWindow()
    {
        driver.switchTo().defaultContent();
        LOGGER.info("Successfully switched to parent window");
    }

    /**
     * Get page source.
     * 
     * @return - page source
     */
    public String getPageSource()
    {
        return driver.getPageSource();
    }

    /**
     * Get page title.
     * 
     * @return - page title
     */
    public String getTitle()
    {
        String title = driver.getTitle();
        LOGGER.info("Current page title is '" + title + "'");

        return title;
    }

    /**
     * Get current page url.
     * 
     * @return - current url
     */
    public String getCurrentUrl()
    {
        String url = driver.getCurrentUrl();
        LOGGER.info("Current page url is '" + url + "'");

        return url;
    }

    /**
     * Get all window handles. Ideal for situations, where you have multiple
     * windows open and you want to switch to a specific window by name or
     * window handle.
     * 
     * @return - window handles
     */
    public Set<String> getAllWindowHandles()
    {
        return driver.getWindowHandles();
    }

    /**
     * Get current window handle.
     * 
     * @return - current window handle
     */
    public String getCurrentWindowHandle()
    {
        String currentWindowHandle = driver.getWindowHandle();
        LOGGER.info("Current window handle is '" + currentWindowHandle + "'");

        return currentWindowHandle;
    }

    /**
     * Open url.
     * 
     * @param url
     *            - url to open
     */
    public void openUrl(String url)
    {
        driver.get(url);
        LOGGER.info("Successfully opened url '" + url + "'");
    }
}
