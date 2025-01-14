package org.finance2.dtoOrmodels.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSnakeCase
public class LoginRequest {

    @NotNull
    String email;

    @NotNull
    String password;
}
