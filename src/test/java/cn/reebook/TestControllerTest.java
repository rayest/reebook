package cn.reebook;

import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by lirui on 2017/4/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestControllerTest extends TestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void getTestContent() {
        given().
                contentType(ContentType.JSON)
                .when()
                .get("/test")
                .then()
                .statusCode(200)
                .body("description", equalTo("This is a text"));
    }
}
