package com.gonzalovega.clientmanagement.specifications;

import jakarta.persistence.criteria.Predicate;
import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceSearchRequestDto;
import com.gonzalovega.clientmanagement.models.ResourceModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ResourceSpecifications {

    public static Specification<ResourceModel> byFilter(ResourceSearchRequestDto f) {
        return (root, query, cb) -> {

            if (f == null) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();

            if (f.getTypeApp() != null) {
                predicates.add(cb.equal(root.get("typeApp"), f.getTypeApp()));
            }

            if (hasText(f.getName())) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + f.getName().trim().toLowerCase() + "%"));
            }

            if (hasText(f.getDescription())) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + f.getDescription().trim().toLowerCase() + "%"));
            }

            if (f.getLaunchDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("launchDate"), f.getLaunchDateFrom()));
            }
            if (f.getLaunchDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("launchDate"), f.getLaunchDateTo()));
            }
            if (f.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), f.getActive()));
            } else {
                predicates.add(cb.isTrue(root.get("active")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }
}