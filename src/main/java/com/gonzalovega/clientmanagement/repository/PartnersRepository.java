package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.PartnersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnersRepository extends JpaRepository<PartnersModel, Integer>, JpaSpecificationExecutor<PartnersModel> {
    Optional<PartnersModel> findPartnersById(Integer id);

    List<PartnersModel> findAllByActiveTrue();
}
