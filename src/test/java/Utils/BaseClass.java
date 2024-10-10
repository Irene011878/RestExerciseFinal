package Utils;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseClass {

    @BeforeTest
    public void setUpReport() {

        ExtentReportManager.setUpReport();
    }

    @AfterTest
    public void tearDown() {

        ExtentReportManager.flushReport();
    }
}
