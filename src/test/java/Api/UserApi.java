package Api;

import SerializacionPayloads.SerAndDesUserApi.UserResponse;
import Utils.BaseClass;
import Utils.ExtentReportManager;
import Utils.SerializationManager;
import com.aventstack.extentreports.Status;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UserApi extends BaseClass {

    private static final Logger logger = LogManager.getLogger(UserApi.class);

    @Test(priority = 2, groups = {"regression"})
    public void getUserName() {


        response = request.pathParam("username", "Emma01")
                .get("/user/{username}");

        UserResponse userResponse = SerializationManager.deserialize(response.getBody().asString(), UserResponse.class);
        Assert.assertEquals(userResponse.getUsername(), "Emma01");
        Assert.assertEquals(userResponse.getId(), 833197);
        Assert.assertEquals(response.getStatusCode(),200);


    }

    @Test(priority = 1, groups = {"regression"})
    public void createUser() {

        String newUserJson = SerializationManager.readJsonFileAsString("src/test/resources/Data/createUser.json");
        logger.debug("New User JSON: " + newUserJson);
        response = request.contentType(ContentType.JSON).body(newUserJson).when().post("/user");
        Assert.assertEquals(response.getStatusCode(), 200);
        ExtentReportManager.test.log(Status.PASS, "User created successfully.");
    }

    @Test(priority = 3, groups = {"regression"})
    public void deleteUser() {

        response = request.pathParam("username", "Emma01").delete("/user/{username}");
        Assert.assertEquals(response.getStatusCode(), 200);
        ExtentReportManager.test.log(Status.PASS, "User deleted successfully.");

    }

    @Test(priority = 4, groups = {"smoke"})
    public void deleteUserNotFound() {

        response = request.pathParam("username", "NonExistentUser")
                .delete("/user/{username}");
        logger.warn("User not found. Response Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 404);
        ExtentReportManager.test.log(Status.PASS, "Correct status code 404 for deleting non-existent user.");


    }

    @Test(priority = 5, groups = {"smoke"})
    public void getUserNameInvalidFormat() {


        response = request.pathParam("username", "Emma!@#")
                .get("/user/{username}");
        logger.warn("Invalid username format. Response Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 404);
        ExtentReportManager.test.log(Status.PASS, "Correct status code 400 for invalid username format.");
        logger.info("Response body: " + response.getBody().asString());
    }
}
