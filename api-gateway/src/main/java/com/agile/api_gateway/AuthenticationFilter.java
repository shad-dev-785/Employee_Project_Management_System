package com.agile.api_gateway;

import com.netflix.spectator.impl.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

@Autowired
JwtUtil jwtUtil;
@Autowired
RouteValidator routeValidator;
    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public org.springframework.cloud.gateway.filter.GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new RuntimeException("Invalid authorization header");
                }
                String jwtToken = authHeader.substring(7);
                try {
                    // 4. VALIDATE TOKEN (The "Scanner")
                    jwtUtil.validateToken(jwtToken);

                    // 5. EXTRACT USERNAME
                    String username = jwtUtil.extractUsername(jwtToken);

                    // 6. MUTATE THE REQUEST (Add "loggedInUser" header)
                    // This is how we pass the user's identity to Project Service
                    exchange = exchange.mutate()
                            .request(request -> request.header("loggedInUser", username))
                            .build();

                } catch (Exception e) {
                    System.out.println("Invalid Access...!");
                    System.out.println("GATEWAY TOKEN ERROR: " + e.getClass().getName());
                    System.out.println("ERROR MESSAGE: " + e.getMessage());
                    e.printStackTrace(); // <--- CRITICAL: Print the stack trace to console
                    throw new RuntimeException("Unauthorized access to application");
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Put the configuration properties
    }
}
