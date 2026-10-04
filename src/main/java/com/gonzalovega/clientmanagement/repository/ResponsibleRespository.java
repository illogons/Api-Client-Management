package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.OperativeSystemModel;
import com.gonzalovega.clientmanagement.models.ResponsibleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponsibleRespository extends JpaRepository<ResponsibleModel, Integer>, JpaSpecificationExecutor<ResponsibleModel> {

    Optional<ResponsibleModel> findResponsibleModelById(int id);

    List<ResponsibleModel> findAllByActiveTrue();

    Optional<ResponsibleModel> findByIdAndActiveTrue(Integer id);
}
