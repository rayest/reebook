package cn.reebook.registration;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lirui on 2017/4/21.
 */
@RestController
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public HttpEntity login(Object o, Object o1) {
        return null;
    }
}
