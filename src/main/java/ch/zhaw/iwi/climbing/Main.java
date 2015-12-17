package ch.zhaw.iwi.climbing;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fmila.sportident.DownloadPorts;
import com.fmila.sportident.DownloadStation;

public class Main {

	private final static String SERIALPORT = "serialport";
	private final static String URL = "url";

	public static void main(String[] args) throws Exception {

		Map<String, String> parameters = parseArguments(args);

		Scanner scanner = new Scanner(System.in);

		// List Serial Ports
		String selectedPort = parameters.get(SERIALPORT);
		if (selectedPort == null) {
			selectedPort = queryPort(scanner);
		}

		// Ask URL
		String selectedUrl = parameters.get(URL);
		if (selectedUrl == null) {
			selectedUrl = queryUrl(scanner);
		}

		DownloadStation station = new DownloadStation(selectedPort, 38400, new WebDownloadSession(selectedUrl, scanner));
		station.open();

		scanner.close();
	}

	private static Map<String, String> parseArguments(String[] args) {
		Map<String, String> parameters = new HashMap<>();
		for (String arg : args) {
			if (arg.contains("=")) {
				String key = arg.substring(1, arg.indexOf("="));
				if (SERIALPORT.equalsIgnoreCase(key) || URL.equalsIgnoreCase(key)) {
					parameters.put(key, arg.substring(arg.indexOf("=") + 1));
				} else {
					exitWithError("Unkwown command-line option: " + arg);
				}
			} else {
				exitWithError("Unkwown command-line option: " + arg);
			}
		}
		return parameters;
	}

	private static String queryUrl(Scanner scanner) {
		String selectedUrl;
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
		selectedUrl = scanner.next();
		if (selectedUrl.toLowerCase().equals("q")) {
			System.out.println("Finished.");
			System.exit(0);
		}
		if (selectedUrl.toLowerCase().equals("1")) {
			selectedUrl = testUrl1;
		} else if (selectedUrl.toLowerCase().equals("2")) {
			selectedUrl = testUrl2;
		} else if (selectedUrl.toLowerCase().equals("3")) {
			selectedUrl = testUrl3;
		} else if (selectedUrl.toLowerCase().equals("4")) {
			selectedUrl = testUrl4;
		}
		return selectedUrl;
	}

	private static String queryPort(Scanner scanner) {
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
		return selectedPort;
	}

	private static void exitWithError(String message) {
		System.out.println(message);
		System.out.println("USAGE: -Dserialport=COMXY -Durl=http://localhost/index.php");
		System.exit(1);
	}

}