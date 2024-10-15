package Utils;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseClass {

    public static final Logger logger = LogManager.getLogger(BaseClass.class);

    public BaseClass() {
        logger.info("Initializing ExtentReport setup from constructor...");
        ExtentReportManager.setUpReport();
    }

    @BeforeSuite
    public void setUpReport() {
        logger.info("Initializing ExtentReport setup...");
        ExtentReportManager.setUpReport();
        logger.info("ExtentReport setup completed successfully.");
    }


    @AfterSuite
    public void tearDown() {
        logger.info("Flushing ExtentReport...");
        ExtentReportManager.flushReport();
        logger.info("ExtentReport flushed successfully.");
    }
}
