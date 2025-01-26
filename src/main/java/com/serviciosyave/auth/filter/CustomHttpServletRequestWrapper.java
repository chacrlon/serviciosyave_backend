package com.serviciosyave.auth.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.Enumeration;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String token;

    public CustomHttpServletRequestWrapper(HttpServletRequest request, String token) {
        super(request);
        this.token = token;
    }

    @Override
    public String getHeader(String name) {
        if ("Authorization".equals(name)) {
            return "Bearer " + this.token;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if ("Authorization".equals(name)) {
            return Collections.enumeration(Collections.singletonList("Bearer " + this.token));
        }
        return super.getHeaders(name);
    }
}