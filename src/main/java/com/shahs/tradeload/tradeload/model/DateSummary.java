package com.shahs.tradeload.tradeload.model;

import javax.persistence.*;
import java.util.ArrayList;
@Entity
@Table(name="date_summary")
@SequenceGenerator(name="seq", initialValue=9, allocationSize=10)
public class DateSummary {

    private long id;
    private String s_date;
    private String s_ticker;
    private double profit_loss;

    public DateSummary() {
    }

    public DateSummary(String date, String ticker, double profit_loss) {
        this.s_date = date;
        this.s_ticker = ticker;
        this.profit_loss = profit_loss;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name="s_date", nullable = false)
    public String getSummaryDate() {
        return this.s_date;
    }
    public void setSummaryDate(String date) {
        this.s_date = date;
    }

    @Column(name="s_ticker", nullable = false)
    public String getSummaryTicker() {
        return this.s_ticker;
    }
    public void setSummaryTicker(String ticker) {
        this.s_ticker = ticker;
    }

    @Column(name="profit_loss", nullable = false)
    public double getProfitLoss() {
        return this.profit_loss;
    }
    public void setProfitLoss(double pl){
        this.profit_loss = pl;
    }

    @Override
    public String toString() {

        String s = " Id:"+ this.getId() +
                " Date: " + this.getSummaryDate() +
                " ticker: " + this.getSummaryTicker() +
                " profit/loss: " + this.getProfitLoss();

        return s;
    }

}
