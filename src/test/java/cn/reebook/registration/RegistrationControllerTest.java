package cn.reebook.registration;

import cn.reebook.TestBase;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by lirui on 2017/4/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class RegistrationControllerTest extends TestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void registerNewUser_shouldReturn201WhenRegisterSuccessfully() throws Exception {
        String password = "foo";
        // String encryptPassword = MD5Service.encrypt(password);
        JSONObject body = new JSONObject();
        body.put("userName", "someone");
        body.put("email", "someone@qq.com");
        body.put("password", password);
        given().body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("/registration")
                .then()
                .statusCode(201)
                .body("userName", equalTo("someone"))
                .body("id", equalTo("fooId"))
                .body("email", equalTo("someone@qq.com"))
                .body("_link.login.href", endsWith("/login"));
    }
}
