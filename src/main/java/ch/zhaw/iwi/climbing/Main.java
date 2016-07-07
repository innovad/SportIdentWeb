package ch.zhaw.iwi.climbing;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fmila.sportident.DownloadPorts;
import com.fmila.sportident.DownloadStation;
import com.fmila.sportident.Version;

public class Main {

	private final static String SERIALPORT = "serialport";
	private final static String URL = "url";

	public static void main(String[] args) throws Exception {
		System.out.println("Welcome to 4milaSI " + Version.getVersion() + " / " + " ClimberChallengeSI " + ch.zhaw.iwi.climbing.Version.getVersion());

		Map<String, String> parameters = parseArguments(args);
		Scanner scanner = new Scanner(System.in);

		// List Serial Ports
		String selectedPort = parameters.get(SERIALPORT);
		if (selectedPort == null) {
			selectedPort = System.getProperty(SERIALPORT);
			if (selectedPort == null) {
				selectedPort = queryPort(scanner);
			}
		}

		// Ask URL
		String selectedUrl = parameters.get(URL);
		if (selectedUrl == null) {
			selectedUrl = System.getProperty(URL);
			if (selectedUrl == null) {
				selectedUrl = queryUrl(scanner);
			}
		}

		// Ask AirMode
		Boolean enableAirMode = null;
		if (selectedUrl == null || selectedUrl.length() <= 0) {
			String selectedAirMode = queryAirMode(scanner);
			if (selectedAirMode.equalsIgnoreCase("disable")) {
				enableAirMode = false;
			} else if (selectedAirMode.equalsIgnoreCase("enable")) {
				enableAirMode = true;
			}
		}

		DownloadStation station = new DownloadStation(selectedPort, 38400, null, new WebDownloadSession(selectedUrl, scanner, enableAirMode));
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

		String testUrl1 = "http://localhost/climber-challenge/src/backend/service.php/scan/lease/";
		String testUrl2 = "http://localhost/climber-challenge/src/backend/service.php/scan/upload/";
		String testUrl3 = "http://aranea-dev.herokuapp.com/src/backend/service.php/scan/lease/";
		String testUrl4 = "http://aranea-dev.herokuapp.com/src/backend/service.php/scan/upload/";
		String testUrl5 = "http://aranea.herokuapp.com/src/backend/service.php/scan/lease/";
		String testUrl6 = "http://aranea.herokuapp.com/src/backend/service.php/scan/upload/";

		System.out.println("Select an URL: ");
		System.out.println("[d] debug mode, prints to console");
		System.out.println("[1] " + testUrl1);
		System.out.println("[2] " + testUrl2);
		System.out.println("[3] " + testUrl3);
		System.out.println("[4] " + testUrl4);
		System.out.println("[5] " + testUrl5);
		System.out.println("[6] " + testUrl6);
		System.out.println("[q] quit/exit");
		System.out.println("or enter another URL: ");
		selectedUrl = scanner.nextLine();
		if (selectedUrl.toLowerCase().equals("q")) {
			System.out.println("Finished.");
			System.exit(0);
		}
		if (selectedUrl.toLowerCase().equals("d")) {
			selectedUrl = "";
		} else if (selectedUrl.toLowerCase().equals("1")) {
			selectedUrl = testUrl1;
		} else if (selectedUrl.toLowerCase().equals("2")) {
			selectedUrl = testUrl2;
		} else if (selectedUrl.toLowerCase().equals("3")) {
			selectedUrl = testUrl3;
		} else if (selectedUrl.toLowerCase().equals("4")) {
			selectedUrl = testUrl4;
		} else if (selectedUrl.toLowerCase().equals("5")) {
			selectedUrl = testUrl5;
		} else if (selectedUrl.toLowerCase().equals("6")) {
			selectedUrl = testUrl6;
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
		String selectedPort = scanner.nextLine();
		if (selectedPort.toLowerCase().equals("q")) {
			System.out.println("Finished.");
			System.exit(0);
		}
		return selectedPort;
	}

	private static String queryAirMode(Scanner scanner) {
		String selectedAirMode = "none";

		String airMode1 = "Enable Air Mode";
		String airMode2 = "Disable Air Mode";
		String airMode3 = "None";

		System.out.println("Select an Air Mode: ");
		System.out.println("[e] " + airMode1);
		System.out.println("[d] " + airMode2);
		System.out.println("[n] " + airMode3);
		System.out.println("[q] quit/exit");
		selectedAirMode = scanner.next("[a-zA-Z]");
		if (selectedAirMode.toLowerCase().equals("q")) {
			System.out.println("Finished.");
			System.exit(0);
		}
		if (selectedAirMode.toLowerCase().equals("e")) {
			selectedAirMode = "enable";
		} else if (selectedAirMode.toLowerCase().equals("d")) {
			selectedAirMode = "disable";
		}
		return selectedAirMode;
	}

	private static void exitWithError(String message) {
		System.out.println(message);
		System.out.println("USAGE: -Dserialport=COMXY -Durl=http://localhost/index.php");
		System.exit(1);
	}

}