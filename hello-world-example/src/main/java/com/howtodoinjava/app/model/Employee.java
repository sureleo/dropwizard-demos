package com.howtodoinjava.app.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.howtodoinjava.app.obfuscation.EmployeeCustomDeserializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonDeserialize(using = EmployeeCustomDeserializer.class)
public class Employee {

  @NotNull
  private Integer id;

  @NotBlank
  @Length(min = 2, max = 255)
  private String firstName;

  @NotBlank
  @Length(min = 2, max = 255)
  private String lastName;

  @Pattern(regexp = ".+@.+\\.[a-z]+")
  private String email;
}
