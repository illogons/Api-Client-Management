package com.gonzalovega.clientmanagement.specifications;

import jakarta.persistence.criteria.Predicate;
import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseSearchRequestDto;
import com.gonzalovega.clientmanagement.models.DatabaseModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSpecifications {

    public static Specification<DatabaseModel> byFilter(DatabaseSearchRequestDto f) {
        return (root, query, cb) -> {

            if (f == null) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();

            if (hasText(f.getName())) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + f.getName().trim().toLowerCase() + "%"));
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