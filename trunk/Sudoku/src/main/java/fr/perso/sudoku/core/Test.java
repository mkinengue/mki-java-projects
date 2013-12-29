package fr.perso.sudoku.core;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

	private static final Map<String, String> MAP_CHAINES_ATTENDUES = new HashMap<String, String>();
	static {
		MAP_CHAINES_ATTENDUES.put("LinkedMicrocondition", "microconditions");
		MAP_CHAINES_ATTENDUES.put("titi", "toto");
	}

	private static final String[] MAP_INDEX = new String[] { "i", "j", "k", "l", "m", "n", "o", "p", "q" };

	private static final List<String> LIST_STRING = new ArrayList<String>();
	static {
		LIST_STRING.add("int");
		LIST_STRING.add("string");
	}

	private static String getPluriel(final String str) {
		return str + "s";
	}

	private static boolean isPluriel(final String str1, final String pluriel) {
		if (str1 == null || pluriel == null) {
			return false;
		}
		return getPluriel(str1).toLowerCase().equals(pluriel.toLowerCase());
	}

	private static String getIndex(final int idx) {
		return "\\[" + MAP_INDEX[idx] + "\\]";
	}

	private static String ajouteIndex(final String str) {
		if (str == null) {
			return null;
		}

		String ret = "";
		final String[] split = str.split("/");
		final int nbFld = split.length;
		int idx = 0;
		int cptFld = 0;
		for (final String fld : split) {
			// Le champ courant est dans la map des chaînes de caractères attendus pour pluriel
			if ((cptFld > 0) && (MAP_CHAINES_ATTENDUES.get(fld) != null)
					&& MAP_CHAINES_ATTENDUES.get(fld).equals(split[cptFld - 1])) {
				// Chaine attendue trouvée, on ajoute l'index à la prochaine itération
				ret = ret + fld + getIndex(idx);
				idx++;
			}
			// On vérifie si le champ courant est pluriel du champ précédent sauf pour le premier élément
			else if ((cptFld > 0) && isPluriel(split[cptFld - 1], fld)) {
				// On ajoute un index au champ et on augmente l'index
				ret = ret + fld + getIndex(idx);
				idx++;
			}
			// On vérifie que le champ courant est l'une des chaînes de la liste
			else if (LIST_STRING.contains(fld.toLowerCase())) {
				// On ajoute un index et on augmente l'index
				ret = ret + fld + getIndex(idx);
				idx++;
			}
			// Aucune particularité,, on ajoute le champ courant dans la chaîne finale
			else {
				ret = ret + fld;
			}

			cptFld++;
			if (cptFld < nbFld) {
				// Le compteur de champs traités, additionné de un, est encore strictement inférieur au nombre total
				// d'éléments
				ret = ret + "/";
			}
		}

		return ret;
	}

	private static String printHourOrMinuteOrSeconds(final int hourOrMinute) {
		if (hourOrMinute < 10) {
			return "0" + hourOrMinute;
		}
		return String.valueOf(hourOrMinute);
	}

	private static String printMilliems(final int milliems) {
		if (milliems < 10) {
			return "00" + milliems;
		} else if (milliems < 100) {
			return "0" + milliems;
		}
		return String.valueOf(milliems);
	}

	private static int extractHourFromMinutesOrMinutesFromSeconds(final int minutesOrSeconds) {
		if (minutesOrSeconds < 0) {
			throw new RuntimeException("Minute (or second) cannot be negative : " + minutesOrSeconds);
		} else if (minutesOrSeconds < 60) {
			return 0;
		} else {
			int hourOrMinute = 0;
			int tmpMinOrSec = minutesOrSeconds;
			while (tmpMinOrSec >= 60) {
				tmpMinOrSec -= 60;
				hourOrMinute++;
			}
			return hourOrMinute;
		}
	}

	private static int getRemainingMinutesOrSeconds(final int minutesOrSeconds, final int hourOrMinutes) {
		final int hourInMinutesOrMinutesInSeconds = hourOrMinutes * 60;
		if (hourInMinutesOrMinutesInSeconds > minutesOrSeconds) {
			throw new RuntimeException(
					"Number of hours (or minutes) too great for the number of minutes (or seconds). Hour (or minutes) = "
							+ hourOrMinutes + " - Minutes (or seconds) = " + minutesOrSeconds);
		}
		return minutesOrSeconds - hourInMinutesOrMinutesInSeconds;
	}

	private static int extractSecondssFromMilliem(final int milliem) {
		if (milliem < 0) {
			throw new RuntimeException("Milliem cannot be negative : " + milliem);
		} else if (milliem < 1000) {
			return 0;
		} else {
			int seconds = 0;
			int tmpMilliem = milliem;
			while (tmpMilliem >= 1000) {
				tmpMilliem -= 1000;
				seconds++;
			}
			return seconds;
		}
	}

	private static int getRemainingMilliem(final int milliems, final int seconds) {
		final int secondsInMilliem = seconds * 1000;
		if (secondsInMilliem > milliems) {
			throw new RuntimeException("Number of seconds too great for the number of milliems. Seconds = " + seconds
					+ " - Milliems = " + milliems);
		}
		return milliems - milliems;
	}

	/**
	 * 
	 * @param subtitleDate : format = hh:mm:ss,mmm
	 * @return
	 */
	private static String addMinutesToSubtitleDate(final String subtitleDate, final int minutesToAdd) {
		final String[] split = subtitleDate.split(":");
		final String[] split2 = split[2].split(",");
		final int hour = Integer.parseInt(split[0]);
		final int minutes = Integer.parseInt(split[1]);
		final int seconds = Integer.parseInt(split2[0]);
		final int milliems = Integer.parseInt(split2[1]);

		int newMinutes = minutes + minutesToAdd;
		final int hourExtracted = extractHourFromMinutesOrMinutesFromSeconds(newMinutes);
		final int newHour = hour + hourExtracted;
		newMinutes = getRemainingMinutesOrSeconds(newMinutes, hourExtracted);

		final StringBuffer sb = new StringBuffer(printHourOrMinuteOrSeconds(newHour)).append(":")
				.append(printHourOrMinuteOrSeconds(newMinutes)).append(":").append(printHourOrMinuteOrSeconds(seconds))
				.append(",").append(printMilliems(milliems));
		return sb.toString();
	}

	/**
	 * 
	 * @param subtitleDate : format = hh:mm:ss,mmm
	 * @return
	 */
	private static String addSecondsToSubtitleDate(final String subtitleDate, final int secondsToAdd) {
		final String[] split = subtitleDate.split(":");
		final String[] split2 = split[2].split(",");
		final int hour = Integer.parseInt(split[0]);
		final int minutes = Integer.parseInt(split[1]);
		final int seconds = Integer.parseInt(split2[0]);
		final int milliems = Integer.parseInt(split2[1]);

		// Managing seconds
		int newSeconds = seconds + secondsToAdd;
		final int minutesExtracted = extractHourFromMinutesOrMinutesFromSeconds(newSeconds);
		int newMinutes = minutes + minutesExtracted;
		newSeconds = getRemainingMinutesOrSeconds(newSeconds, minutesExtracted);

		// Managing minutes
		final int hourExtracted = extractHourFromMinutesOrMinutesFromSeconds(newMinutes);
		final int newHour = hour + hourExtracted;
		newMinutes = getRemainingMinutesOrSeconds(newMinutes, hourExtracted);

		final StringBuffer sb = new StringBuffer(printHourOrMinuteOrSeconds(newHour)).append(":")
				.append(printHourOrMinuteOrSeconds(newMinutes)).append(":")
				.append(printHourOrMinuteOrSeconds(newSeconds)).append(",").append(printMilliems(milliems));
		return sb.toString();
	}

	/**
	 * 
	 * @param subtitleDate : format = hh:mm:ss,mmm
	 * @return
	 */
	private static String addMilliemsToSubtitleDate(final String subtitleDate, final int milliemsToAdd) {
		final String[] split = subtitleDate.split(":");
		final String[] split2 = split[2].split(",");
		final int hour = Integer.parseInt(split[0]);
		final int minutes = Integer.parseInt(split[1]);
		final int seconds = Integer.parseInt(split2[0]);
		final int milliems = Integer.parseInt(split2[1]);

		// Managing milliems
		int newMilliems = milliems + milliemsToAdd;
		final int secondsExtracted = extractSecondssFromMilliem(newMilliems);
		newMilliems = getRemainingMilliem(newMilliems, secondsExtracted);

		// Managing seconds
		int newSeconds = seconds + secondsExtracted;
		final int minutesExtracted = extractHourFromMinutesOrMinutesFromSeconds(newSeconds);
		newSeconds = getRemainingMinutesOrSeconds(newSeconds, minutesExtracted);

		// Managing minutes
		int newMinutes = minutes + minutesExtracted;
		final int hourExtracted = extractHourFromMinutesOrMinutesFromSeconds(newMinutes);
		final int newHour = hour + hourExtracted;
		newMinutes = getRemainingMinutesOrSeconds(newMinutes, hourExtracted);

		final StringBuffer sb = new StringBuffer(printHourOrMinuteOrSeconds(newHour)).append(":")
				.append(printHourOrMinuteOrSeconds(newMinutes)).append(":")
				.append(printHourOrMinuteOrSeconds(newSeconds)).append(",").append(printMilliems(milliems));
		return sb.toString();
	}

	public static void main(final String[] args) throws ParseException {
		// final String test1 = "toto/titi/tutu/Tutus/test";
		// final String test2 = "toto/titi/string";
		// final String test3 = "toto/titi/int";
		// final String test4 = "toto/titi/tutu/Tutus";
		// final String test5 = "toto/titi/tutu/Tutus/test/int";
		// final String test6 = "toto/titi/tutu/Tutus/int";
		// final String test7 = "toto/microconditions/LinkedMicrocondition/titi/tutu/Tutus/int";
		//
		// // final String[] split = test1.split("/");
		// // for (final String string : split) {
		// // System.out.println(string);
		// // }
		// System.out.println(ajouteIndex(test1));
		// System.out.println(ajouteIndex(test2));
		// System.out.println(ajouteIndex(test3));
		// System.out.println(ajouteIndex(test4));
		// System.out.println(ajouteIndex(test5));
		// System.out.println(ajouteIndex(test6));
		// System.out.println(ajouteIndex(test7));

		// final DateFormat instance = DateFormat.getInstance();
		// final Date parse = instance.parse("00:17:57,604");
		// System.out.println(parse);

		final String[] split = "00:17:57,604".split(":");
		final Calendar cal = Calendar.getInstance();

	}
}
