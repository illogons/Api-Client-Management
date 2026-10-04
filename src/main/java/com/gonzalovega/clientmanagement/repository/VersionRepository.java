package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.VersionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VersionRepository extends JpaRepository<VersionModel, Integer>, JpaSpecificationExecutor<VersionModel> {

    List<VersionModel> findAllByActiveTrue();

    Boolean existsByIdAndActiveTrue(Integer id);

    Optional<VersionModel> findByIdAndActiveTrue(Integer id);
}
