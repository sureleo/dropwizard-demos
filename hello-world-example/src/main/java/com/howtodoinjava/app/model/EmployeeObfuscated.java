package com.howtodoinjava.app.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.howtodoinjava.app.obfuscation.EmployeeCustomDeserializer;

// TODO: experiment with https://robtimus.github.io/obfuscation-jackson-databind/
// which is a much cleaner approach than custom deserializer
@JsonDeserialize(using = EmployeeCustomDeserializer.class)
public class EmployeeObfuscated extends Employee {

}
