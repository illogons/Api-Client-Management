package com.gonzalovega.clientmanagement.specifications;

import jakarta.persistence.criteria.JoinType;
import com.gonzalovega.clientmanagement.dto.ClientDto.ClientSearchRequestDto;
import com.gonzalovega.clientmanagement.models.ClientModel;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ClientSpecifications {

    public static Specification<ClientModel> byFilter(ClientSearchRequestDto f) {
        return (root, query, cb) -> {

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (f == null) return cb.conjunction();

            if (f.getCreatedBy() != null) {
                predicates.add(cb.like(cb.lower(root.get("createdBy")),
                        f.getCreatedBy().trim().toLowerCase()));
            }

            if (hasText(f.getName())) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + f.getName().trim().toLowerCase() + "%"));
            }
            if (hasText(f.getEmail())) {
                predicates.add(cb.like(cb.lower(root.get("email")),
                        "%" + f.getEmail().trim().toLowerCase() + "%"));
            }

            if (hasText(f.getPhoneNumber())) {
                predicates.add(cb.like(cb.lower(root.get("phoneNumber")),
                        "%" + f.getPhoneNumber().trim().toLowerCase() + "%"));
            }
            if (hasText(f.getAddress())) {
                predicates.add(cb.like(cb.lower(root.get("address")),
                        "%" + f.getAddress().trim().toLowerCase() + "%"));
            }

            if (f.getJoinDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("joinDate"), f.getJoinDateFrom()));
            }
            if (f.getJoinDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("joinDate"), f.getJoinDateTo()));
            }

            if( f.getSupport() != null){
                predicates.add(cb.equal(root.get("support"), f.getSupport()));
            }







            //filtrar por fechas
            if (f.getTerminationDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("terminationDate"), f.getTerminationDateFrom()));
            }
            if (f.getTerminationDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("terminationDate"), f.getTerminationDateTo()));
            }

            if (f.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), f.getActive()));
            } else {
                predicates.add(cb.isTrue(root.get("active")));
            }







            // para filtrar por nombre de database desde cliente
            if (hasText(f.getDatabaseName())) {
                var dbJoin =root.join("databaseId", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(dbJoin.get("name")),
                        "%" + f.getDatabaseName().trim().toLowerCase() + "%"));
            }
            // para filtrar por nombre de op desde cliente
            if (hasText(f.getOperativeSystemName())) {
                var osJoin = root.join("operativeSystemId", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(osJoin.get("name")),
                        "%" + f.getOperativeSystemName().trim().toLowerCase() + "%"));
            }
            // para filtrar por nombre de partner desde client
            if(hasText(f.getPartnerName())){
                var osJoin = root.join("partnerId", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(osJoin.get("name")),
                        "%" + f.getPartnerName().trim().toLowerCase() + "%"));
            }
            // para filtrar por nombre de partner desde client
            if(hasText(f.getResponsibleName())){
                var osJoin = root.join("responsibleId", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(osJoin.get("name")),
                        "%" + f.getResponsibleName().trim().toLowerCase() + "%"));
            }











            // para filtrar por listas de los nombres desde clientes
            if (f.getDatabaseNames() != null && !f.getDatabaseNames().isEmpty()) {
                var dbJoin = root.join("databaseId", JoinType.LEFT);
                predicates.add(cb.lower(dbJoin.get("name")).in(f.getDatabaseNames().stream().map(String::toLowerCase)
                        .toList()));
            }

            if (f.getOperativeSystemNames() != null && !f.getOperativeSystemNames().isEmpty()) {
                var osJoin = root.join("operativeSystemId", JoinType.LEFT);
                predicates.add(cb.lower(osJoin.get("name")).in(f.getOperativeSystemNames().stream().map(String::toLowerCase)
                        .toList()));
            }

            if (f.getClientNames() != null && !f.getClientNames().isEmpty()) {
                var join = root.join("clientId", JoinType.LEFT);
                predicates.add(cb.lower(join.get("name"))
                        .in(f.getClientNames().stream().map(String::toLowerCase).toList()));

            }
            if(f.getPartnerNames() != null && !f.getPartnerNames().isEmpty()) {
                var join = root.join("partnerId", JoinType.LEFT);
                predicates.add(cb.lower(join.get("name"))
                        .in(f.getPartnerNames().stream().map(String::toLowerCase).toList()));
            }
            if(f.getResponsibleNames() != null && !f.getResponsibleNames().isEmpty()) {
                var join = root.join("responsibleId", JoinType.LEFT);
                predicates.add(cb.lower(join.get("name"))
                        .in(f.getPartnerNames().stream().map(String::toLowerCase).toList()));
            }








            //para filtrar por listas de ids desde cliente
            if (f.getDatabasesIds() != null && !f.getDatabasesIds().isEmpty()) {
                predicates.add(root.get("databaseId").get("id").in(f.getDatabaseId()));
            }
            if(f.getOperativeSystemIds() != null && !f.getOperativeSystemIds().isEmpty()){
                predicates.add(root.get("operativeSystemId").get("id").in(f.getOperativeSystemId()));
            }
            if (f.getClientIds() != null && !f.getClientIds().isEmpty()) {
                predicates.add(root.get("clientId").get("id").in(f.getClientIds()));
            }
            if(f.getPartnerIds() != null && !f.getPartnerIds().isEmpty()) {
                predicates.add(root.get("partnerId").get("id").in(f.getPartnerIds()));
            }
            if(f.getResponsibleIds() != null && !f.getResponsibleIds().isEmpty()) {
                predicates.add(root.get("responsibleId").get("id").in(f.getResponsibleIds()));
            }







            // filtrar por id aislado
            if(f.getPartnerId() != null){
                predicates.add(root.get("partnerId").get("id").in(f.getPartnerId()));
            }if(f.getResponsibleId() != null){
                predicates.add(root.get("responsibleId").get("id").in(f.getResponsibleId()));
            }







            // filtra por versiones desde cliente
            if(hasText(f.getVersionClient())){
                var osJoin = root.join("updateModels", JoinType.INNER);

                var jointversion = osJoin.join("versionId", JoinType.INNER);

                predicates.add(cb.like(cb.lower(jointversion.get("version")),
                        "%" + f.getVersionClient().trim().toLowerCase() + "%"));

                // Evita duplicados si un cliente tiene varias versiones
                query.distinct(true);
            }





                return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }
}