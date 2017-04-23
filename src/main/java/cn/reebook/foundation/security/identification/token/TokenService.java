package cn.reebook.foundation.security.identification.token;

import cn.reebook.foundation.common.date.DateService;
import cn.reebook.foundation.common.date.DateStyle;
import cn.reebook.foundation.security.Constants;
import cn.reebook.foundation.security.identification.rsa.RSAService;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by xubt on 7/8/16.
 */
@Service
public class TokenService {
    @Resource
    public RSAService rsaService;

    @Resource
    private DateService dateService;

    public String buildToken(String userName) throws Exception {
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setUserName(userName);
        Date expirationTime = dateService.addSecond(new Date(), Constants.TOKEN_EXPIRED_TIME);
        String expirationTimeStr = dateService.DateToString(expirationTime, DateStyle.YYYY_MM_DD_HH_MM_SS);

        authenticationToken.setExpirationTime(expirationTimeStr);
        return rsaService.encrypt(authenticationToken.toString());
    }

    public boolean isExpired(String token) throws Exception {
        String decryptedToken = rsaService.dencrypt(token);
        AuthenticationToken authenticationToken = JSON.parseObject(decryptedToken, AuthenticationToken.class);

        Date expiredTime = dateService.StringToDate(authenticationToken.getExpirationTime(), DateStyle.YYYY_MM_DD_HH_MM_SS);
        return expiredTime.before(new Date());
    }

    public boolean isTampered(String token, String userName) throws Exception {
        String decryptedToken = rsaService.dencrypt(token);
        AuthenticationToken authenticationToken = JSON.parseObject(decryptedToken, AuthenticationToken.class);

        return !authenticationToken.getUserName().equals(userName);
    }

    public IdentityResult identify(String token, String userName) throws Exception {
        if (isTokenEmpty(token)) {
            return IdentityResult.result(Constants.SECURITY_IDENTITY_NO_AUTHENTICATION_TOKEN_CODE, Constants.SECURITY_IDENTITY_NO_AUTHENTICATION_TOKEN);
        }
        if (isExpired(token)) {
            return IdentityResult.result(Constants.SECURITY_IDENTITY_AUTHENTICATION_TOKEN_HAS_EXPIRE_CODE, Constants.SECURITY_IDENTITY_AUTHENTICATION_TOKEN_HAS_EXPIRE);
        }
        if (isTampered(token, userName)) {
            return IdentityResult.result(Constants.SECURITY_IDENTITY_USER_NAME_IS_NOT_CONSISTENT_CODE, Constants.SECURITY_IDENTITY_USER_NAME_IS_NOT_CONSISTENT);
        }
        return IdentityResult.result(Constants.SECURITY_IDENTIFY_PASSED_CODE, Constants.SECURITY_IDENTIFY_PASSED);
    }


    private boolean isTokenEmpty(String token) {
        return token == null || token.equals("") || token.equals("null");
    }

    public String updateToken(String token) throws Exception {
        String decryptedToken = rsaService.dencrypt(token);
        AuthenticationToken authenticationToken = JSON.parseObject(decryptedToken, AuthenticationToken.class);

        return buildToken(authenticationToken.getUserName());
    }
}
