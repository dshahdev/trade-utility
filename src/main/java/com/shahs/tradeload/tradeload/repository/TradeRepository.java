package com.shahs.tradeload.tradeload.repository;

import com.shahs.tradeload.tradeload.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface TradeRepository extends JpaRepository<Trade, Long> {
    @Query(value = "SELECT t FROM Trade t WHERE t.action = 'Sell' and t.date = Date(:date)")
    List<Trade> findSellTradesForDate(@Param("date") String date);

}
