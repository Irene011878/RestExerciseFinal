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

public class StoreApi extends BaseClass {

    ObjectMapper objectMapper = new ObjectMapper();


    @Test(priority = 1, groups = {"regression"})
    public void storeOrder() throws JacksonException {

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Store Order Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        OrderResponse completedOrder = new OrderResponse(8, 4191801, 1, "2024-10-03T23:14:17.279Z", "placed", true);
        String completedOrderJson = objectMapper.writeValueAsString(completedOrder);
        System.out.println(completedOrder);
        Response response = given().contentType(ContentType.JSON).body(completedOrderJson)
                .when().post("/store/order");
        System.out.println(response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200);

        ExtentReportManager.test.log(Status.INFO, "Response Body: " + response.getBody().asString());
        ExtentReportManager.test.log(Status.PASS, "Store order successfully created.");


    }

    @Test(priority = 2, groups = {"regression"})
    public void getOrder() throws JacksonException{

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Get Order Test");


        RestAssured.baseURI= PropertiesReader.getBaseUri();
        Response response = given().pathParam("orderId", 8)
                           .when().get("/store/order/{orderId}");
        OrderResponse myOrder =  objectMapper.readValue(response.getBody().asString(), OrderResponse.class);


        Assert.assertEquals(myOrder.getId(), 8);
        Assert.assertEquals(myOrder.getPetId(), 4191801);
        System.out.println(myOrder.getStatus());

        ExtentReportManager.test.log(Status.INFO, "Response Body: " + response.getBody().asString());
        ExtentReportManager.test.log(Status.PASS, "Order fetched successfully.");
    }

    @Test(priority = 3, groups = {"regression"})
    public void deleteOrder() {

        ExtentReportManager.test = ExtentReportManager.extent.createTest("Delete Order Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        Response response = given().pathParam("orderId", 8).when().delete("/store/order/{orderId}");

        System.out.println(response.getStatusCode());

        Assert.assertEquals(response.getStatusCode(), 200);
        ExtentReportManager.test.log(Status.INFO, "Deleted order with ID 7.");
        ExtentReportManager.test.log(Status.PASS, "Order deleted successfully.");

    }

}
