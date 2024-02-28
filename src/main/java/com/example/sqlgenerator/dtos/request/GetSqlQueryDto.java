package com.example.sqlgenerator.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSqlQueryDto {
    @NotBlank
    @Size(min = 10, max = 200)
    private String nlQuery;

    @NotBlank
    @Size(max = 50)
    private String dbName;

    @NotBlank
    @Size(max = 50)
    private String schema;
}
