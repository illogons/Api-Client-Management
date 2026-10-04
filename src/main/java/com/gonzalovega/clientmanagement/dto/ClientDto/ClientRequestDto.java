package com.gonzalovega.clientmanagement.dto.ClientDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequestDto {

    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate terminationDate;

    private Integer versionLock;

    @NotNull(message = "client have support or not")
    private Boolean support;

    private String url;

    private Integer partnerId;

    @Size(max = 500, message = "comment description must not exceed 5000 characters")
    private String comment;

    @NotNull(message = "Database ID is required")
    private Integer databaseId;

    private Integer responsibleId;

    @NotNull(message = "Operative System ID is required")
    private Integer operativeSystemId;

}
