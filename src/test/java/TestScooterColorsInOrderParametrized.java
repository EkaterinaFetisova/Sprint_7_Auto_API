import org.example.*;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import java.util.List;

@RunWith(Parameterized.class)
public class TestScooterColorsInOrderParametrized {
    Order order;
    List<String> color;
    int orderId;

    public TestScooterColorsInOrderParametrized(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] testScooterColorsData() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GRAY")},
                {List.of("BLACK", "GRAY")},
                {List.of("")}
        };
    }

    @Before
    public void createOrderInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;
    }

    @Test
    public void testScooterColorsInOrder() {
        order = new Order ("Naruto", "Uchiha","Konoha, 142 apt.", "4", "+7 800 355 35 35",
                4, "2020-06-06", "Saske, come back to Konoha",this.color);
        Response response = given()
                .header(Data.REQUEST_HEADER)
                .and()
                .body(order)
                .when()
                .post(OrderAPI.newOrderPath);
        orderId = response.jsonPath().getInt("track");
        response.then().assertThat().statusCode(SC_CREATED)
                .and().body("track",notNullValue());
        response =  given()
                .header(Data.REQUEST_HEADER)
                .when()
                .get(OrderAPI.getOrderByIdPath(orderId));
        response.then()
                .statusCode(SC_OK)
                .and()
                .body("order.color", equalTo(this.color));

    }
    @After
    public void deleteOrder(){
        try{
            given()
                    .header(Data.REQUEST_HEADER)
                    .and()
                    .when()
                    .post(OrderAPI.getFinishOrderPath(orderId));

        }catch (NullPointerException e){}

    }

}
