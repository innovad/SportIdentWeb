package ch.zhaw.iwi.climbing;

import java.util.Map;
import java.util.Scanner;

import com.fmila.sportident.DownloadPorts;
import com.fmila.sportident.DownloadStation;

public class Main {

	public static void main(String[] args) throws Exception {

		Scanner scanner = new Scanner(System.in);

		// List Serial Ports
		System.out.println("Scanning serial/USB ports...");
		Map<String, String> friendlyNames = DownloadPorts.getPorts();
		for (String port : friendlyNames.keySet()) {
			System.out.println("[" + port + "]" + " (" + friendlyNames.get(port) + ")");
		}

		// Ask Port
		System.out.println("Select a port [...] or [q] for quit/exit: ");
		String selectedPort = scanner.next();
		if (selectedPort.toLowerCase().equals("q")) {
			System.out.println("Finished.");
			System.exit(0);
		}

		// Ask URL
		String testUrl1 = "http://localhost:81/KletterhalleProjekt/functions/upload/lease.php";
		String testUrl2 = "http://localhost:81/KletterhalleProjekt/functions/upload/upload.php";
		String testUrl3 = "http://localhost/kletterhalle/upload.php";
		String testUrl4 = "http://localhost/KletterhalleProjekt/functions/upload/upload.php";
		
		System.out.println("Select an URL: ");
		System.out.println("[1] " + testUrl1);
		System.out.println("[2] " + testUrl2);
		System.out.println("[3] " + testUrl3);
		System.out.println("[4] " + testUrl4);
		System.out.println("[q] quit/exit");
		System.out.println("or enter another URL: ");
		String selectedUrl = scanner.next();
		if (selectedUrl.toLowerCase().equals("q")) {
			System.out.println("Finished.");
			System.exit(0);
		}
		if (selectedUrl.toLowerCase().equals("1")) {
			selectedUrl = testUrl1;
		}
		else if (selectedUrl.toLowerCase().equals("2")) {
			selectedUrl = testUrl2;
		}
		else if (selectedUrl.toLowerCase().equals("3")) {
			selectedUrl = testUrl3;
		}
		else if (selectedUrl.toLowerCase().equals("4")) {
			selectedUrl = testUrl4;
		}

		DownloadStation station = new DownloadStation(selectedPort, 38400, new WebDownloadSession(selectedUrl, scanner));
		station.open();
		
		scanner.close();
	}
}