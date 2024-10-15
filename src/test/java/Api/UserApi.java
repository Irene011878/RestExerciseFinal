package Api;

import SerializacionPayloads.SerAndDesUserApi.UserResponse;
import Utils.BaseClass;
import Utils.ExtentReportManager;
import Utils.PropertiesReader;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class UserApi extends BaseClass {

    private static final Logger logger = LogManager.getLogger(UserApi.class);

    ObjectMapper objectMapper = new ObjectMapper();


    @Test(priority = 2, groups = {"regression"})
    public void getUserName() throws JsonProcessingException {

        logger.info("Starting Get User Name Test");

        RestAssured.baseURI= PropertiesReader.getBaseUri();
        Response response = given().pathParam("username", "Emma01")
                .when().get("/user/{username}");

        logger.info("Response Body: " + response.getBody().asString());

        UserResponse userResponse = objectMapper.readValue(response.getBody().asString(), UserResponse.class);

        Assert.assertEquals(userResponse.getUsername(), "Emma01");
        Assert.assertEquals(userResponse.getId(), 833197);


        ExtentReportManager.test.log(Status.INFO, "Response Body: " + response.getBody().asString());
        ExtentReportManager.test.log(Status.PASS, "User name retrieved successfully.");

        logger.info("User Status: " + userResponse.getUserStatus());


        //System.out.println(userResponse.getUserStatus());

    }

    @Test(priority = 1, groups = {"regression"})
    public void createUser() throws JsonProcessingException{

        logger.info("Starting Create User Test");

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Create User Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        UserResponse newUser = new UserResponse(833197, "Emma01", "Emma", "Calvillo", "ec.techmahindra.com", "test4321", "8444104540", 1);
        String newUserJson = objectMapper.writeValueAsString(newUser);

        logger.debug("New User JSON: " + newUserJson);
        //System.out.println(newUser);
        Response response = given().contentType(ContentType.JSON).body(newUserJson)
                .when().post("/user");

        logger.info("Response Body: " + response.getBody().asString());
        //System.out.println(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        ExtentReportManager.test.log(Status.INFO, "Response Body: " + response.getBody().asString());
        ExtentReportManager.test.log(Status.PASS, "User created successfully.");

    }

    @Test(priority = 3, groups = {"regression"})
    public void deleteUser(){

        logger.info("Starting Delete User Test");

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Delete User Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        Response response = given().pathParam("username","Emma01").when().delete("/user/{username}");

        //System.out.println(response.getStatusCode());

        logger.info("Response Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);

        ExtentReportManager.test.log(Status.INFO, "Deleted user with username Emma01.");
        ExtentReportManager.test.log(Status.PASS, "User deleted successfully.");

    }

    @Test(priority = 4, groups = {"smoke"})
    public void deleteUserNotFound() {

        logger.warn("Starting Delete User Not Found Test");

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Delete User Not Found Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        Response response = given().pathParam("username", "NonExistentUser")
                .when().delete("/user/{username}");

        logger.warn("User not found. Response Status Code: " + response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 404); // Verificar que el código sea 404

        ExtentReportManager.test.log(Status.PASS, "Correct status code 404 for deleting non-existent user.");
        //System.out.println("Response body: " + response.getBody().asString());
        logger.info("Response body: " + response.getBody().asString());
    }

    @Test(priority = 5, groups = {"smoke"})
    public void getUserNameInvalidFormat() throws JsonProcessingException {

        logger.warn("Starting Get User with Invalid Username Format Test");

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Get User with Invalid Username Format Test");

        RestAssured.baseURI= PropertiesReader.getBaseUri();
        Response response = given().pathParam("username", "Emma!@#")
                .when().get("/user/{username}");

        logger.warn("Invalid username format. Response Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 404); // Verificar que el código sea 400
        ExtentReportManager.test.log(Status.PASS, "Correct status code 400 for invalid username format.");
        //System.out.println("Response body: " + response.getBody().asString());
        logger.info("Response body: " + response.getBody().asString());
    }
}
