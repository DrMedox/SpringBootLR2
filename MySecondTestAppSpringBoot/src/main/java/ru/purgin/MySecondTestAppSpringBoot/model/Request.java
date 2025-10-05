package ru.purgin.MySecondTestAppSpringBoot.model;
import jakarta.validation.*;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @NotBlank
    @Size(max=32)
    private String uid;

    @NotBlank
    @Size(max=32)
    private String operationUid;

    private String systemName;

    @NotBlank
    private String systemTime;

    private String source;

    @Min(1)
    @Max(100000)
    private int communicationId;

    private int templateId;
    private int productCode;
    private int smsCode;
}
