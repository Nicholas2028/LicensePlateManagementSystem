import java.util.Arrays;
import java.util.Random;

/**
 * Plates.java
 * 
 * A license plate management system that supports generating, validation,
 *  incrementing, and analyzing license plates based on serial formats.
 */
public class Plates {

	public static void main(String[] args) {
		// Increments a plate to the next in the series
		System.out.println(nextPlate("215CF2"));

		// Generates a random plate based on a 
		// serial format and expiration month
		System.out.println(createRandomPlate("112AA1", 3));

		// Retrusn the serial format of a given plate
		System.out.println(getSerial("21GBH4"));

		// Checks if a plate is a legal vanity plate
		System.out.println(isLegalVanityPlate("ABC12"));

		// Returns all plates that match a given partial string
		String[] plates = {"21GBH4", "399ZZ9", "ABC123"};
		System.out.println(Arrays.toString(matchPlate("AB-23", plates)));
	}

	//	Given a string representing a serial format and an integer corresponding to the month of vehicle
	//	expiration, return a randomly generated plate adhering to the serial format.
	public static String createRandomPlate(String serial, int month) {
		Random rand = new Random(); // Creates random object to generate numbers random;y
		String plate = ""; // Starts the plate with an empty string

		// Loops through each character in the serial string
		for (int i = 0; i < serial.length(); i++) {
			char c = serial.charAt(i);

			// checks if the character is a digit
			if (Character.isDigit(c)) { 
				if (i == 0) {
					// If this is the first character, generates a rnadom digit between 1-9
					plate += (char) ('1' + rand.nextInt(9));
				} else {
					// If it is not the first character, generates a random digit between 0-9
					plate += (char) ('0' + rand.nextInt(10));
				}
			} 
			// Checks if the character is a letter
			else if (Character.isLetter(c)) {
				// Generates a random letter from A to Z
				plate += (char) ('A' + rand.nextInt(26));
			} 
		}
		// Modifies the plate to replace the last character with the value of month
		plate = plate.substring(0, plate.length() - 1) + month;

		return plate;
 	}

	//	Given a string representing a license plate,
	//	return a new string representing the incremented, next plate in the series.
	public static String nextPlate(String plate) {
		// Converts the inputted plate toa chracter array
		char[] plateArray = plate.toCharArray();

		// Loops through the plateArray
		for (int i = plateArray.length - 1; i >= 0; i--) {
			char c = plateArray[i];

			// Runs if the given character is a digit
			if (Character.isDigit(c)) {
				if (c == '9') {
					plateArray[i] = '0';
				} else {
					// INcrements the digit by 1
					plateArray[i] = (char) (c + 1);
					return new String(plateArray);
				}
			}
			// Runs if the character is a letter
			else if (Character.isLetter(c)) {
				if (c == 'Z') {
					plateArray[i] = 'A';
				} else {
					// Increments the letter by 1
					plateArray[i] = (char) (c + 1);
					return new String(plateArray);
				}
			}
		}
		return "error";
	}

	//	Given a plate string, return a string corresponding to the serial format of that plate
	public static String getSerial(String plate) {
		String format = ""; // Creates an empty string

		for (int i = 0; i < plate.length(); i++) {
			// Accesses the character at the given index
			char c = plate.charAt(i);

			if (Character.isDigit(c)) {
				// Adds 1 to the format string is the character is a digit
				format += '1';
			} 
			else if (Character.isLetter(c)) {
				// Adds A to the format string is the character is a letter
				format += 'A';
			}
		}
		return format;
	}

	//	Given a plate string, return a boolean value denoting whether the plate is a legal vanity plate.
	public static boolean isLegalVanityPlate(String plate) {
		// Checks if the plate length is between 2 and 6
		if (plate.length() < 2 || plate.length() > 6) {
			return false;
		}
		// Checks if the first two characters are letters
		if (!Character.isLetter(plate.charAt(0)) || !Character.isLetter(plate.charAt(1))) {
			return false;
		}
		// Set to track if a digit has been encountered in the plate
		boolean number = false;

		for (int i = 0; i < plate.length(); i++) {
			// Accesses the character at the given index
			char c = plate.charAt(i);

			// Runs if the character is a digit
			if (Character.isDigit(c)) {
				number = true;
			} else if (number) {
				return false;
			}
		}
		// Checks if the last digit is a '0'
		if (plate.charAt(plate.length() - 1) =='0') {
			return false;
		}
		// If none of the following conditions have been met, returns true
		return true;
	} 

	//	Given an array of plate strings, return an array of floats corresponding to the
	//	frequency of each expiration month.
	public static float[] getMonthStats(String[] plate) {
		float[] monthStats = new float[10];

		for (String p : plate) {
			// Gets the last character of the plate
			char monthC = p.charAt(p.length() - 1);
			// Converts the character into an integer
			int month = monthC - '0';

			// Checks to see if the month is valid
			if (month >= 0 && month <= 9) {
				monthStats[month]++;
			}
		}
		float ttl = 0;

		// Sums the total number of plates
		for (int i = 0; i < monthStats.length; i++) {
			ttl += monthStats[i];
		}

		// Divides each element of monthStats by the total
		for (int i = 0; i < monthStats.length; i++) {
			monthStats[i] /= ttl;
		}
		return monthStats;
	}

	//	Given an array of plate strings and an array of serial format strings, return an array of floats
	//	corresponding to the frequency of each serial format.
	public static float[] getSerialStats(String[] plate, String[] serials) {
		float[] stats = new float[serials.length];

		// Loops for each plate in the array
		for (String p : plate) {
			// Gets the serial format of current plate
			String plateS = getSerial(p);

			// Loops through each format
			for (int i = 0; i < serials.length; i++) {
				// If the plate matches the format
				if (plateS.equals(serials[i])) {
					// Increments the counter
					stats[i]++;
					break;
				}
			}
		}

		// Float created for the total number of plates
		float totalP = plate.length;

		// Divides each count by the total number of plates
		for (int i = 0; i < stats.length; i++) {
			stats[i] /= totalP;
		}
		return stats;
	}

	//	Given a partial string and an array of plate strings, return an array of strings corresponding
	//	to the plates that match.
	public static String[] matchPlate(String partial, String[] plates) {
		String[] result = new String[plates.length];
		// Keeps count of how many plates match the partial string
		int count = 0;

		int hyphen = -1;
		
		// Loops throufh each character in the partial string
		for (int i = 0; i < partial.length(); i++) {
			if (partial.charAt(i) == '-') {
				// Sets the hyphen to its respective index
				hyphen = i;
				break;
			}
		}
		// -1 indicates that no hyphen was found, if so this statement runs
		if (hyphen == -1) {
			for (String plate : plates) {
				if (plate.contains(partial)) {
					result[count++] = plate;
				}
			}
		  // If a hyphen is found this runs
		} else {
			// If a hyphen is found this runs
			// String section before the hyphen
			String pre = partial.substring(0, hyphen);
			// String section after the hyphen
			String suf = partial.substring(hyphen + 1);

			for (String plate : plates) {
				// Adds a plate matching these conditions to the results
				if (plate.startsWith(pre) && plate.endsWith(suf)) {
					result[count++] = plate;
				}
			}
		}
		
		return Arrays.copyOf(result, count);
	}
}

