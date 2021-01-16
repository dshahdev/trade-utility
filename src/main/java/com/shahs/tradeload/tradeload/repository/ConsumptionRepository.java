package com.shahs.tradeload.tradeload.repository;

import com.shahs.tradeload.tradeload.model.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {

    @Query(value = "SELECT * from consumptionView", nativeQuery = true)
    public List<Consumption> findAllConsumptionData();
 }
