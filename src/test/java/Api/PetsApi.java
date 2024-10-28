package Api;


import SerializacionPayloads.SerandDesPetsApi.PetResponse;
import Utils.BaseClass;
import Utils.ExtentReportManager;
import Utils.SerializationManager;
import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.stream.Collectors;


public class PetsApi extends BaseClass {

    private static final Logger logger = LogManager.getLogger(PetsApi.class); // Añadir el logger


    @Test(priority = 3, groups = {"regression"})
    public void getPetById() {

        response = request.pathParam("petId", 4191801)
                .get("/pet/{petId}");
        logger.debug("Response status code: " + response.getStatusCode());
        logger.debug("Response body: " + response.getBody().asString());
        PetResponse respuesta = SerializationManager.deserialize(response.getBody().asString(), PetResponse.class);


        Assert.assertEquals((long) respuesta.getId(), 4191801);
        ExtentReportManager.test.log(Status.PASS, "The Pet ID is correct: 4191801");
        Assert.assertEquals(respuesta.getName(), "Oso");
        ExtentReportManager.test.log(Status.PASS, "The correct Pet name: Oso");

        ExtentReportManager.test.log(Status.INFO, "Pet Status: " + respuesta.getStatus());

    }

    @Test(priority = 4, groups = {"regression"})
    public void getPetsByStatus() throws JsonProcessingException {

        response = request.queryParam("status", "available")
                .get("/pet/findByStatus")
                .then().statusCode(200).extract().response();

        logger.debug("Response body: " + response.prettyPrint());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getBody().jsonPath().getString("[0].status"), "available");
        ExtentReportManager.test.log(Status.PASS, "The status code is 200 OK");
        ExtentReportManager.test.log(Status.INFO, "First Pet Status: " + response.getBody().jsonPath().getString("[0].status"));
        logger.info("Filtering pets whose name starts with 'Po'");
        TypeReference<List<PetResponse>> tiposMascotas = new TypeReference<List<PetResponse>>() {
        };
        List<PetResponse> listaMascotas = SerializationManager.deserializeList2(response.getBody().asString(), new TypeReference<List<PetResponse>>() {});
        List<PetResponse> mascotasFiltradas = listaMascotas.stream()
                .filter(mascota -> mascota.getName() != null && mascota.getName().startsWith("Po"))
                .collect(Collectors.toList());

        ExtentReportManager.test.log(Status.INFO, "Number of filtered pets: " + mascotasFiltradas.size());
        logger.debug("Filtered pets: " + mascotasFiltradas);
    }


    @Test(priority = 1, groups = {"regression"})
    public void postCreateNewPet()  {

        String mascotaJson = SerializationManager.readJsonFileAsString("src/test/resources/Data/createPet.json");
        response = request.contentType(ContentType.JSON).body(mascotaJson)
                .post("/pet");
        logger.debug("Response body: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);

    }

    @Test(priority = 2, groups = {"regression"})
    public void putUpdatePet() {


        String mascotaUpadtedJson = SerializationManager.readJsonFileAsString("src/test/resources/Data/updatePet.json");
        logger.debug("Request body: " + mascotaUpadtedJson);
        response = request.contentType(ContentType.JSON).body(mascotaUpadtedJson).put("/pet");
        Assert.assertEquals(response.getStatusCode(), 200);

    }

    @Test(priority = 5, groups = {"regression"})
    public void deletePet() {

        response = request.pathParam("petId", 4191801).when().delete("/pet/{petId}");
        logger.debug("Response status code: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);

    }


    @Test(priority = 6, groups = {"smoke"})
    public void getPetByIdNotFound()  {

        response = request.pathParam("petId", 99999999)
                .get("/pet/{petId}");
        Assert.assertEquals(response.getStatusCode(), 404);
        ExtentReportManager.test.log(Status.PASS, "Correct status code 404 when pet not found");

    }


    @Test(priority = 7)
    public void deletePetNotFound() {

        logger.info("Starting test: DELETE non-existent Pet");
        response = request.pathParam("petId", 99999999)
                .delete("/pet/{petId}");

        Assert.assertEquals(response.getStatusCode(), 404);
        ExtentReportManager.test.log(Status.PASS, "Correct status code 404 for deleting non-existent pet");

    }


}
