package cucumberTestRunner;
 
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;

@RunWith(CucumberRunner.class)
@CucumberOptions(
        format = {"pretty", "html:Results/ResultsHTML" },
        features = "Features/Core",
        glue = "stepDefinition"
)
public class TestRunner {
    public static void main(String[] args) {
        JUnitCore.main("cucumberTestRunner.TestRunner");
    }
}