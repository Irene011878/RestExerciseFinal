package Api;

import SerializacionPayloads.SerandDesPetsApi.CategoryResponse;
import SerializacionPayloads.SerandDesPetsApi.PetResponse;
import SerializacionPayloads.SerandDesPetsApi.TagResponse;
import Utils.BaseClass;
import Utils.ExtentReportManager;
import Utils.PropertiesReader;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PetsApi extends BaseClass {

    ObjectMapper objectMapper = new ObjectMapper();


    @Test(priority = 3)
    public void getPetById() throws JsonProcessingException {

            ExtentReportManager.test = ExtentReportManager.extent.createTest("GET Pet by ID Test");

        RestAssured.baseURI= PropertiesReader.getBaseUri();
        Response response = given().pathParam("petId", 4191801)
                        .when().get("/pet/{petId}");

        PetResponse respuesta =  objectMapper.readValue(response.getBody().asString(), PetResponse.class);


        try {
            Assert.assertEquals((long) respuesta.getId(), 4191801);
            ExtentReportManager.test.log(Status.PASS, "The Pet ID is correct: 4191801");
            Assert.assertEquals(respuesta.getName(), "Oso");
            ExtentReportManager.test.log(Status.PASS, "The correct Pet name: Oso");
            System.out.println(respuesta.getStatus());
            ExtentReportManager.test.log(Status.INFO, "Pet Status: " + respuesta.getStatus());
        }catch (AssertionError e){
            ExtentReportManager.test.log(Status.FAIL, "Fail in  assertions: " + e.getMessage());
            throw e;
        }

    }

    @Test(priority = 4)
    public void getPetsByStatus() throws JsonProcessingException{
        ExtentReportManager.test = ExtentReportManager.extent.createTest("GET Pets by Status Test");

        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        Response response = given().queryParam("status", "available")
                       .when().get("/pet/findByStatus")
                       .then().statusCode(200).extract().response();
        System.out.println(response.prettyPrint());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().jsonPath().getString("[0].status"), "available");
        System.out.println(response.getBody().jsonPath().getString("[0].status"));


        ExtentReportManager.test.log(Status.PASS, "The status code is 200 OK");
        ExtentReportManager.test.log(Status.INFO, "First Pet Status: " + response.getBody().jsonPath().getString("[0].status"));



        TypeReference <List<PetResponse>> tiposMascotas = new TypeReference<List<PetResponse>>() {};
        List<PetResponse> listaMascotas = objectMapper.readValue(response.getBody().asString(), tiposMascotas);
        List<PetResponse> mascotasFiltradas = listaMascotas.stream()
                .filter(mascota -> mascota.getName() != null && mascota.getName().startsWith("Po"))
                .collect(Collectors.toList());

        ExtentReportManager.test.log(Status.INFO, "Number of filtered pets: " + mascotasFiltradas.size());


        System.out.println(mascotasFiltradas);

    }


    @Test(priority = 1)
    public void postCreateNewPet() throws JsonProcessingException{

        ExtentReportManager.test = ExtentReportManager.extent.createTest("POST Create New Pet Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        CategoryResponse category = new CategoryResponse(600L, "Yorki");
        TagResponse tags = new TagResponse(900L, "Cute");
        PetResponse mascota= new PetResponse(4191801, category, "Polito", Arrays.asList("YorkisCute.com"), Arrays.asList(tags), "available");

        String mascotaJson = objectMapper.writeValueAsString(mascota);
        System.out.println(mascota);
        Response response = given().contentType(ContentType.JSON).body(mascotaJson)
                .when().post("/pet");
        System.out.println(response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200);

        ExtentReportManager.test.log(Status.PASS, "Pet correctly created with status code response 200");
        ExtentReportManager.test.log(Status.INFO, "Reponse: " + response.getBody().asString());

    }

    @Test(priority = 2)
    public void putUpdatePet() throws JsonProcessingException{

        ExtentReportManager.test = ExtentReportManager.extent.createTest("PUT Update Pet Test");


        RestAssured.baseURI = PropertiesReader.getBaseUri();
        CategoryResponse category = new CategoryResponse(600L, "Pastor Aleman");
        TagResponse tags = new TagResponse(900L, "Guardian");
        PetResponse mascotaUpdated= new PetResponse(4191801, category, "Oso", Arrays.asList("PastoresGuardianes.com"), Arrays.asList(tags), "available");

        String mascotaUpadtedJson = objectMapper.writeValueAsString(mascotaUpdated);
        System.out.println(mascotaUpdated);
        Response response = given().contentType(ContentType.JSON).body(mascotaUpadtedJson).when().put("/pet");
        System.out.println(response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200);

        ExtentReportManager.test.log(Status.PASS, "Updated Pet correctly with status code 200");
        ExtentReportManager.test.log(Status.INFO, "Response: " + response.getBody().asString());

    }
    @Test(priority = 5)
    public void deletePet(){

        ExtentReportManager.test = ExtentReportManager.extent.createTest("DELETE Pet Test");

        RestAssured.baseURI = PropertiesReader.getBaseUri();
        Response response = given().pathParam("petId",4191801).when().delete("/pet/{petId}");

        Assert.assertEquals(response.getStatusCode(), 200);
        ExtentReportManager.test.log(Status.PASS, "Pet deleted correctly with status code 200");

    }

}
