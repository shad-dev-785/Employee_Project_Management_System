package com.agile.api_gateway;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openEndpoints = List.of("/auth/register",
            "/auth/login",
            "/eureka");

    public Predicate<ServerHttpRequest> isSecured = serverHttpRequest -> openEndpoints.stream().noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
}


