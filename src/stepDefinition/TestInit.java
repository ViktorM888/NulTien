package stepDefinition;

import Utility.ExcelConfig;
import Utility.ExcelDataForTests;
import Utility.WebElementExtender;
import cucumber.api.Scenario;
import cucumber.api.java.Before;

import java.util.HashMap;
import java.util.List;


public class TestInit {
    private static String scenarioName;
    private static String featureName;
    private static List<HashMap<String, String>> scenarioXlsData;

    @Before
    public void initXLS(Scenario scenario) {
        scenarioName = scenario.getName();
        featureName = ExcelConfig.ReadExcel("excelTestsSources/ExecutionConfiguration/" +
                WebElementExtender.getComputerName() + ".xlsx", "TestName", scenarioName, "Feature");

        setScenarioXlsData(ExcelDataForTests.getDataFromExcel("excelTestsSources/excelExamples/" +
                featureName + "/" + scenarioName + ".xlsx", scenarioName));
    }

    public static List<HashMap<String, String>> getScenarioXlsData() {
        return scenarioXlsData;
    }

    public static void setScenarioXlsData(List<HashMap<String, String>> xlsData) {
        scenarioXlsData = xlsData;
    }

    public static String getScenarioName() {
        return scenarioName;
    }

    public static String getFeatureName() {
        return featureName;
    }
}