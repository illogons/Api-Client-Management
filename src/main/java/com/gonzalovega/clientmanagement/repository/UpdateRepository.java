package com.gonzalovega.clientmanagement.repository;

import com.gonzalovega.clientmanagement.models.ClientResourceModel;
import com.gonzalovega.clientmanagement.models.ResourceModel;
import com.gonzalovega.clientmanagement.models.UpdateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UpdateRepository extends JpaRepository<UpdateModel, Integer>, JpaSpecificationExecutor<UpdateModel> {


    Optional<UpdateModel> findByIdAndActiveTrue(Integer id);

    List<UpdateModel> findAllByClientIdIdAndActiveTrue(Integer clientId);

    List<UpdateModel> findByClientIdId(Integer id);

    Optional<UpdateModel> findById(Integer id);

    List<UpdateModel> findAllByActiveTrue();

    List<UpdateModel> findByFutureDateIsNotNullAndFutureDateBefore(LocalDateTime now);
}
