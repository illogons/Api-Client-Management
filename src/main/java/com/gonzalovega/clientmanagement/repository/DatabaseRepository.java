package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.DatabaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseRepository extends JpaRepository<DatabaseModel, Integer>, JpaSpecificationExecutor<DatabaseModel> {

    List<DatabaseModel> findAllByActiveTrue();

    Boolean existsByIdAndActiveTrue(Integer id);

    Optional<DatabaseModel> findByIdAndActiveTrue(Integer id);
}
