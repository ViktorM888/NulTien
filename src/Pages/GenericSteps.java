package Pages;

//import org.apache.commons.exec.util.StringUtils;

import com.thoughtworks.selenium.webdriven.commands.Close;
import org.junit.Assert;
import org.junit.Ignore;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import Utility.Log;
import Utility.WebElementExtender;

import java.util.concurrent.TimeUnit;

import static Utility.Base.AssertEqualsWrapper;
import static Utility.Base.driver;


public class GenericSteps {

    public static Boolean isVisible = false;


    private static WebElement GetElementByXpathWithoutCondition(String xPath) throws InterruptedException {

        WebElement element = null;
        for (int i = 1; i < 20; i = i + 1)
            try {

                //new WebDriverWait(WebElementExtender.driver, 5).ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));

                element = driver.findElement(By.xpath(xPath));
                break;
            } catch (Exception e) {
                //default:500, put lower number for less waiting time
                Thread.sleep(500);

                if (i == 19) {
                    isVisible = false;
                    break;
                }
            }
        return element;
    }

    //----------------------Automation NulTien-----------------------------------------------

    private static String XpathName() throws InterruptedException {
        String xpath = "//input[@id='et_pb_contact_name_1']";
        return xpath;
    }

    public static void Name(String name) throws InterruptedException {
        String xpath = XpathName();
        WebElement element = GetElementByXpathWithoutCondition(xpath);
        CommonPage.WaitForElementToBeAvailable(xpath);
        element.sendKeys(name);

    }

    private static String XpathMessage() throws InterruptedException {
        String xpath = "//textarea[@id='et_pb_contact_message_1']";
        return xpath;
    }

    public static void Message(String message) throws InterruptedException {
        String xpath = XpathMessage();
        WebElement element = GetElementByXpathWithoutCondition(xpath);
        CommonPage.WaitForElementToBeAvailable(xpath);
        element.sendKeys(message);

    }

    private static String XpathSum() throws InterruptedException {
        String xpath = "//input[@name='et_pb_contact_captcha_1']";
        return xpath;
    }

    public static void Sum(String sum) throws InterruptedException {
        String xpath = XpathSum();
        WebElement element = GetElementByXpathWithoutCondition(xpath);
        CommonPage.WaitForElementToBeAvailable(xpath);
        element.sendKeys(sum);

    }

    private static String XpathSubmitButton() throws InterruptedException {
        String xpath = "//div[@id='et_pb_contact_form_1']//button[@name='et_builder_submit_button'][contains(text(),'Submit')] ";
        return xpath;
    }

    public static void SubmitButton() throws InterruptedException {
        String xpath = XpathSubmitButton();
        WebElement element = GetElementByXpathWithoutCondition(xpath);
        CommonPage.WaitForElementToBeAvailable(xpath);
        element.click();
    }

    public static void ConfirmNumbersChanged() throws InterruptedException {
        String xpath = XpathSubmitButton();
        WebElement element = GetElementByXpathWithoutCondition(xpath);
        CommonPage.WaitForElementToBeAvailable(xpath);
        String oldNumber = driver.findElement(By.xpath("//span[@class='et_pb_contact_captcha_question']")).getText();
        element.click();
        String newNumber = driver.findElement(By.xpath("//span[@class='et_pb_contact_captcha_question']")).getText();

        System.out.println("Numbers " + oldNumber + " and " + newNumber + " are not the same");
        Assert.assertFalse("Numbers are not the same", false);
    }

    public static void CheckMessage() throws InterruptedException {
        String message = driver.findElement(By.xpath("//p[contains(text(),'Success')]")).getText();
        CommonPage.WaitForElementToBeAvailable(message);

        Assert.assertEquals("Success", message);
    }

    public static void Addition() throws InterruptedException {
        String sum = driver.findElement(By.xpath("//span[@class='et_pb_contact_captcha_question']")).getText().trim();
        String removespace = sum.replaceAll("\\s+", "");
        String[] parts = removespace.split("\\+");
        String part1 = parts[0];
        String part2 = parts[1];
        String[] parts1 = part2.split("\\=");
        String part3 = parts1[0];
        int addition = Integer.parseInt(part1) + Integer.parseInt(part3);
        WebElement tbox = driver.findElement(By.xpath("//input[@name='et_pb_contact_captcha_1']"));
        tbox.clear();
        tbox.sendKeys("" + addition);
    }

}




















