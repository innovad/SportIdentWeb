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
		String testUrl = "http://localhost/kletterhalle/upload.php";
		System.out.println("Select an URL: ");
		System.out.println("[d] " + testUrl);
		System.out.println("[q] quit/exit");
		System.out.println("or enter another URL: ");
		String selectedUrl = scanner.next();
		if (selectedUrl.toLowerCase().equals("q")) {
			System.out.println("Finished.");
			System.exit(0);
		}
		if (selectedUrl.toLowerCase().equals("d")) {
			selectedUrl = testUrl;
		}

		DownloadStation station = new DownloadStation(selectedPort, 38400, new WebDownloadSession(selectedUrl, scanner));
		station.open();
		
		scanner.close();
	}
}