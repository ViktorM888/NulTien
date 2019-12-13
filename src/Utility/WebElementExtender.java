package Utility;

import java.awt.AWTException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class WebElementExtender extends Base {

    public WebElementExtender() {
        super();
    }

    public static void StartDriver(String Application, String UserName) throws AWTException, InterruptedException {
        Start(Application);
    }

    public static void CloseDriver() {
        Close();
    }

    public static void OpenURL(String url) {
        driver.navigate().to(url);
    }

    public static void ClickOnBackButton() {
        driver.navigate().back();
    }

    public static String getComputerName() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "Unknown Computer";
    }

    static String getErrorMessage = "";
    public static int passed = 0;
    public static int failed = 0;

    public static WebElement GetWebElementByXpath(String xPath) {

        WebElement element = null;
        for (int i = 1; i < 20; i = i + 1)
            try {
                Thread.sleep(1000);
                element = driver.findElement(By.xpath(xPath));
                i = 20;
            }

            catch (Exception e) {
                if (e.getMessage().contains("element is not attached")) {
                    System.out.print("WebElement not loaded in DOM. Try no: " + i + "/20");
                }
                System.out.print("Exception Text : " + e.toString());

                if (i == 19) {
                    System.out.print("WebElement not founded");
                    break;
                }
            }

        return element;

    }

    public static void SelectDropDownList(WebElement ddlElement, String value, String elementName) throws InterruptedException {

        WaitForElement(ddlElement, elementName);
        Select select = new Select(ddlElement);

        if (!value.contains("default:")) {
            select.selectByVisibleText(value);
            Log.info("Value" + " " + value + " " + "from Drop Down List " + " " + elementName + " " + "is selected.");
        }
    }

    public static void SelectDropDownListByIndex(WebElement ddlElement, int index, String elementName) throws InterruptedException {

        WaitForElement(ddlElement, elementName);
        Select select = new Select(ddlElement);

        //if(!value.contains("default:")){
        select.selectByIndex(index);
        index = index + 1;
        Log.info("Value" + " " + index + " " + "from Drop Down List " + " " + elementName + " " + "is selected.");
        //}
    }

    public static Boolean isValueExistInDropDownlist(WebElement ddlElement, String value) {
        Select select = new Select(ddlElement);
        Boolean exist = false;
        List<WebElement> allOptions = select.getOptions();

        for (WebElement i : allOptions) {

            if (i.getText().equals(value)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public static String SelectedValueInDDL(WebElement ddlelement) {
        Select select = new Select(ddlelement);
        return select.getFirstSelectedOption().getText();
    }

    public static String GetTextFromElement(WebElement element) throws InterruptedException {
        String a = "";
        
        WaitForElement(element, element.toString());
        a = element.getText();
        //Added if statement, to correct error with blank spaces
        if (a.equals(" ")) {
            a = "";
        }
        return a;
    }

    public static String GetValueFromElement(WebElement element) throws InterruptedException {
        String a = "";

        WaitForElement(element, element.toString());
        a = element.getAttribute("value");

        return a;
    }
    
    public static String GetTitleFromElement(WebElement element) throws InterruptedException {
        String a = "";
        
        WaitForElement(element, element.toString());
        a = element.getAttribute("title");
        
        return a;
    }
    
    public static String GetAttributeFromElement(WebElement element, String attribute) throws InterruptedException {
        String a = "";

        WaitForElement(element, element.toString());
        a = element.getAttribute(attribute);

        return a;
    }

    public static void SetText(WebElement element, String value, String elementName) throws InterruptedException {

        if (!value.contains("default:")) {
            WaitForElement(element, elementName);
            element.clear();
            element.sendKeys(value);
            Log.info("FIELD:" + " " + elementName + " is populated with Value: " + value);
        }
    }
    
    //Specific step that will enter text 2 times
    public static void SetTextTwoTimes(WebElement element, String value, String elementName) throws InterruptedException {
        if (!value.contains("default:")) {
            WaitForElement(element, elementName);
            element.clear();
            element.sendKeys(" ");
            element.clear();
            element.sendKeys(value);
            Log.info("FIELD:" + " " + elementName + " is populated with VALUE: " + value);
        }
    }

    public static void ClickOnElement(WebElement element) {
        element.click();
    }

    public static void ClickOnElementJS(WebElement element, String elementName) throws InterruptedException {
         
        WaitForElement(element, elementName);
 
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        
        //Log.info("Click on " + elementName);
    }

    public static void SetAttributeOnElementJS(WebElement element, String elementName, String attribute, String attrValue) throws InterruptedException {
        
        WaitForElement(element, elementName);
         
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('" + attribute + "', '" + attrValue + "');", element);
        
        Log.info("Setting attribute " + attribute + " to " + attrValue + " in HTML element " + elementName);
    }

    public static boolean ElementIsVisible(WebElement element) throws InterruptedException {
        Boolean a = true;

        try {
            element.isDisplayed();
            a = true;

        } catch (NoSuchElementException e) {

            Thread.sleep(500);

            getErrorMessage = e.getMessage().toString();
            a = false;

        }

        return a;
    }

    public static void WaitForElement(WebElement element, String elementName) throws InterruptedException {
        for (int i = 1; i <= 10; i = i + 1) {
            try {
                //Log.info("Line before isDisplayed, i = " + i); //temp
                element.isDisplayed();
                //Log.info("Line before break, i = " + i); //temp
                break;
            } catch (NoSuchElementException e) {
                Thread.sleep(500);
                //Log.info("Line after sleep in catch blok, i = " + i); //temp
                if (i == 10) {
                    getErrorMessage = e.getMessage().toString();
                    Log.info("Element " + elementName + " is not displayed!!!");
                    Log.info(getErrorMessage);
                }
            }
        }
    }
    
    public static void ShortWaitForElement(WebElement element, String elementName) throws InterruptedException {
        for (int i = 1; i <= 6; i = i + 1) {
            try {
                element.isDisplayed();
                break;
            } catch (NoSuchElementException e) {
                Thread.sleep(500);
            }
        }
    }

    public static Boolean BoolWaitForElement(WebElement element, String elementName) throws InterruptedException {
        Boolean a = true;
        for (int i = 1; i <= 15; i = i + 1) {
            try {
                element.isDisplayed();
                a = true;
                break;
            } catch (NoSuchElementException e) {
                Thread.sleep(500);

                if (i == 15) {
                    getErrorMessage = e.getMessage().toString();
                    Log.info("Element " + elementName + "is not displayed!!!");
                    Log.info(getErrorMessage);
                    a = false;
                }
            }
        }
        return a;
    }

    public static Boolean BoolWaitForElementWithTimeout(WebElement element, String elementName, int timeoutInSec) throws InterruptedException {
        Boolean a = true;
        int timeCounterInSecCorrection = (timeoutInSec) / 6;
        for (int i = 1; i <= timeCounterInSecCorrection; i = i + 1) {
            try {
                element.isDisplayed();
                a = true;
                break;
            } catch (NoSuchElementException e) {
                //Thread.sleep(100);

                if (i >= timeCounterInSecCorrection) {
                    getErrorMessage = e.getMessage().toString();
                    Log.info("Element " + elementName + "is not displayed!!!");
                    Log.info(getErrorMessage);
                    a = false;
                }
            }
        }
        return a;
    }

    public static String GetTextFromElementShortTimeout(WebElement element) throws InterruptedException {
        String a = "";
        ShortWaitForElement(element, element.toString());
        a = element.getText();
        return a;
    }

    public static boolean isAlertPresent() throws InterruptedException {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException Ex) {
            return false;
        }

    }

    public static boolean waitForAlertPresent() throws InterruptedException {
        boolean result = false;
        for (int i = 0; i < 20; i++) {
            try {
                driver.switchTo().alert();
                result = true;
                break;
            } catch (NoAlertPresentException Ex) {
                Thread.sleep(120);
                if (i == 19) {
                    result = false;
                }
            }
        }
        return result;
    }

    public static void WaitToAlertDissapear(int timeOut) throws InterruptedException {
       /*while (isAlertPresent()) {
            Thread.sleep(500);
        }
        */
        for(int i=1; i<timeOut/1000; i++){
            if(!isAlertPresent()){break;}
            Thread.sleep(1000);
        }
        Thread.sleep(3000); //waiting after certificate dialog is closed
    }

    public static void WriteTextInFile(String file, String value) throws IOException {

        String str = value;
        File newTextFile = new File(file);

        FileWriter fw = new FileWriter(newTextFile);
        // fw.write("\r\n");
        fw.write(str);
        fw.close();

    }

    public static boolean IsElementContainsAttribute(WebElement element, String elementDescription, String attribute) throws InterruptedException {

        WebElementExtender.WaitForElement(element, elementDescription);

        Boolean result = false;
        try {
            String value = element.getAttribute(attribute);
            if (value != null) {
                result = true;
            }
        } catch (Exception e) {
        }

        return result;
    }

    public static boolean CheckIfAttributeIsEqualToText(WebElement web, String attribute, String textRequired) throws InterruptedException {
        boolean result = false;
        String actualText = web.getAttribute(attribute);
        if (actualText.equals(textRequired)) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    public static void Close() {
        driver.quit();
        init = false;
    }

    //Select By Visible Text
    public static void SelectByVisibleText(String Applet, String Label) throws InterruptedException {
        WebElement element = driver.findElement(By.xpath("//*[@ot='" + Applet + "']/option[@un='" + Label + "']"));
        element.click();
        //Select se = new Select(element);
        //se.selectByVisibleText(Label);
    }

}
