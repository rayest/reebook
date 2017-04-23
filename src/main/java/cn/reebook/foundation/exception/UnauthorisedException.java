package cn.reebook.foundation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorisedException extends BusinessException {

    private static final long serialVersionUID = -5266231525138811045L;

    public UnauthorisedException(int errorCode, String message) {
        super(errorCode, message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorisedException(Object codeObject) {
        super(codeObject);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
