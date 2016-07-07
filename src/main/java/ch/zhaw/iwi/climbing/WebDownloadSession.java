package ch.zhaw.iwi.climbing;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fmila.sportident.DownloadSession;
import com.fmila.sportident.bean.Punch;

public class WebDownloadSession implements DownloadSession {

	private String url;
	private Scanner scanner;
	private Boolean enableSiacAirMode;

	private final static Logger LOGGER = Logger.getLogger(WebDownloadSession.class.getName());

	public WebDownloadSession(String url, Scanner scanner, Boolean enableSiacAirMode) {
		super();
		this.url = url;
		this.scanner = scanner;
		this.enableSiacAirMode = enableSiacAirMode;
	}

	@Override
	public boolean handleCardInserted(String eCardNo) {
		System.out.println("ReadECard" + ": " + eCardNo);
		return true;
	}

	@Override
	public boolean handleData(String stationNo, String cardNo, List<Punch> controlData) {
		StringBuilder punchList = new StringBuilder();
		for (Punch p : controlData) {
			punchList.append(p.getControlNo());
			punchList.append("-");
			punchList.append(p.getRawTime());
			punchList.append("/");
		}
		System.out.println("Read Controls: " + punchList.toString());

		// send URL to localhost server
		try {
			if (url.length() > 0) {
				String request = url + cardNo + "/" + punchList.toString();
				// TODO add stationNo to request, parse request result and set SIAC on/off
				System.out.println("Send request GET " + request);
				URL u = new URL(request);
				Scanner urlScanner = new Scanner(u.openStream());
				String r = urlScanner.useDelimiter("\\Z").next();
				urlScanner.close();
				System.out.println("http result: " + r);
			} else {
				System.out.println("*************************");
				System.out.println("Debug mode, Station Number: " + stationNo + ", Card Number: " + cardNo);
				for (Punch punch : controlData) {
					System.out.println("Sort Code: " + punch.getSortCode());
					System.out.println("Control Number: " + punch.getControlNo());
					System.out.println("Raw time (ms): " + punch.getRawTime());
					Instant instant = Instant.ofEpochMilli(punch.getRawTime());
					System.out.println("Formatted time: " + LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toString() + "." + String.format("%03d", punch.getRawTime() % 1000));
				}
				System.out.println("*************************");
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed handling data", e);
		}

		return true;
	}
	
	@Override
	public Boolean enableSiacAirMode() {
		return enableSiacAirMode;
	}

	@Override
	public void handleCardRemoved() {
		System.out.println("===> Waiting for cards ([q] for quit/exit): ");
	}

	@Override
	public void waitForCards() {
		System.out.println("===> Waiting for cards ([q] for quit/exit): ");
		scanner.next();
	}

	@Override
	public void close() {
		System.out.println("Finished.");
	}

}
