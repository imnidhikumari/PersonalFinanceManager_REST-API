package org.finance2.dtoOrmodels.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSnakeCase
public class TransactionResponse {

    Long userId;
    Double amount;
    String type;
    String description;
}
