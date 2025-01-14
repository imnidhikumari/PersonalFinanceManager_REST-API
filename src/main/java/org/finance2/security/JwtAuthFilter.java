package org.finance2.security;

import io.jsonwebtoken.Claims;
import org.finance2.utils.JwtUtil;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider //Marks this class as a Jersey filter provider.
@Priority(1000)
public class JwtAuthFilter implements ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String path = requestContext.getUriInfo().getPath();
        // Only apply to specific paths
        if (!path.startsWith("transactions") && !path.startsWith("balance")) {
            return; // Skip the filter for other resources
        }

        String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            abortRequest(requestContext, "Missing or Invalid Authorization Header");
            return;
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            Claims claims = JwtUtil.validateToken(token);
            // Attaching user information to the request context for identification
            requestContext.setProperty("userEmail",claims.getSubject());
        } catch (Exception e) {
            abortRequest(requestContext, "Invalid or Expired token");
        }
    }

    private  void abortRequest(ContainerRequestContext requestContext, String message){
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity(message)
                .build());
    }
}
