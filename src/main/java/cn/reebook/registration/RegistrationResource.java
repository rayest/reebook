package cn.reebook.registration;

import cn.reebook.foundation.common.RestResource;
import cn.reebook.foundation.hateoas.TLink;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by lirui on 2017/4/21.
 */
@Service
public class RegistrationResource extends RestResource {

    @Resource
    private TLink tlink;

    public Object toResource(User registeredUser) {
        RegistrationResource registrationResource = new RegistrationResource();
        registrationResource.domainObject = registeredUser;
        if (registeredUser != null) {
            Link loginLInk = linkTo(methodOn(LoginController.class).login(null, null)).withRel("login");
            registrationResource.add(tlink.from(loginLInk).build());
        }
        return registrationResource.getResource();
    }
}
