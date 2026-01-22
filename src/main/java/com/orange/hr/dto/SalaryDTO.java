package com.orange.hr.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryDTO {
    private Double gross;
    private Double net;
}
