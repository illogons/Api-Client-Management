package com.gonzalovega.clientmanagement.specifications;

import jakarta.persistence.criteria.Predicate;
import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemSearchRequestDto;
import com.gonzalovega.clientmanagement.models.OperativeSystemModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OperativeSystemSpecifications {

    public static Specification<OperativeSystemModel> byFilter(OperativeSystemSearchRequestDto f) {
        return (root, query, cb) -> {

            if (f == null) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();

            if (hasText(f.getName())) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + f.getName().trim().toLowerCase() + "%"));
            }

            if (hasText(f.getVersion())) {
                predicates.add(cb.like(cb.lower(root.get("version")), "%" + f.getVersion().trim().toLowerCase() + "%"));
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