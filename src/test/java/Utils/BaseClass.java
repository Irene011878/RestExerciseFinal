package Utils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.Method;
import static io.restassured.RestAssured.given;

    public class BaseClass {
        protected RequestSpecification request;
        public Response response;
        public static final Logger logger = LogManager.getLogger(BaseClass.class);

        @BeforeMethod
        public void setUp(Method method){
            logger.info("Starting Test: " + method.getName());
            ExtentReportManager.test = ExtentReportManager.extent.createTest("Test:" + method.getName());
            RestAssured.baseURI= PropertiesReader.getBaseUri();
            request = given();
        }
        @AfterMethod
        public void endTest(ITestResult result) {
            if (result.getStatus() == ITestResult.FAILURE) {
                ExtentReportManager.test.fail("Status code: " + response.statusCode());
                ExtentReportManager.test.fail("Response Body: " + response.getBody().asString());
                ExtentReportManager.test.fail("Test Failed");
                logger.info("Test Failed");
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                ExtentReportManager.test.pass("Status code: " + response.statusCode());
                ExtentReportManager.test.pass("Response Body: " + response.getBody().asString());
                ExtentReportManager.test.pass("Test Success");
                logger.info("Test Successs");
            } else if (result.getStatus() == ITestResult.SKIP) {
                ExtentReportManager.test.pass("Test Skipped");
                logger.info("Test Skipped");
            }
        }

        @BeforeSuite
        public void setUpReport() {
            logger.info("Initializing ExtentReport setup...");
            ExtentReportManager.setUpReport();
            logger.info("ExtentReport setup completed successfully.");
        }

        @AfterSuite
        public void tearDown() {
            logger.info("Finishing Report...");
            ExtentReportManager.flushReport();
            logger.info("Report created successfully.");
        }

    }
