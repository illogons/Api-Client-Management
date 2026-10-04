package com.gonzalovega.clientmanagement.specifications;

import jakarta.persistence.criteria.Predicate;
import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnSearchDto;
import com.gonzalovega.clientmanagement.models.ClientVPNModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClientVpnSpecification {

    public static Specification<ClientVPNModel> byFilter(ClientVpnSearchDto f) {
        return (root, query, cb) -> {

            if (f == null) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();

            if (f.getClientId() != null) {
                predicates.add(cb.equal(root.get("clientId").get("id"), f.getClientId()));
            }

            if (f.getClientIds() != null && !f.getClientIds().isEmpty()) {
                predicates.add(root.get("clientId").get("id").in(f.getClientIds()));
            }

            if (f.getVpnId() != null) {
                predicates.add(cb.equal(root.get("vpnId").get("id"), f.getVpnId()));
            }

            if (f.getVpnIds() != null && !f.getVpnIds().isEmpty()) {
                predicates.add(root.get("vpnId").get("id").in(f.getVpnIds()));
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
