package com.shahs.tradeload.tradeload;

import com.shahs.tradeload.tradeload.repository.TradeRepository;
import com.shahs.tradeload.tradeload.service.HelloMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.exit;
@SpringBootApplication
public class TradeloadApplication implements CommandLineRunner {

	@Value("${TRADELOADER_INPUT_PATH}")
	private String inputPath;

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

		if(args.length >= 1) {
			helloMessageService.printCsvFile(inputPath, args[0].toString());
		}
		 else {
			System.out.println("usage: program filename (must have TRADEDATA.YYYYMMDD.CSV)");
		}
//		helloMessageService.printCsvFile();
		exit(0);
	}

}
