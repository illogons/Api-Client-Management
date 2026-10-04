package com.gonzalovega.clientmanagement.dto.ResourceDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.gonzalovega.clientmanagement.ResourceType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRequestDto {

    private Integer id;

    @NotNull(message = "Type cannot be null")
    private ResourceType typeApp;

    @NotNull(message = "Launch date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate launchDate;

    @NotBlank(message = "name cannot be blank")
    @Size(max = 150, message = "name must not exceed 150 characters")
    private String name;

    @Size(max = 5000, message = "description must not exceed 5000 characters")
    private String description;

    private Integer versionLock;
}