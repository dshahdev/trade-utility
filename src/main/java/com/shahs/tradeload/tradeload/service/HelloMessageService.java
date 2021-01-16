package com.shahs.tradeload.tradeload.service;

import com.shahs.tradeload.tradeload.model.*;
import com.shahs.tradeload.tradeload.repository.*;
import com.shahs.tradeload.tradeload.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class HelloMessageService {

    public static final String DATE_FORMAT_MDY= "MM/dd/yyyy";
    public static final String DATE_FORMAT_YMD= "yyyy/MM/dd";
    public static final String DATE_FORMAT_YMD_NO_SLASH = "yyyyMMdd";

    String line = "";
    String splitBy=",";

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    DateSummaryRepository dateSummaryRepository;

    @Autowired
    ParameterRepository parameterRepository;

    @Autowired
    ConsumptionRepository consumptionRepository;

    @Autowired
    AllocRepository allocRepository;

    SystemParameters sysParams = null;


    // lastAllocatedSellId

    public void LoadParameters()  {
        System.out.println("1) In loadparams: "+parameterRepository.findAll());
        Iterable<Parameter> param = parameterRepository.findAll();

        sysParams = new SystemParameters();

        // fill sysParams:
        param.forEach( p-> {
            if (p.getParamKey().equals("lastDate")) {
                System.out.println("loaded parmas: "+ p);
                sysParams.setLastDateStr(p.getParamValue());
                sysParams.setLastDate(MiscUtils.stringToDate(p.getParamValue(),DATE_FORMAT_MDY));
            }
        });

    }

    public void SaveParameters() {
        Parameter ps = new Parameter();
        ps.setParamKey("lastDate");
        ps.setParamValue(sysParams.getLastDateStr());
        parameterRepository.save(ps);
    }
    public boolean checkDate(java.util.Date date) {

        if (sysParams.getLastDateStr().equals("")) {    // this means nothing is loaded in sysParams variable
            sysParams.setLastDateStr(MiscUtils.dateToString(date,DATE_FORMAT_MDY));
            sysParams.setLastDate(date);
        } else {
            if (sysParams.getLastDate().after(date) || sysParams.getLastDate().equals(date)) {
                System.out.println("date already loaded in the system, cannot load again");
                return false;
            }
        }
        return  true;

    }
    public List<Consumption> loadConsumption() {
        List<Consumption> consumption = new ArrayList<Consumption>();;
        consumptionRepository.findAllConsumptionData().forEach(c -> {
            System.out.println("consumption: "+ c.toString());
            consumption.add(c);
        });

        return consumption;
    }
    public void printCsvFile(String inputPath, String csvFile) throws IOException{
        java.util.Date runDate = MiscUtils.stringToDate(csvFile.substring(10,18), DATE_FORMAT_YMD_NO_SLASH);
        System.out.println(csvFile.substring(10,18));
        // load parameters so we have parameter last date
        LoadParameters();

        // check date passed against parameter date
        // if date passed is less than or equal to parameter date then ERROR date already loaded, cannot re-load the same date

        checkDate(runDate);
        if(!checkDate(runDate)) { return; }

        // load trades here i.e. calculate ...
        loadTrades(inputPath + csvFile);
//        loadTrades(csvFile);

        generateAlloc(runDate);

        // save the date that was just run
        sysParams.setLastDateStr(MiscUtils.dateToString(runDate,DATE_FORMAT_MDY));
        sysParams.setLastDate(runDate);
        SaveParameters();

    }
    public void generateAlloc(java.util.Date runDate) {
        // generate alloc data
        List<Alloc> allocList = new ArrayList<Alloc>();

        // load consumption data
        List<Consumption> consumptionsData = loadConsumption();
        System.out.println("consumptionsData: " + consumptionsData);

        // load sell trades for today's date

        List<Trade> tradeList = tradeRepository.findSellTradesForDate(runDate);
        tradeList.forEach(td -> {
            System.out.println("sell entry: "+ td);
            //this sell needs to be allocated
            // what we have -- quantity to allocate
            // find available quantity from consumption data
            int qtyToAllocate = td.getQuantity();
            int allocatedQty = 0;
            for(Consumption c: consumptionsData) {
                if(c.getTicker().equals(td.getTicker()) && c.getAvailableQty() > 0) {
                    if(c.getAvailableQty() >= qtyToAllocate) {

                        allocatedQty += qtyToAllocate;

                        Alloc a = new Alloc();
                        a.setAllocatedQty(qtyToAllocate);
                        a.setBuyTradeId(c.getId());
                        a.setSellTradeId(td.getId());

                        allocList.add(a);

                        c.setAllocatedQty(qtyToAllocate);
                        c.setAvailableQty(c.getOriginalQty() - qtyToAllocate);

                    } else if(c.getAvailableQty() < qtyToAllocate) {

                        allocatedQty += c.getAvailableQty();
                        qtyToAllocate = qtyToAllocate - c.getAvailableQty();

                        Alloc a = new Alloc();
                        a.setAllocatedQty(c.getAvailableQty());
                        a.setBuyTradeId(c.getId());
                        a.setSellTradeId(td.getId());

                        allocList.add(a);

                        c.setAllocatedQty(c.getAvailableQty());
                        c.setAvailableQty(c.getOriginalQty() - c.getAvailableQty());
                    }
                }
                if(qtyToAllocate == 0) {
                    break;
                }
            };
        });

        allocList.forEach(allist -> {
            System.out.println("saving alloc record: "+allist);
            allocRepository.save(allist);
        });


    }
    //Trades file
    public void loadTrades(String csvFile) throws IOException {
        Map<String, Double> tradeMap = new ConcurrentHashMap<String, Double>();

        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        String line = br.readLine();
        String something = br.readLine();
        System.out.println(line + " " + something);
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
            System.out.println("final trades: "+trade.toString());


            tradeRepository.save(trade);

        }

        // so now we have hashmap with key-- date+ticker, value pnlVal
        // now we r iterating throw this hashmap and separating date, ticker and pnlvalue
        // and create the object of DateSummary
//        for(Map.Entry<String, Double> entry: tradeMap.entrySet()) {
//            DateSummary ds = new DateSummary();
//            ds.setSummaryDate(entry.getKey().substring(0,10));
//            ds.setSummaryTicker(entry.getKey().substring(10));
//            ds.setProfitLoss(entry.getValue());
//            System.out.println("ds obj: "+ds.toString());
//            dateSummaryRepository.save(ds);
//        }

       }


}
