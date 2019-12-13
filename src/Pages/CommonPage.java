package Pages;

import Utility.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utility.WebElementExtender;

public class CommonPage {


    public static String WaitForElementToBeAvailable(String xpath) throws InterruptedException {
        try {
            WebDriverWait newq = new WebDriverWait(WebElementExtender.driver, 2);
            newq.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
            Thread.sleep(1000);
            return null;
        }
        catch (Exception e)
        {
            Thread.sleep(1000);
            return "error";
        }
    }

    //Method for waiting and chcking if page is fully loaded
    public static void WaitForPageToLoad() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor)WebElementExtender.driver;

        for (int i = 1; 1 <= 100; i++){
            String status = (String)js.executeScript("return document.readyState");

            if (status.equals("complete")){
                Log.info("Page loading process is: " + status);
                break;
            }
            else{
                Log.info("Page loading process is: " + status);
                Thread.sleep(500);
            }
        }
    }
}
