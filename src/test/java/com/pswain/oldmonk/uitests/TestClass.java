package com.pswain.oldmonk.uitests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.pswain.oldmonk.core.BasePage;
import com.pswain.oldmonk.utils.TestDataProvider;

public class TestClass extends BaseTest
{
    private final Logger logger = LoggerFactory.getLogger(TestClass.class);

    @Test(groups = { "smoke" }, dataProvider = "testdataprovider", dataProviderClass = TestDataProvider.class)
    public void testMethod(String name, String desc, String teams) throws Exception
    {
        if (null == driver)
        {
            System.out.println("Driver is null!");
        }

        BasePage bPage = new BasePage(driver);
        bPage.click("mail_link");
        
        Thread.sleep(5000);
        bPage.click("mail_link");
    }
}
