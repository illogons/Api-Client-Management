package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.OperativeSystemModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperativeSystemRepository extends JpaRepository<OperativeSystemModel, Integer>, JpaSpecificationExecutor<OperativeSystemModel> {

    List<OperativeSystemModel> findByActiveTrue();

    Optional<OperativeSystemModel> findByIdAndActiveTrue(Integer id);

    Boolean existsByIdAndActiveTrue(Integer id);
}