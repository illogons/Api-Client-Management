package com.gonzalovega.clientmanagement.dto.ClientDto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VersionHistoryDTO {

    private Integer updateId;
    private LocalDate launchDate;
    private LocalDate terminationDate;
    private String versionName;
    private String changelog;
    private Boolean active;
}
