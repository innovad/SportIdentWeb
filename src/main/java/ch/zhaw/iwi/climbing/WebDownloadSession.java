package ch.zhaw.iwi.climbing;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.util.Scanner;

import com.fmila.sportident.DownloadSession;
import com.fmila.sportident.bean.Punch;


public class WebDownloadSession implements DownloadSession {

	private String url;
	private Scanner scanner;

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
			punchList.append(";");
		}
		System.out.println("Read Controls: " + punchList.toString());

		// send URL to localhost server
		try {
			String request = url + "?card=" + cardNo + "&punches=" + punchList.toString();
			

			// open browser window
			if(Desktop.isDesktopSupported())
			{
				Desktop.getDesktop().browse(new URI(request));
			}			
		    
//			System.out.println("Send request GET " + request);
//			URL u = new URL(request);
//			Scanner scanner = new Scanner(u.openStream());
//			String r = scanner.useDelimiter("\\Z").next();
//			scanner.close();
//			System.out.println("http result: " + r);
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
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
