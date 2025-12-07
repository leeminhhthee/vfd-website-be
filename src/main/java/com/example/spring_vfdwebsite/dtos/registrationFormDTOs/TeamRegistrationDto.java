package com.example.spring_vfdwebsite.dtos.registrationFormDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for Team Registration")
public class TeamRegistrationDto {
    @Schema(description = "ID of the registration form", example = "1")
    private Integer id;

    @Schema(description = "Name of the team", example = "The Invincibles")
    private String teamName;

    @Schema(description = "Name of the coach", example = "John Doe")
    private String coach;

    @Schema(description = "Registration unit", example = "Sports Club A")
    private String registrationUnit;
    
    @Schema(description = "Number of athletes in the team", example = "15")
    private Integer numberAthletes;
}
