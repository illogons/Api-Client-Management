package com.gonzalovega.clientmanagement.specifications;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionSearchRequestDto;
import com.gonzalovega.clientmanagement.models.ConnectionModel;
import org.springframework.data.jpa.domain.Specification;

public class ConnectionSpecifications {
    public static Specification<ConnectionModel> byFilter(ConnectionSearchRequestDto filterDto) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filterDto == null) return cb.conjunction();

            if (filterDto.getClientId() != null) {
                predicates.add(cb.equal(root.get("clientId").get("id"), filterDto.getClientId()));
            }

            if (filterDto.getClientIds() != null && !filterDto.getClientIds().isEmpty()) {
                predicates.add(root.get("clientId").get("id").in(filterDto.getClientIds()));
            }

            if (filterDto.getTypeConnectionId() != null) {
                predicates.add(cb.equal(root.get("typeConnectionId").get("id"), filterDto.getTypeConnectionId()));
            }

            if (hasText(filterDto.getDetails())) {
                String like = "%" + filterDto.getDetails().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("details")), like)
                ));
            }

            if (filterDto.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), filterDto.getActive()));
            } else {
                predicates.add(cb.isTrue(root.get("active")));
            }

            return cb.and(predicates.toArray((new Predicate[0])));
        };
    }
    private static boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
