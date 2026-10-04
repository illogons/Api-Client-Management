package com.gonzalovega.clientmanagement.dto.ClientDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder

public class ClientSearchRequestDto {

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDateTo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate terminationDateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate terminationDateTo;

    private String url;

    private Boolean active;
    private Boolean support;

    private Integer databaseId;
    private Integer operativeSystemId;
    private Integer partnerId;
    private Integer responsibleId;

    private String DatabaseName;
    private String operativeSystemName;
    private String partnerName;
    private String versionClient;
    private String responsibleName;

    private List<Integer> DatabasesIds;
    private List<Integer> OperativeSystemIds;
    private List<Integer> ClientIds;
    private List<Integer> partnerIds;
    private List<Integer> responsibleIds;

    private List<String> databaseNames;
    private List<String> operativeSystemNames;
    private List<String> clientNames;
    private List<String> partnerNames;
    private List<String> responsibleNames;





}