package com.PetCaretopia.health.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaccineTypeDTO {
    private Long vaccineTypeId;
    private String vaccineTypeName;
    private String vaccineTypeDescription;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
