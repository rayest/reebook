package cn.reebook.foundation.security.authentication.rolesAuthenticationProviders;

import cn.reebook.foundation.security.authentication.Authentication;
import cn.reebook.foundation.security.authentication.AuthenticationResult;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by xubt on 24/10/2016.
 */
@Service("visitorAuthenticationProvider")
public class VisitorAuthenticationProvider implements Authentication {

    @Override
    public AuthenticationResult authenticate(Map pathValues, String userName) {
        return new AuthenticationResult();
    }
}
