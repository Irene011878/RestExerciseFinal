package Api;

import SerializacionPayloads.SerAndDesOrdStoreApi.OrderResponse;
import Utils.BaseClass;
import Utils.ExtentReportManager;
import Utils.PropertiesReader;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreApi extends BaseClass {

    private static final Logger logger = LogManager.getLogger(StoreApi.class);

    ObjectMapper objectMapper = new ObjectMapper();


    @Test(priority = 1, groups = {"regression"})
    public void storeOrder() throws JacksonException {

        logger.info("Starting Store Order Test");

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Store Order Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        OrderResponse completedOrder = new OrderResponse(8, 4191801, 1, "2024-10-03T23:14:17.279Z", "placed", true);
        String completedOrderJson = objectMapper.writeValueAsString(completedOrder);


        logger.debug("Request Payload: " + completedOrderJson);

        Response response = given().contentType(ContentType.JSON).body(completedOrderJson)
                .when().post("/store/order");

        logger.info("Response Body: " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("Store order successfully created.");

        ExtentReportManager.test.log(Status.INFO, "Response Body: " + response.getBody().asString());
        ExtentReportManager.test.log(Status.PASS, "Store order successfully created.");


    }

    @Test(priority = 2, groups = {"regression"})
    public void getOrder() throws JacksonException{

        logger.info("Starting Get Order Test");

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Get Order Test");


        RestAssured.baseURI= PropertiesReader.getBaseUri();
        Response response = given().pathParam("orderId", 8)
                           .when().get("/store/order/{orderId}");

        logger.info("Response Body: " + response.getBody().asString());

        OrderResponse myOrder =  objectMapper.readValue(response.getBody().asString(), OrderResponse.class);
        Assert.assertEquals(myOrder.getId(), 8);
        Assert.assertEquals(myOrder.getPetId(), 4191801);

        logger.info("Order fetched successfully.");

        ExtentReportManager.test.log(Status.INFO, "Response Body: " + response.getBody().asString());
        ExtentReportManager.test.log(Status.PASS, "Order fetched successfully.");
    }

    @Test(priority = 3, groups = {"regression"})
    public void deleteOrder() {

        logger.info("Starting Delete Order Test");

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Delete Order Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        Response response = given().pathParam("orderId", 8).when().delete("/store/order/{orderId}");

        logger.info("Response Status Code: " + response.getStatusCode());



        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("Order deleted successfully.");

        ExtentReportManager.test.log(Status.INFO, "Deleted order with ID 7.");
        ExtentReportManager.test.log(Status.PASS, "Order deleted successfully.");

    }

    @Test(priority = 5, groups = {"smoke"})
    public void getOrderNotFound() throws JacksonException {

        logger.warn("Starting Get Order Not Found Test");

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Get Order Not Found Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        Response response = given().pathParam("orderId", 999999)
                .when().get("/store/order/{orderId}");

        logger.warn("Order not found. Response Status Code: " + response.getStatusCode());


        Assert.assertEquals(response.getStatusCode(), 404);
        logger.info("Correct status code 404 for non-existent order.");
        ExtentReportManager.test.log(Status.PASS, "Correct status code 404 for non-existent order");

        logger.info("Response body: " + response.getBody().asString());
    }


}
