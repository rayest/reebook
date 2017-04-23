package cn.reebook.registration;

import cn.reebook.foundation.common.Response;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * Created by lirui on 2017/4/21.
 */
@RestController
public class RegistrationController {

    @Resource
    private RegistrationService registrationService;

    @Resource
    private RegistrationResource registrationResource;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public HttpEntity register(@RequestBody Registration registration) {
        User registeredUser = registrationService.register(registration);
        return Response.post(registrationResource.toResource(registeredUser));
    }
}
