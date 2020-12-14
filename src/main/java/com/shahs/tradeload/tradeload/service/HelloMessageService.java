package com.shahs.tradeload.tradeload.service;

import com.shahs.tradeload.tradeload.model.DateSummary;
import com.shahs.tradeload.tradeload.model.Trade;
import com.shahs.tradeload.tradeload.repository.DateSummaryRepository;
import com.shahs.tradeload.tradeload.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HelloMessageService {

    private String name;
    @Value("${csvFile:unknown}")
    private String csvFile;

    String line = "";
    String splitBy=",";

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    DateSummaryRepository dateSummaryRepository;

    public String getMessage() {
        return getMessage(name);
    }

    public String getMessage(String name) {
        return "Hello " + name;
    }
    public void printCsvFile() throws IOException{
        printCsvFile(csvFile);
    }
    //Trades file
    public void printCsvFile(String csvFile) throws IOException {
        Map<String, Double> tradeMap = new ConcurrentHashMap<String, Double>();
        DateSummary ds = new DateSummary();

        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        br.readLine();
        br.readLine();
        String line1 = "";
        while((line1 = br.readLine()) != null) {

            String[] arr = line1.replace("\"","")
                            .replace("$","")
                            .split(",");
            if(!(arr[1].equals("Buy") || arr[1].equals("Sell") || arr[1].equals("Sell Short"))){
                continue;
            }
            System.out.println("Line1: "+ line1);
            Trade trade = Trade.createTradeObj(arr);
            //created a key -- date+ticker
            String key = trade.getDate() + trade.getTicker();
            // checking if old value is there,
            // if yes then get new value for the same key
            // and add up
            // then put into the map
            Double pnlVal = 0.0;
            if (tradeMap.containsKey(key)) {

                pnlVal = tradeMap.get(key);
            }
            tradeMap.put(key, pnlVal + trade.getAmount());

            tradeRepository.save(trade);
            System.out.println(line1);
        }
        // so now we have hashmap with key-- date+ticker, value pnlVal
        // now we r iterating throw this hashmap and separating date, ticker and pnlvalue
        // and create the object of DateSummary
        for(Map.Entry<String, Double> entry: tradeMap.entrySet()) {
            ds.setSummaryDate(entry.getKey().substring(0,10));
            ds.setSummaryTicker(entry.getKey().substring(10));
            ds.setProfitLoss(entry.getValue());
            System.out.println("ds obj: "+ds.toString());
            dateSummaryRepository.save(ds);
        }

    }
}
