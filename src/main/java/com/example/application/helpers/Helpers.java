package com.example.application.helpers;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePropertySource;

import java.util.Map;
import java.util.stream.StreamSupport;

public class Helpers {

    public static Map<String, Object> asProperties(Environment env) {
        return StreamSupport.stream(
                        ((AbstractEnvironment) env).getPropertySources().spliterator(), false)
                .filter(ps -> ps instanceof ResourcePropertySource)
                .map(ps -> ((ResourcePropertySource) ps).getSource())
                .findFirst()
                .orElse(null);
    }


}
