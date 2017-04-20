package cn.reebook;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lirui on 2017/4/20.
 */
@RestController
public class TestController {
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public JSONObject getTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("description", "This is a text");
        return jsonObject;
    }
}
