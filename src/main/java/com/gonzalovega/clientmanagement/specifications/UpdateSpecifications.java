package com.gonzalovega.clientmanagement.specifications;

import jakarta.persistence.criteria.Predicate;
import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateSearchRequestDto;
import com.gonzalovega.clientmanagement.models.UpdateModel;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UpdateSpecifications {

    public static Specification<UpdateModel> byFilter(UpdateSearchRequestDto f) {
        return (root, query, cb) -> {

            if (f == null) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();

            if (f.getClientId() != null) {
                predicates.add(cb.equal(root.get("clientId").get("id"), f.getClientId()));
            }

            if (f.getVersionId() != null) {
                predicates.add(cb.equal(root.get("versionId").get("id"), f.getVersionId()));
            }

            if (f.getUserId() != null) {
                predicates.add(cb.equal(root.get("userId").get("id"), f.getUserId()));
            }

            if (f.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), f.getActive()));
            } else {
                predicates.add(cb.isTrue(root.get("active")));
            }

            if (f.getLaunchDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("launchDate"), f.getLaunchDate()));
            }

            if (f.getTerminationDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("terminationDate"), f.getTerminationDate()));
            }

            if (f.getFuture() != null) {
                if (f.getFuture()) {
                    predicates.add(cb.greaterThan(root.get("futureDate"), LocalDateTime.now()));
                } else {
                    predicates.add(cb.or(
                            cb.isNull(root.get("futureDate")),
                            cb.lessThanOrEqualTo(root.get("futureDate"), LocalDateTime.now())
                    ));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}