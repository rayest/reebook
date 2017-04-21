package cn.reebook;

import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by lirui on 2017/4/20.
 */
@SpringBootTest
public class TestBase {

    @Before
    public void setUp() throws Exception {
        RestAssured.port = 8020;
    }
}
