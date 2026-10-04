package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.ResourceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceModel, Integer>, JpaSpecificationExecutor<ResourceModel> {

    List<ResourceModel> findByActiveTrue();

    Optional<ResourceModel> findByIdAndActiveTrue(Integer id);

    Boolean existsByIdAndActiveTrue(Integer id);
}