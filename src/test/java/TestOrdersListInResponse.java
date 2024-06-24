import org.example.*;
import io.restassured.RestAssured;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class TestOrdersListInResponse {

    @Before
    public void createCourierInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;

    }
    @Test
    public void TestOrdersListInResponse(){
        Response response = given()
                .header(Data.REQUEST_HEADER)
                .and()
                .get(OrderAPI.getOrdersListAPIPath);
        response.then().assertThat().body("orders",notNullValue());
    }

}
