package com.ssafy.tripgg.domain.verification.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class LocationRequest {

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;
}
