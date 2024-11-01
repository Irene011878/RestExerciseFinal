package Api;

import SerializacionPayloads.SerAndDesOrdStoreApi.OrderResponse;
import Utils.BaseClass;
import Utils.ExtentReportManager;
import Utils.PropertiesReader;
import Utils.SerializationManager;
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

        String completedOrderJson = SerializationManager.readJsonFileAsString("src/test/resources/Data/storeOrder.json");
        logger.debug("Request Payload: " + completedOrderJson);
        response = request.contentType(ContentType.JSON).body(completedOrderJson)
                .post("/store/order");
        logger.info("Response Body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("Store order successfully created.");
        ExtentReportManager.test.log(Status.PASS, "Store order successfully created.");


    }

    @Test(priority = 2, groups = {"regression"})
    public void getOrder() throws JacksonException{

        response = request.pathParam("orderId", 8)
                           .get("/store/order/{orderId}");
        OrderResponse myOrder =  SerializationManager.deserialize(response.getBody().asString(), OrderResponse.class);
        Assert.assertEquals(myOrder.getId(), 8);
        Assert.assertEquals(myOrder.getPetId(), 4191801);
        ExtentReportManager.test.log(Status.PASS, "Order fetched successfully.");
    }


    @Test(priority = 3, groups = {"regression"})
    public void deleteOrder() {

        response = request.pathParam("orderId", 8).when().delete("/store/order/{orderId}");
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("Order deleted successfully.");
        ExtentReportManager.test.log(Status.INFO, "Deleted order with ID 8.");
        ExtentReportManager.test.log(Status.PASS, "Order deleted successfully.");

    }

    @Test(priority = 4, groups = {"smoke"})
    public void getOrderNotFound() throws JacksonException {

        response = request.pathParam("orderId", 999999)
                .get("/store/order/{orderId}");

        logger.warn("Order not found. Response Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 404);
        ExtentReportManager.test.log(Status.PASS, "Correct status code 404 for non-existent order");

    }


}
