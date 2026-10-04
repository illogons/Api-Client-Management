package com.gonzalovega.clientmanagement.specifications;

import jakarta.persistence.criteria.Predicate;
import com.gonzalovega.clientmanagement.dto.UsersDto.UsersSearchDto;
import com.gonzalovega.clientmanagement.models.UsersModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class UsersSpecifications {

    public static Specification<UsersModel> byFilter (UsersSearchDto f){
        return ( root, query, cb) -> {

            if (f == null) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();

            if (hasText(f.getName())) {
                String like = "%" + f.getName().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), like));
            }

            if(f.getNames() != null && !f.getNames().isEmpty()) {
                predicates.add(root.get("name").in(f.getNames()));
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