package com.howtodoinjava.app.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.howtodoinjava.app.model.Employee;
import com.howtodoinjava.app.model.EmployeeObfuscated;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/*
 * There are better ways that we could explore too.
 * i.e. using dropwizard server request log, but need to figure out how to obfuscate certain fields.
 */
@Provider
public class LoggingRequestFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = LogManager.getLogger(LoggingRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        String requestToReplay = "%s;%s;%s";
        String body = "";

        InputStream requestBody = requestContext.getEntityStream();
        String requestBodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

        if (!requestBodyString.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Employee e = objectMapper.readValue(requestBodyString, EmployeeObfuscated.class);
            body = objectMapper.writeValueAsString(e);

            // Set the request body back to the input stream so it can be consumed by the resource
            requestContext.setEntityStream(new ByteArrayInputStream(requestBodyString.getBytes()));
        }

        LOGGER.info(String.format(requestToReplay, method, path, body));
    }
}