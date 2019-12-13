package Utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.apache.commons.io.FileUtils;


public class Base {

    public static WebDriver driver = null;
    
    public static String compname = WebElementExtender.getComputerName();
    
    public static String domainName = System.getenv("USERDOMAIN");
            
    public static String url = Utility.ExcelConfig.ReadExcel("excelTestsSources/Configuration.xlsx", "Penta");
    
    public static String env = Utility.ExcelConfig.ReadExcel("excelTestsSources/Configuration.xlsx", "Environment");

    public static ArrayList<String> listForExecution = ExcelConfig.GetListOfTestForExecution("excelTestsSources/ExecutionConfiguration/" + compname + ".xlsx", "TestName", env);
    
    public static Boolean init = false;
 
    public static String username = ""; 
    
    //public static String browser = Utility.ExcelConfig.ReadExcel("excelTestsSources/Configuration.xlsx", "Browser");
    
    //global variables used for asserts
    public static String CurrentUserNr = "";
    public static String AllUsersNr = "";
    
    public Base() {

    }
    
    public static void AddUserToAllUsersNr (String userNr) {
        Base.AllUsersNr = userNr + ";";
    }
    
    public static void AssertEqualsWrapper(String message, String expected, String actual) {
        
        message = "All users Nr: " + AllUsersNr + " | " + "Current user Nr: " + CurrentUserNr + " | " + message;
        Assert.assertEquals(message, expected, actual);
    }
 
    public static void AssertEqualsWrapper(String message, boolean expected, boolean actual) {
        
        message = "All users Nr: " + AllUsersNr + " | " + "Current user Nr: " + CurrentUserNr + " | " + message;
        Assert.assertEquals(message, expected, actual);
    }
    
    public static void Start(final String Application) throws InterruptedException {
        if (init == false) {
            Log.info("Environment:" + " " + env);
            Log.info("Domain: " + domainName);
            

            DesiredCapabilities chromeCapabilities = new DesiredCapabilities();
            chromeCapabilities = chromeCapabilities.chrome();
                    
            ChromeOptions options = new ChromeOptions();
            File file = new File("libSources/WebDrivers/chromedriver.exe");
            System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
            chromeCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            //chromeCapabilities.setCapability("unexpectedAlertBehaviour", "accept"); //do not use this, it will close every popup, we do not want that
            options.setExperimentalOption("useAutomationExtension", false);
            options.addArguments("--touch-events=disabled");
            //options.addArguments("--no-sandbox"); //added to close all chrome webdrivers
            //driver.manage().deleteAllCookies(); //temp
            chromeCapabilities.setCapability(ChromeOptions.CAPABILITY, options); //this is added
            
            //Instantiating Chrome driver
            driver = new ChromeDriver(chromeCapabilities);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            //driver.manage().timeouts().setScriptTimeout(1, TimeUnit.SECONDS); //added, not used
            driver.manage().window().maximize();
         
         //-------------------------------------------
                //if ( (domainName.equals("UMOBI") && !compname.equals("cd0c0023"))  || domainName.equals("MOBI")) {
             
             final Thread thread1 = new Thread(){

                 public void run(){
                 //    driver.switchTo().alert().accept();
                     try {
                     } catch (Throwable e) {                         
                         e.printStackTrace();
                     }
                 }
             };

             final Thread thread2 = new Thread() {
                 public void run() {
                     try {
                         driver.get(Utility.ExcelConfig.ReadExcel("excelTestsSources/Configuration.xlsx", Application));
                         WebElementExtender.WaitToAlertDissapear(6000);
                         init = true;
                     } catch (Throwable e) {
                         e.printStackTrace();
                     }
                 }
             };
                                                
             thread1.start();
             thread2.start();
             
             thread1.join();
             thread2.join();

             Log.info("Certificate is not choosed properly in Browser");

           //-------------------------------     

            //quick fix for problem: after cert is choosed we sometimes get Error page that has in address bar the same address from config excel
            //and we need to refresh page to open it correctly
            //===============================
            Thread.sleep(5000);
            String addressFromExcel = Utility.ExcelConfig.ReadExcel("excelTestsSources/Configuration.xlsx", Application);
            String urlFromAddressBar = WebElementExtender.driver.getCurrentUrl();  //added
            if (addressFromExcel.equalsIgnoreCase(urlFromAddressBar)) {
                Log.info("----- Handling Siebel error with login - refreshing page ----");
                driver.navigate().refresh();
            }
        }
    }

    public static void StartWithCustomUrl(String url) {
        if (init == false) {
            File file = new File("libSources/WebDrivers/chromedriver.exe");
            System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
            DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            chromeCapabilities.setCapability(options.CAPABILITY, options);
            chromeCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            options.setExperimentalOption("useAutomationExtension", false);
            //options.addArguments("--touch-events=disabled");
            chromeCapabilities.setCapability(ChromeOptions.CAPABILITY, options); //this is added

            driver = new ChromeDriver(chromeCapabilities);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.get(url);
            init = true;
        }
    }
    
    public static void DeleteFile() throws InterruptedException{
        File file = new File("test\\" + env + ".txt");
        
     for (int i = 1; i <= 30; i = i + 1){
        try{
        file.delete();
        break;
        }
        catch (Exception e) {
           Thread.sleep(100); 
        }
     }
        Log.info("File " + env + ".txt is deleted" );
    }
    
    public static void DeleteOutputExcelFile() throws InterruptedException {
        File f = null;
        //deletes Output file
        
        try{
            f = new File("C:/TempUFTData/Output.xlsx");
            f.delete();
        }
        
        finally{   
        }
        
    }
    public static void WaitForOutputExcelFile(int timeOut) throws InterruptedException {
        File f = null;
        
        f = new File("C:/TempUFTData/Output.xlsx");
        for(int i=1; i<timeOut/1000; i++){
            if(f.exists()){Thread.sleep(1000);return;}
            Thread.sleep(1000);
        }
        
    }

    public void TakeScreenShot(String scenarioName, String Iteration, String Env) throws WebDriverException, InterruptedException {

        String strDate = new SimpleDateFormat("dd_MM_yyyy").format(Calendar.getInstance().getTime());

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(scrFile, new File("Screenshot/" + scenarioName + "_" + strDate + "_" + "Environment_" + Env + "_" + "Row_Nr_" + Iteration + ".jpg"));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public static long GetCurrentTimeInSeconds() throws InterruptedException {
        long timeMillis = System.currentTimeMillis();
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
        return timeSeconds;
    }

    public static boolean CheckIfTimeHasPassed(long a, long b, long timeout) throws InterruptedException {

        if (b-a>timeout) {
            return true;
        }
        else {
            return false;
        }
    }




}

