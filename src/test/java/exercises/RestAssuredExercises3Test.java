package exercises;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class RestAssuredExercises3Test {

    private static RequestSpecification requestSpec;

    @BeforeAll
    public static void setUp() {

        createRequestSpecification();
        createResponseSpecification();
        getNinthDriverId();
    }

    static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("http://localhost").
                setPort(9876).
                setBasePath("/api/f1").
                build();
    }


    /*******************************************************
     * Create a static ResponseSpecification that checks whether:
     * - the response has statusCode 200
     * - the response contentType is JSON
     * - the value of MRData.CircuitTable.Circuits.circuitName[0]
     *   is equal to 'Albert Park Grand Prix Circuit'
     ******************************************************/

    private static ResponseSpecification responseSpec;


    static void createResponseSpecification(){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.JSON)
                .expectBody("MRData.CircuitTable.Circuits.circuitName[0]",is("Albert Park Grand Prix Circuit"));
        responseSpec = builder.build();
    }

    /*******************************************************
     * Retrieve the list of 2016 Formula 1 drivers and store
     * the driverId for the ninth mentioned driver in a
     * static String variable
     * Use /2016/drivers.json
     ******************************************************/

    private static String ninthDriverId;


    static void getNinthDriverId() {
        ninthDriverId =
                given().
                        spec(requestSpec).
                        when().get("/2016/drivers.json").getBody().jsonPath().getString("MRData.DriverTable.Drivers[8].driverId");
    }

    /*******************************************************
     * Retrieve the circuit data for the first race in 2014
     * Use the previously created ResponseSpecification to
     * execute the specified checks
     * Use /2014/1/circuits.json
     * Additionally, check that the circuit is located in Melbourne
     ******************************************************/

    @Test
    public void useResponseSpecification() {

        given().
                spec(requestSpec).
                when().
                then();
    }

    /*******************************************************
     * Retrieve the driver data for the ninth mentioned driver
     * Use the previously extracted driverId to do this
     * Use it as a path parameter to /drivers/<driverId>.json
     * Check that the driver is German
     ******************************************************/

    @Test
    public void useExtractedDriverId() {

        given().
                spec(requestSpec).
                pathParams("ninthDriverId",ninthDriverId).
                when().get("/drivers/{ninthDriverId}.json").
                then().log().all()
                .body("MRData.DriverTable.Drivers[0].nationality",is("German"));
    }
}