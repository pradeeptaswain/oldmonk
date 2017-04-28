package io.xschema.oldmonk.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * DataProvider class to provide test data to test classes.
 */
public class TestDataProvider extends DefaultHandler
{
    private static final Logger       LOGGER        = LoggerFactory.getLogger(TestDataProvider.class.getName());

    private static String             declaredClass;
    private static List<String>       testdataParams;

    private static List<List<String>> dataMap;

    private boolean                   isTestClass;
    private boolean                   isDataSet;
    private boolean                   isTestData;

    private StringBuilder             stringBuilder = new StringBuilder();

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
            throws SAXException
    {
        if (qName.equalsIgnoreCase("testclass"))
        {
            if (attributes.getValue("name").equalsIgnoreCase(declaredClass))
            {
                isTestClass = true;
                dataMap = new ArrayList<List<String>>();
            }
        }

        if (isTestClass && qName.equalsIgnoreCase("dataset"))
        {
            isDataSet = true;

            testdataParams = new ArrayList<String>();
        }

        if (!(qName.equalsIgnoreCase("testclass")) && !(qName.equalsIgnoreCase("dataset"))
                && !(qName.equalsIgnoreCase("testdatasuite")))
        {
            if (isTestClass && isDataSet)
            {
                isTestData = true;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (!(qName.equalsIgnoreCase("testclass")) && !(qName.equalsIgnoreCase("dataset"))
                && !(qName.equalsIgnoreCase("testdatasuite")) && isTestClass)
        {
            testdataParams.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }

        if (qName.equalsIgnoreCase("dataset") && isDataSet && isTestClass)
        {
            dataMap.add(testdataParams);
            isDataSet = false;
            testdataParams = null;
        }

        if (qName.equalsIgnoreCase("testclass") && isTestClass)
        {
            isTestClass = false;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException
    {
        if (isTestData)
        {
            if (stringBuilder != null)
            {
                for (int i = start; i < start + length; i++)
                {
                    stringBuilder.append(ch[i]);
                }
            }

            isTestData = false;
        }
    }

    /**
     * TestNG static data provider method. This method will parse the
     * testdataxml file, and supply the testdata to the test methods during
     * runtime.
     * 
     * @param context
     *            - TestNG test context
     * @param method
     *            - TestNG method
     * @return - 2-D Object array containing the test data
     * @throws SAXException
     *             - throw this exception, if test data xml file can not be
     *             parsed
     * @throws ParserConfigurationException
     *             - throw this exception, if exception occurs during
     *             configuring the parser
     * @throws IOException
     *             - throw this exception, if test data xml file is not found
     */
    @DataProvider(name = "testdataprovider")
    public synchronized static Object[][] createTestData(final ITestContext context, Method method)
            throws SAXException, ParserConfigurationException, IOException
    {
        declaredClass = method.getDeclaringClass().getName();

        LOGGER.info("Loading test data for class '" + declaredClass + "'");

        final SAXParserFactory factory = SAXParserFactory.newInstance();

        try
        {
            final SAXParser saxParser = factory.newSAXParser();

            saxParser.parse(context.getCurrentXmlTest().getParameter("testdataxml"), new TestDataProvider());
        } catch (IOException realCause)
        {
            throw new IOException("Unable to open file: " + context.getCurrentXmlTest().getParameter("testdataxml")
                    + "! Please check the file name.\n\n");
        } catch (SAXException realCause)
        {
            throw new SAXException(realCause);
        } catch (ParserConfigurationException realCause)
        {
            throw new ParserConfigurationException("Unable to parse xml file!");
        }

        if (dataMap == null)
        {
            throw new IllegalArgumentException(
                    "TestData Not Found For Class: " + declaredClass + "!" + " Please check the xml data file.");
        }

        Object[][] dataToReturn = new Object[dataMap.size()][];

        for (int i = 0; i < dataMap.size(); i++)
        {
            Object[] data = new Object[dataMap.get(i).size()];

            int count = 0;
            for (String param : dataMap.get(i))
            {
                data[count++] = param;
            }

            dataToReturn[i] = data;
        }

        return dataToReturn;
    }
}
