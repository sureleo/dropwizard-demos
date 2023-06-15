package com.howtodoinjava.app.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.howtodoinjava.app.model.Employee;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Provider
public class LoggingRequestFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        LOGGER.info("Received request - Method: {}, Path: {}", method, path);

        // Log the request body
        if (!method.equals("GET")) {
            InputStream requestBody = requestContext.getEntityStream();
            String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            Employee e = objectMapper.readValue(requestBodyString, Employee.class);
            LOGGER.info(objectMapper.writeValueAsString(e));

            // Set the request body back to the input stream so it can be consumed by the resource
            requestContext.setEntityStream(new ByteArrayInputStream(requestBodyString.getBytes()));
        }
    }
}