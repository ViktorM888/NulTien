package Utility;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log {
    
    
    private static Logger log = Logger.getLogger(Log.class.getName());
    
      
    public static void info(String message){
        //BasicConfigurator.configure();
       // log.getRootLogger().setLevel(Level.INFO);
        log.info(message);
    }
    
    
    public static void startTestCase(String sTestCaseName){

        Log.info("****************************************************************************************");
        Log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$                 "+sTestCaseName+ "       $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        Log.info("****************************************************************************************");
        

        }
  //This is to print log for the ending of the test case

    public static void endTestCase(String sTestCaseName){

           Log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D-" + "  " + sTestCaseName + "             XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

           Log.info("XXXXXXXXXXXXXXXXXXXXX");
           Log.info(".");
           }

           // Need to create these methods, so that they can be called  


    public static void warn(String message) {

       Log.warn(message);

           }

    public static void error(String message) {

       Log.error(message);

           }

    public static void fatal(String message) {

       Log.fatal(message);

           }

    public static void debug(String message) {

       Log.debug(message);

           }
}
