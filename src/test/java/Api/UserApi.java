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

import static io.restassured.RestAssured.given;

public class UserApi extends BaseClass {

    ObjectMapper objectMapper = new ObjectMapper();


    @Test(priority = 2, groups = {"regression"})
    public void getUserName() throws JsonProcessingException {





        RestAssured.baseURI= PropertiesReader.getBaseUri();
        Response response = given().pathParam("username", "Emma01")
                .when().get("/user/{username}");

        UserResponse userResponse = objectMapper.readValue(response.getBody().asString(), UserResponse.class);

        Assert.assertEquals(userResponse.getUsername(), "Emma01");
        Assert.assertEquals(userResponse.getId(), 833197);


        ExtentReportManager.test.log(Status.INFO, "Response Body: " + response.getBody().asString());
        ExtentReportManager.test.log(Status.PASS, "User name retrieved successfully.");


        System.out.println(userResponse.getUserStatus());

    }

    @Test(priority = 1, groups = {"regression"})
    public void createUser() throws JsonProcessingException{

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Create User Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        RestAssured.baseURI= PropertiesReader.getBaseUri();
        UserResponse newUser = new UserResponse(833197, "Emma01", "Emma", "Calvillo", "ec.techmahindra.com", "test4321", "8444104540", 1);
        String newUserJson = objectMapper.writeValueAsString(newUser);
        System.out.println(newUser);
        Response response = given().contentType(ContentType.JSON).body(newUserJson)
                .when().post("/user");

        System.out.println(response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

        ExtentReportManager.test.log(Status.INFO, "Response Body: " + response.getBody().asString());
        ExtentReportManager.test.log(Status.PASS, "User created successfully.");

    }

    @Test(priority = 3)
    public void deleteUser(){

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Delete User Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        Response response = given().pathParam("username","Emma01").when().delete("/user/{username}");

        System.out.println(response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 200);
        ExtentReportManager.test.log(Status.INFO, "Deleted user with username Emma01.");
        ExtentReportManager.test.log(Status.PASS, "User deleted successfully.");

    }

}
