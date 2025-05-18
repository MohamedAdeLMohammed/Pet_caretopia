package com.PetCaretopia.health.DTO;

import com.PetCaretopia.health.entity.VaccineType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaccineDTO {
    private Long vaccineId;
    private String vaccineName;
    private String description;
    private Integer recommendedAgeWeeks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private VaccineType vaccineType;
}
