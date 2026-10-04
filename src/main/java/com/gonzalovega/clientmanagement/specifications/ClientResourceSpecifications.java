package com.gonzalovega.clientmanagement.specifications;

import jakarta.persistence.criteria.Predicate;
import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceSearchRequestDto;
import com.gonzalovega.clientmanagement.models.ClientResourceModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClientResourceSpecifications {

    public static Specification<ClientResourceModel> byFilter(ClientResourceSearchRequestDto f) {
        return (root, query, cb) -> {

            if (f == null) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();

            if (f.getClientId() != null) {
                predicates.add(cb.equal(root.get("clientId").get("id"), f.getClientId()));
            }
            if( f.getClientIds() != null && !f.getClientIds().isEmpty() ) {
                predicates.add(root.get("clientId").get("id").in(f.getClientIds()));
            }
            if (f.getResourceId() != null) {
                predicates.add(cb.equal(root.get("resourceId").get("id"), f.getResourceId()));
            }

            if (f.getPersonalized() != null) {
                predicates.add(cb.equal(root.get("personalized"), f.getPersonalized()));
            }

            if (f.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), f.getStartDate()));
            }

            if (f.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), f.getEndDate()));
            }

            if (f.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), f.getActive()));
            } else {
                predicates.add(cb.isTrue(root.get("active")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}