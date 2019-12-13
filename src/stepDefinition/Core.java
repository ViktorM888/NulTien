package stepDefinition;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.InetAddress;
import java.net.MalformedURLException;


import Pages.GenericSteps;
import org.apache.log4j.xml.DOMConfigurator;

import org.junit.Assume;
import org.junit.AssumptionViolatedException;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;

import Pages.CommonPage;
import Utility.Base;
import Utility.ExcelConfig;
import Utility.ExcelDataForTests;
import Utility.Log;
import Utility.WebElementExtender;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;



public class Core {

    WebElementExtender extender = new WebElementExtender();
    Utility.ExcelConfig ec = new Utility.ExcelConfig();
    Utility.ExcelDataForTests excel = new Utility.ExcelDataForTests();

    boolean DebugMessages = false;
    boolean IsUserLoggedIn = false;
    boolean propagateProblem;
    boolean execute = true;
    int countPassed = 0;
    int countFailed = 0;
    public List<HashMap<String, String>> datamap;
    public List<HashMap<String, String>> datamapElan;
    static StringBuilder sb = new StringBuilder();
    static String _rowNumber = "";

    String AppUsed = "";
    String iteration = "";
    String scName = "";
    String certNameOfUserLoggedIn = "NoCertificate";
    String FeatureName = "";


    @Before
    public void before(Scenario scenario) throws MalformedURLException, AWTException, InterruptedException, FileNotFoundException {

        String a = scenario.getName();

        if (!WebElementExtender.listForExecution.contains(a)) {
            execute = false;
            Assume.assumeTrue(execute);
            throw new AssumptionViolatedException("Skip this test");
        } else {
            DOMConfigurator.configure("log4j.xml");
            Log.startTestCase(scenario.getName());
            scName = scenario.getName();

            FeatureName =
                    ExcelConfig.ReadExcel("excelTestsSources/ExecutionConfiguration/" + WebElementExtender.getComputerName()
                            + ".xlsx", "TestName", scName, "Feature");

            System.out.println(datamap);
        }
    }

    @After
    public void after(Scenario scenario) throws IOException, InterruptedException {

        Log.info("AFTER scenario is started");
        Log.info("Test scenario is in status " + scenario.getStatus());
        Log.info("=============");

        //-------------------------------------------------------
        //WebElementExtender.CloseAlertPopUpIfShownAndLogMessage();

        //4WebElementExtender.CloseAlertPopUpIfShownAndLogMessage();
        //-------------------------------------------------------

        if (scenario.isFailed() || scenario.getStatus().equalsIgnoreCase("undefined") || scenario.getStatus().equalsIgnoreCase("skipped")) {

            extender.TakeScreenShot(scenario.getName(), iteration, WebElementExtender.env);

            String strDate = new SimpleDateFormat("dd_MM_yyyy").format(Calendar.getInstance().getTime());

            ExcelConfig.WriteTestResult(scenario.getName(), "FAILED",
                    Utility.ExcelConfig.ReadExcel("excelTestsSources/Configuration.xlsx", "Environment"), iteration,
                    scenario.getName() + "_" + strDate + "_" + "Environment_" + WebElementExtender.env + "_" + "Row_Nr_"
                            + iteration + ".jpg");

            WebElementExtender.failed = WebElementExtender.failed + 1;
            Log.info("FAILED!");

        } else {
            if (execute) {
                ExcelConfig.WriteTestResult(scenario.getName(), "PASSED",
                        Utility.ExcelConfig.ReadExcel("excelTestsSources/Configuration.xlsx", "Environment"), iteration, "");
                WebElementExtender.passed = WebElementExtender.passed + 1;
                Log.info("PASSED!");
                //String computername = InetAddress.getLocalHost().getCanonicalHostName();
                //sb.append("machine: " + computername + "# tests:");
            }
        }

        String computername = InetAddress.getLocalHost().getCanonicalHostName();

        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("Environment: " + WebElementExtender.env + "; ");
        strBuilder.append("Passed:" + Integer.toString(WebElementExtender.passed) + "# ");
        strBuilder.append("Failed:" + Integer.toString(WebElementExtender.failed) + "! ");

        if (scenario.isFailed()) {
            if (WebElementExtender.failed == 1 && WebElementExtender.passed == 0) {
                sb.append("\r\n");
                sb.append("machine: " + computername + "# tests:");
            }
            //sb.append((scName + " iteration: " + iteration + ","));
            sb.append(("[*RED*]" + scName + " iteration: " + iteration + ","));
        }
        //adding lines for html to display link to environment even if the test is passed
        else {
            if (WebElementExtender.passed == 1 && WebElementExtender.failed == 0) {
                sb.append("\r\n");
                sb.append("machine: " + computername + "# tests:");
                //sb.append((scName + " iteration: " + iteration + ","));
                sb.append(("[*GREEN*]" + scName + " iteration: " + iteration + ","));
            }
            if (WebElementExtender.passed >= 2 /*&& WebElementExtender.failed == 0*/) {
                //sb.append((scName + " iteration: " + iteration + ","));
                sb.append(("[*GREEN*]" + scName + " iteration: " + iteration + ","));
            }
            //sb.append((scName + " iteration: " + iteration + ","));
        }

        WebElementExtender.WriteTextInFile("Results/" + "_" + WebElementExtender.env + "_" + WebElementExtender.compname + ".txt",
                strBuilder.toString() + sb.toString());

        if (WebElementExtender.listForExecution.contains(scenario.getName())) {

            Log.info("Failed tests: " + Integer.toString(WebElementExtender.failed));
            Log.info("Passed tests: " + Integer.toString(WebElementExtender.passed));

            WebElementExtender.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); //this is added

            try {
                WebElementExtender.CloseDriver();
                Log.info("WebDriver is closed sucessfully");
            } catch (Exception e) {
                Log.info("WebDriver is not closed");
            }
        }

        if (WebElementExtender.listForExecution.contains(scenario.getName())) {
            Log.endTestCase(scenario.getName());
        }

    }

    //Step used only for debug
    @Then("^Step used only for debug$")
    public void step_used_only_for_debug() throws Throwable {


        //GenericVars.SetGlobalKeyValueData("Var1", "1");
        //LP_OrdinalTileNumber = "1";
        //Geschaftsnummer = "G-2900-0841";
        //String storedValue = GenericVars.GetGlobalKeyValueData("Var1");
        //Log.info("Var1:" + storedValue);
    }

    @Then("^Log message \"([^\"]*)\"$")
    public void log_message(String message) throws Throwable {

        Log.info(message);
    }


    @Given("^Delete file$")
    public void delete_file() throws Throwable {
        //WebElementExtender.DeleteFile();            

    }

    public void Log_iteration_number_method(String arg1) throws Throwable {
        iteration = arg1;
        Log.info("Iteration: " + iteration);
    }

    @Given("^Log iteration number \"([^\"]*)\"$")
    public void Log_iteration_number(String arg1) throws Throwable {
        Log_iteration_number_method(arg1);
    }

    //@description Start InternetExplorer and open specific URL and choose certificate that is selected in excel. @ExcelFields: Application, certName
    @Given("^Go to URL \"([^\"]*)\"$")
    public void go_to_URL(String arg1) throws Throwable {
        String application = getDataFromFile(arg1, "Application");

        go_to_URL_method(application);
    }

    public void go_to_URL_method(String application) throws Throwable {

        //String application = getDataFromFile(arg1, "Application");
        //String userNr = getDataFromFile(arg1, "certName");


        //========= this segment moved to separate method =============
        //iteration = arg1;
        //Log.info("Iteration: " + iteration);
        //=============================================================

        Log.info("Logging in user");
        //------------------------------------------------------
        //Base.CurrentUserNr = userNr;
        Base.AddUserToAllUsersNr(Base.CurrentUserNr);
        AppUsed = application;
        //------------------------------------------------------

        WebElementExtender.Start(application);
        //certNameOfUserLoggedIn = userNr;
        IsUserLoggedIn = true;
    }

    public String getDataFromFile(String arg1, String columnName) {

        datamap =
                ExcelDataForTests.getDataFromExcel("excelTestsSources/excelExamples/" + FeatureName + "/" + scName + ".xlsx",
                        scName);

        int index = Integer.parseInt(arg1) - 1;
        /*String myData = datamap.get(index).get(columnName);
        return myData;*/

        String myData = "";
        try {
            //if index does not exist "get(columnName)" coud not be executed and we will get into catch block
            //if index exist but columnName does not exist, we will get myData=null error and we will not get into catch block
            myData = datamap.get(index).get(columnName);

        } catch (Exception e) {
            Log.info("Excel row does not exist!!!");
            Base.AssertEqualsWrapper("Excel row does not exist", true, false);
        }

        return myData;
    }

    private String getDataFromFileTest(String arg1, String columnName, String form) throws IOException {

        datamap =
                ExcelDataForTests.getDataFromExcel("excelTestsSources/excelExamples/" + FeatureName + "/" + scName + ".xlsx"
                        , form);

        int index = Integer.parseInt(arg1) - 1;
        String myData = datamap.get(index).get(columnName);
        return myData;
    }

    //Enter First Name from excel credentials
    @Then("^Enter First Name using \"([^\"]*)\" from excel \"([^\"]*)\"$")
    public void Enter_Name_Excel(String firstname, String arg1) throws Throwable {
        GenericSteps gs = new GenericSteps();
        String name = getDataFromFile(arg1, firstname);
        gs.Name(name);
    }

    //Enter Message from excel credentials
    @Then("^Enter Message using \"([^\"]*)\" from excel \"([^\"]*)\"$")
    public void EnterMessage(String message, String arg1) throws Throwable {
        GenericSteps gs = new GenericSteps();
        String letter = getDataFromFile(arg1, message);
        gs.Message(letter);
    }

    //Click on Submit Button
    @Then("^Click on Submit Button$")
    public void Submit_Button() throws Throwable {
        GenericSteps gs = new GenericSteps();
        gs.SubmitButton();
    }

    //Enter Sum from excel credentials
    @Then("^Enter Sum using \"([^\"]*)\" from excel \"([^\"]*)\"$")
    public void EnterSum(String sum, String arg1) throws Throwable {
        GenericSteps gs = new GenericSteps();
        String x = getDataFromFile(arg1, sum);
        gs.Sum(x);
    }

    //Check numbers have changed
    @Then("^Check numbers have changed$")
    public void CheckNumbers() throws Throwable {
        GenericSteps gs = new GenericSteps();
        gs.ConfirmNumbersChanged();
    }

    //Get sum of both numbers
    @Then("Get Sum of Both numbers$")
    public void Sum() throws Throwable {
        GenericSteps gs = new GenericSteps();
        gs.Addition();
    }

    //Check message has changed
    @Then("^Check message$")
    public void CheckMessage() throws Throwable {
        GenericSteps gs = new GenericSteps();
        gs.CheckMessage();
    }


}