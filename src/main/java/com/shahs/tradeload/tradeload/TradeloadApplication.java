package com.shahs.tradeload.tradeload;

import com.shahs.tradeload.tradeload.model.DateSummary;
import com.shahs.tradeload.tradeload.model.Trade;
import com.shahs.tradeload.tradeload.repository.TradeRepository;
import com.shahs.tradeload.tradeload.service.HelloMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.exit;
@SpringBootApplication
public class TradeloadApplication implements CommandLineRunner {

	@Autowired
	TradeRepository tradeRepository;

	@Autowired
	private HelloMessageService helloMessageService;

	public static void main(String[] args) throws Exception {

		SpringApplication app = new SpringApplication(TradeloadApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}



	// Put your logic here.
	@Override
	public void run(String... args) throws Exception {

		helloMessageService.printCsvFile();
		//got array
//		String[] arr = {
//				"\"10/23/2020\",\"Buy\",\"EXPI\",\"EXP WORLD HLDGS INC\",\"20\",\"$44.745\",\"\",\"-$894.90\"",
//				"\"10/23/2020\",\"Sell\",\"EXPI\",\"EXP WORLD HLDGS INC\",\"20\",\"$45.745\",\"\",\"$914.90\""
//				};
//// 			created map to store key and value
//		Map<String, Double> myMap = new ConcurrentHashMap<String, Double>();
//		// new object of DateSummary
//		DateSummary ds = new DateSummary();
	// iterate through the array and discard extra symbols
//		Arrays.stream(arr).forEach(e -> {
//			String[] arr1 = e.replace("\"","")
//					.replace("$","")
//					.split(",");
//			// created plain trade object and added into trades
//			Trade trade = Trade.createTradeObj(arr1);
//			//created a key -- date+ticker
//			String key = trade.getDate() + trade.getTicker();
//			// checking if old value is there,
//			// if yes then get new value for the same key
//			// and add up
//			// then put into the map
//			Double oldVal = 0.0;
//			if(myMap.containsKey(key)) {
//				oldVal = myMap.get(key); // getting the current value of this key
//			}
//			myMap.put(key, oldVal +  trade.getAmount());
//			System.out.println("abc");
//
//		});

		// so we have map of key-- for example: 11/20/2020EXP and pnlvalue
//		for(Map.Entry<String, Double> entry: myMap.entrySet()) {
//			System.out.println("Key : "+ entry.getKey() + ", Value : " + entry.getValue());
//			// here we r breaking key and separating date and tricker
//			// and setting respictive feilds with in into DateSummary object
//			// as well as pnlValue too.
//			ds.setSummaryDate(entry.getKey().substring(0,10));
//			ds.setSummaryTicker(entry.getKey().substring(10));
//			ds.setProfitLoss(entry.getValue());
//			System.out.println("ds obj: "+ds.toString());
//
//		}


		exit(0);
	}

}
