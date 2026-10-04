package com.gonzalovega.clientmanagement.specifications;

import jakarta.persistence.criteria.Predicate;
import com.gonzalovega.clientmanagement.dto.VersionDto.VersionSearchRequestDto;
import com.gonzalovega.clientmanagement.models.VersionModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class VersionSpecifications {

    public static Specification<VersionModel> byFilter(VersionSearchRequestDto f) {
        return (root, query, cb) -> {

            if (f == null) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();

            if (hasText(f.getVersion())) {
                String like = "%" + f.getVersion().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("version")), like));
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

            query.orderBy(
                    cb.desc(root.get("launchDate")),
                    cb.desc(root.get("id"))
            );

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }
}