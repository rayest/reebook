package cn.reebook.foundation.common;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Created by xubitao on 1/2/16.
 */

public class Response {
    public static HttpEntity build(UrlResource urlResource) throws IOException {
        return Response.build(FileUtil.urlResourceToString(urlResource));
    }

    public static HttpEntity build(String s) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(s);
    }

    public static HttpEntity build(Object o) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(o);
    }

    public static HttpEntity post(Object o) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(o);
    }
}
