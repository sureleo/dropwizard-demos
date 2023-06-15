package com.howtodoinjava.app.obfuscation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.howtodoinjava.app.model.Employee;

import java.io.IOException;

public class EmployeeCustomDeserializer extends JsonDeserializer<Employee> {
    @Override
    public Employee deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int id = (Integer) (node.get("id")).numberValue();
        String firstName = node.get("firstName").asText();
        String lastName = node.get("lastName").asText();

        return new Employee(id, firstName, lastName, "Obfuscated@Obfuscated.com");
    }
}
