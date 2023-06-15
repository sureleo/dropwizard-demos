package com.howtodoinjava.app.obfuscation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.howtodoinjava.app.model.Employee;

import java.io.IOException;

public class EmployeeCustomDeserializer extends JsonDeserializer<Employee> {
    @Override
    public Employee deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        Employee myObject = jsonParser.readValueAs(Employee.class);
        myObject.setEmail("<obfuscated>");
        return myObject;
    }
}
