package io.nwdaf.eventsubscription.controller.http;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public class HttpServletRequestAdapter implements HttpInputMessage {

    private final HttpServletRequest servletRequest;

    public HttpServletRequestAdapter(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    @Override
    public InputStream getBody() throws IOException {
        return servletRequest.getInputStream();
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.add(headerName, servletRequest.getHeader(headerName));
        }
        return headers;
    }
}
