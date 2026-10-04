package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.TypeConnectionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeConnectionRepository extends JpaRepository<TypeConnectionModel, Integer>, JpaSpecificationExecutor<TypeConnectionModel> {

    List<TypeConnectionModel> findByActiveTrue();

    Optional<TypeConnectionModel> findByIdAndActiveTrue(Integer typeConnectionId);
}
