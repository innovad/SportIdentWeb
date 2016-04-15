package ch.zhaw.iwi.climbing;

import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fmila.sportident.DownloadSession;
import com.fmila.sportident.bean.Punch;


public class WebDownloadSession implements DownloadSession {

	private String url;
	private Scanner scanner;
	
	private final static Logger LOGGER = Logger.getLogger(WebDownloadSession.class.getName()); 
	
	public WebDownloadSession(String url, Scanner scanner) {
		super();
		this.url = url;
		this.scanner = scanner;
	}

	@Override
	public boolean handleCardInserted(String eCardNo) {
		System.out.println("ReadECard" + ": " + eCardNo);
		return true;
	}

	@Override
	public boolean handleData(String cardNo, List<Punch> controlData) {
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
			String request = url + "/" + cardNo + "/" + punchList.toString();
					    
			System.out.println("Send request GET " + request);
			URL u = new URL(request);
			Scanner scanner = new Scanner(u.openStream());
			String r = scanner.useDelimiter("\\Z").next();
			scanner.close();
			System.out.println("http result: " + r);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed handling data", e);
		}

		return true;
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
