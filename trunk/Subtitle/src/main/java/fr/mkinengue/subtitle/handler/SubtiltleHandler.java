package fr.mkinengue.subtitle.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;

/**
 * Handle subtitles files with format defined in the list SUBTITLE_EXTENSIONS.<br />
 * Copy the subtitle file and allow to add any hour, minute second or thousandth in a whole file or from a specified
 * comment line number<br />
 * While initializing the class, the input file has to be provided with the extension otherwise parsing the file will
 * fail
 * 
 * @author mkinengue
 */
public class SubtiltleHandler {

	private final String subtitleFile;

	private static final String SUFFIX_COPIED_FILE = "_COPY";

	private static final int MAX_TO_FLUSH = 1000;

	/** List of managed extensions of subtitle files */
	private static final List<String> SUBTITLE_EXTENSIONS = new ArrayList<String>();
	static {
		SUBTITLE_EXTENSIONS.add("srt");
	}

	/**
	 * Constructor which initializes the subtitle file to work on
	 * 
	 * @param subtitleFile
	 */
	public SubtiltleHandler(final String subtitleFile) {
		this.subtitleFile = subtitleFile;
	}

	/**
	 * Being given a time from a subtitle (with format hh:mm:ss,mmm) as first parameter of the function, the method adds
	 * the number of hours given by the second parameter<br />
	 * Returns the subtitle time added with the corresponding number of hours.
	 * 
	 * @param subtitleTime : format = hh:mm:ss,mmm
	 * @return String
	 * @throws Exception
	 */
	private String addHoursToSubtitleTime(final String subtitleTime, final int hoursToAdd) throws Exception {
		final String[] split = subtitleTime.split(":");
		final String[] split2 = split[2].split(",");
		final int hour = Integer.parseInt(split[0]);
		final int minutes = Integer.parseInt(split[1]);
		final int seconds = Integer.parseInt(split2[0]);
		final int thousandths = Integer.parseInt(split2[1]);

		final int newHour = hour + hoursToAdd;

		// TODO @MKI Manage negative hoursToAdd

		final StringBuffer sb = new StringBuffer(printHourOrMinuteOrSeconds(newHour)).append(":")
				.append(printHourOrMinuteOrSeconds(minutes)).append(":").append(printHourOrMinuteOrSeconds(seconds))
				.append(",").append(printThousandth(thousandths));
		return sb.toString();
	}

	/**
	 * Being given a time from a subtitle (with format hh:mm:ss,mmm) as first parameter of the function, the method adds
	 * the number of minutes given by the second parameter<br />
	 * Returns the subtitle time added with the corresponding number of minutes. Manage the fact that the addition can
	 * result in a number of minutes greater or equal than 60 :<br />
	 * In such case, the minute is converted in hours and minutes and the hours obtained are added to the already
	 * existing hours of the time of the subtitle
	 * 
	 * @param subtitleTime : format = hh:mm:ss,mmm
	 * @return String
	 * @throws Exception
	 */
	private String addMinutesToSubtitleTime(final String subtitleTime, final int minutesToAdd) throws Exception {
		final String[] split = subtitleTime.split(":");
		final String[] split2 = split[2].split(",");
		final int hour = Integer.parseInt(split[0]);
		final int minutes = Integer.parseInt(split[1]);
		final int seconds = Integer.parseInt(split2[0]);
		final int thousandths = Integer.parseInt(split2[1]);

		int newMinutes = minutes + minutesToAdd;
		final int hourExtracted = extractHourFromMinutesOrMinutesFromSeconds(newMinutes);
		final int newHour = hour + hourExtracted;
		newMinutes = getRemainingMinutesOrSeconds(newMinutes, hourExtracted);

		final StringBuffer sb = new StringBuffer(printHourOrMinuteOrSeconds(newHour)).append(":")
				.append(printHourOrMinuteOrSeconds(newMinutes)).append(":").append(printHourOrMinuteOrSeconds(seconds))
				.append(",").append(printThousandth(thousandths));
		return sb.toString();
	}

	/**
	 * Being given a time from a subtitle (with format hh:mm:ss,mmm) as first parameter of the function, the method adds
	 * the number of seconds given by the second parameter<br />
	 * Returns the subtitle time added with the corresponding number of seconds. Manage the fact that the addition can
	 * result in a number of seconds greater or equal than 60 :<br />
	 * In such case, the second is converted in minutes and seconds and the minutes obtained are added to the already
	 * existing minutes of the time of the subtitle.<br />
	 * In case the number of minutes exceed 60, they are converted to hours and minutes and then, the number of hours
	 * are added to the already existing hours of the subtitle time
	 * 
	 * @param subtitleTime : format = hh:mm:ss,mmm
	 * @return String
	 * @throws Exception
	 */
	private String addSecondsToSubtitleTime(final String subtitleTime, final int secondsToAdd) throws Exception {
		final String[] split = subtitleTime.split(":");
		final String[] split2 = split[2].split(",");
		final int hour = Integer.parseInt(split[0]);
		final int minutes = Integer.parseInt(split[1]);
		final int seconds = Integer.parseInt(split2[0]);
		final int thousandths = Integer.parseInt(split2[1]);

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
				.append(printHourOrMinuteOrSeconds(newSeconds)).append(",").append(printThousandth(thousandths));
		return sb.toString();
	}

	/**
	 * Being given a time from a subtitle (with format hh:mm:ss,mmm) as first parameter of the function, the method adds
	 * the number of thousandth given by the second parameter<br />
	 * Returns the subtitle time added with the corresponding number of thousandth. Manage the fact that the addition
	 * can result in a number of thousandth greater or equal than 1000 :<br />
	 * In such case, the thousandth is converted in seconds and thousandth and the seconds obtained are added to the
	 * already existing seconds of the time of the subtitle.<br />
	 * In case the number of seconds exceed 60, they are converted to minutes and seconds and then, the number of
	 * minutes are added to the already existing minutes of the subtitle time.<br />
	 * In case the number of minutes exceed 60, they are converted to hours and minutes and then, the number of hours
	 * are added to the already existing hours of the subtitle time
	 * 
	 * @param subtitleTime : format = hh:mm:ss,mmm
	 * @return String
	 * @throws Exception
	 */
	private String addThousandthsToSubtitleDate(final String subtitleTime, final int thousandthToAdd) throws Exception {
		final String[] split = subtitleTime.split(":");
		final String[] split2 = split[2].split(",");
		final int hour = Integer.parseInt(split[0]);
		final int minutes = Integer.parseInt(split[1]);
		final int seconds = Integer.parseInt(split2[0]);
		final int thousandths = Integer.parseInt(split2[1]);

		// Managing milliems
		int newThousandths = thousandths + thousandthToAdd;
		final int secondsExtracted = extractSecondsFromThousandth(newThousandths);
		newThousandths = getRemainingThousandth(newThousandths, secondsExtracted);

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
				.append(printHourOrMinuteOrSeconds(newSeconds)).append(",").append(printThousandth(newThousandths));
		return sb.toString();
	}

	/**
	 * Check if the file is supported by this application by checking the extension of the file provided
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean checkExtensionFile() throws Exception {
		final String[] split = subtitleFile.split("\\.");
		if (split.length < 2) {
			throw new Exception("No extension file in the file provided as input");
		}
		final String fileExtension = Files.getFileExtension(subtitleFile);
		// final String fileExtension = split[split.length - 1];
		return SUBTITLE_EXTENSIONS.contains(fileExtension);
	}

	/**
	 * Return the parent directory of the subtitle file being managed
	 * 
	 * @return String
	 */
	private String getParentDirectory() {
		final File f = new File(subtitleFile);
		return f.getParent();
	}

	/**
	 * Return the name of the file to copy and to work on with or without the extension depending on whether there is an
	 * extension to the original file name
	 * 
	 * @return String
	 */
	private String getCopyFileName() {
		final File f = new File(subtitleFile);
		String fName = f.getName();
		final String fileExtension = Files.getFileExtension(subtitleFile);
		if (!fileExtension.isEmpty()) {
			// There is an extension, we remove it and get the name
			final int lastIndexOf = fName.lastIndexOf('.');
			fName = fName.substring(0, lastIndexOf);
		}

		return fName + SUFFIX_COPIED_FILE + "." + fileExtension;
	}

	/**
	 * Return the path to the copy of the subtitle file
	 * 
	 * @return String
	 */
	private String getCopyPathName() {
		return getParentDirectory() + File.separator + getCopyFileName();
	}

	/**
	 * Copy the subtitle file to the file to work on after deleting an existing file with the same name<br />
	 * Returns a RuntimeException in case the subtitle file does not exist
	 * 
	 * @throws IOException
	 */
	private void createEmptySubtitleCopiedFile() throws IOException {
		final File originFile = new File(subtitleFile);
		if (!originFile.exists()) {
			throw new RuntimeException("The subtitle file does not exist. Subtitle file : " + subtitleFile);
		}

		final File destFile = new File(getCopyPathName());
		destFile.delete();
		destFile.createNewFile();
		// Files.copy(originFile, new File(getParentDirectory() + File.separator + getCopyFileName()));
	}

	/**
	 * Parse the subtitle file and add from the number of comment fromComment the times given as input
	 * 
	 * @param fromComment
	 * @param hoursToAdd
	 * @param minutesToAdd
	 * @param secondsToAdd
	 * @param thousandthToAdd
	 * @throws IOException
	 */
	private void parseFileAndAddTimeToSubtitle(final int fromComment, final int hoursToAdd, final int minutesToAdd,
			final int secondsToAdd, final int thousandthToAdd) throws IOException {
		final File f = new File(subtitleFile);
		final File fCopy = new File(getCopyPathName());
		if (!f.exists()) {
			throw new RuntimeException("The subtitle file does not exist. Subtitle file : " + subtitleFile);
		}
		if (!fCopy.exists()) {
			throw new RuntimeException("The copy of the subtitle file does not exist. Copy file : " + getCopyPathName());
		}

		BufferedReader newReader = null;
		BufferedWriter newWriter = null;
		try {
			// Creation of the Reader to read the subtitle file
			newReader = Files.newReader(f, Charset.forName("UTF-8"));

			// Creation of the Writer to write in the copy of the subtitle file
			newWriter = Files.newWriter(fCopy, Charset.forName("UTF-8"));

			int cntLineWritten = 0;
			String currentLine = null;
			String previousLine = null;
			String lineToCopy = null;
			boolean beginUpdate = false;
			boolean isPreviousLineANumComment = false;
			while ((currentLine = newReader.readLine()) != null) {
				lineToCopy = currentLine;
				final boolean currentLineIsACommentOne = isCurrentLineANumberOfComment(currentLine, previousLine);
				if (!beginUpdate) {
					// Check the comment line only if the update of time has not begun yet
					if (currentLineIsACommentOne) {
						final int numComment = Integer.parseInt(currentLine);
						if (fromComment == numComment) {
							// Current comment if the one from where must start the update of time
							beginUpdate = true;
						}
					}
				} else if (isPreviousLineANumComment) {
					// The update has already started, we check the current line only if the previous one was a comment
					// one
					lineToCopy = parseTimeLine(currentLine, hoursToAdd, minutesToAdd, secondsToAdd, thousandthToAdd);
				}

				if (currentLineIsACommentOne) {
					final int numComment = Integer.parseInt(currentLine);
					if ((numComment % 100) == 0) {
						System.out.println("Comment line treated = " + numComment);
					}
				}

				// Fill the previous line with current line which has just been managed
				previousLine = currentLine;
				isPreviousLineANumComment = currentLineIsACommentOne;

				// We copy the line in the copy file
				newWriter.write(lineToCopy);
				newWriter.newLine();

				cntLineWritten++;
				if (cntLineWritten >= MAX_TO_FLUSH) {
					System.out.println("Flush");
					// Flush the writer buffer and reinitialize the counter
					newWriter.flush();
					cntLineWritten = 0;
				}
			}

			// We flush the last ones
			newWriter.flush();
		} finally {
			if (newWriter != null) {
				newWriter.close();
			}

			if (newReader != null) {
				newReader.close();
			}
		}
	}

	/**
	 * Add the time units to all the times of the subtitle times (two)
	 * 
	 * @param line
	 * @param hoursToAdd
	 * @param minutesToAdd
	 * @param secondsToAdd
	 * @param thousandthToAdd
	 * @return String
	 */
	private String parseTimeLine(final String line, final int hoursToAdd, final int minutesToAdd,
			final int secondsToAdd, final int thousandthToAdd) {
		final String[] split = line.split("-->");
		if (split.length != 2) {
			return line;
		}

		String time1 = split[0].trim();
		String time2 = split[1].trim();

		// Add time to the first and second times
		time1 = addTimeToSubtitleTime(time1, hoursToAdd, minutesToAdd, secondsToAdd, thousandthToAdd);
		time2 = addTimeToSubtitleTime(time2, hoursToAdd, minutesToAdd, secondsToAdd, thousandthToAdd);

		return ((new StringBuffer(time1)).append(" --> ").append(time2)).toString();
	}

	/**
	 * Add the times units to the subtitle time given as input
	 * 
	 * @param time
	 * @param hoursToAdd
	 * @param minutesToAdd
	 * @param secondsToAdd
	 * @param thousandthToAdd
	 * @return String
	 */
	private String addTimeToSubtitleTime(final String time, final int hoursToAdd, final int minutesToAdd,
			final int secondsToAdd, final int thousandthToAdd) {
		String tmpTime = time;
		try {
			tmpTime = addThousandthsToSubtitleDate(tmpTime, thousandthToAdd);
			tmpTime = addSecondsToSubtitleTime(tmpTime, secondsToAdd);
			tmpTime = addMinutesToSubtitleTime(tmpTime, minutesToAdd);
			tmpTime = addHoursToSubtitleTime(tmpTime, hoursToAdd);
		} catch (final Exception e) {
			throw new RuntimeException("An error occurred while adding times to the time of the subtitle", e);
		}
		return tmpTime;
	}

	/**
	 * Return true if the current line is number of comment. False otherwise<br />
	 * Check that current line is a comment one only if previous line is null (thus current line is the first) or if it
	 * is empty (thus current line is expecting to be a comment one)
	 * 
	 * @param currentLine
	 * @param previousLine
	 * @return boolean
	 */
	private boolean isCurrentLineANumberOfComment(final String currentLine, final String previousLine) {
		if (previousLine == null || previousLine.isEmpty()) {
			// Check that current line is a comment one only if previous line is null (thus current line is the first)
			// or if it is empty (thus current line is expecting to be a comment one)
			return currentLine.matches("^[0-9]+$");
		}
		return false;
	}

	/**
	 * Add the times given as input in the subtitle file in the corresponding times from comment number fromComment
	 * 
	 * @param fromComment
	 * @param hoursToAdd
	 * @param minutesToAdd
	 * @param secondsToAdd
	 * @param thousandthToAdd
	 */
	public void addTimeToSubtitle(final int fromComment, final int hoursToAdd, final int minutesToAdd,
			final int secondsToAdd, final int thousandthToAdd) {
		// First we check if the subtitle file can be managed
		boolean validExtension = false;
		try {
			validExtension = checkExtensionFile();
		} catch (final Exception e) {
			throw new RuntimeException("An error occurred while checking the extension", e);
		}

		if (!validExtension) {
			throw new RuntimeException("The extension of the subtitle file either does not exist or is not supported");
		}

		// Copy of the subtitle file to work on the copy
		try {
			createEmptySubtitleCopiedFile();
		} catch (final IOException e) {
			throw new RuntimeException("An error occurred while copying the subtitle file", e);
		}

		// Parse the subtitle file and add the times in the copy file
		try {
			parseFileAndAddTimeToSubtitle(fromComment, hoursToAdd, minutesToAdd, secondsToAdd, thousandthToAdd);
		} catch (final IOException e) {
			throw new RuntimeException("An error occurred while parsing the subtitle file and adding the times", e);
		}
	}

	public static void main(final String[] args) {
		final String file1 = "I:\\videos\\Series\\Game of thrones\\Game.of.Thrones.S02E06.720p.HDTV.x264-2HD.srt";
		final SubtiltleHandler subtHandler = new SubtiltleHandler(file1);
		subtHandler.addTimeToSubtitle(587, 0, 0, 29, 0);
	}

	/**
	 * Return the String representation of the hour, the minute or the second to write in the subtitle file
	 * 
	 * @param hourOrMinuteOrSeconds
	 * @return String
	 */
	private String printHourOrMinuteOrSeconds(final int hourOrMinuteOrSeconds) {
		if (hourOrMinuteOrSeconds < 10) {
			return "0" + hourOrMinuteOrSeconds;
		}
		return String.valueOf(hourOrMinuteOrSeconds);
	}

	/**
	 * Return the String representation of the thousandth to write in the subtitle file
	 * 
	 * @param thousandth
	 * @return String
	 */
	private String printThousandth(final int thousandth) {
		if (thousandth < 10) {
			return "00" + thousandth;
		} else if (thousandth < 100) {
			return "0" + thousandth;
		}
		return String.valueOf(thousandth);
	}

	/**
	 * Extract and return as an integer the number of hours (resp. minutes) contained in the minutes (resp. seconds)
	 * given as input<br />
	 * For instance, if the input parameter is 50 minutes, 0 will be returned. If it is 75 minutes, 1 hour will be
	 * returned.<br />
	 * In case the input parameter is negative, an exception is returned
	 * 
	 * @param minutesOrSeconds
	 * @return int
	 * @throws Exception
	 */
	private int extractHourFromMinutesOrMinutesFromSeconds(final int minutesOrSeconds) throws Exception {
		if (minutesOrSeconds < 0) {
			int hourOrMinute = 0;
			int tmpMinOrSec = minutesOrSeconds;
			while (tmpMinOrSec < 0) {
				tmpMinOrSec += 60;
				hourOrMinute--;
			}
			return hourOrMinute;
			// throw new Exception("Minute (or second) cannot be negative : " + minutesOrSeconds);
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

	/**
	 * Return the number of minutes (resp. seconds) which remain from the first input parameter after having subtracted
	 * the corresponding number of hours (resp. minutes) given by the second parameter<br />
	 * In case the input parameter is negative, an exception is returned
	 * 
	 * @param minutesOrSeconds
	 * @param hourOrMinutes
	 * @return int
	 * @throws Exception
	 */
	private int getRemainingMinutesOrSeconds(final int minutesOrSeconds, final int hourOrMinutes) throws Exception {
		final int hourInMinutesOrMinutesInSeconds = hourOrMinutes * 60;
		if (hourInMinutesOrMinutesInSeconds > minutesOrSeconds) {
			throw new Exception(
					"Number of hours (or minutes) too great for the number of minutes (or seconds). Hour (or minutes) = "
							+ hourOrMinutes + " - Minutes (or seconds) = " + minutesOrSeconds);
		}
		return minutesOrSeconds - hourInMinutesOrMinutesInSeconds;
	}

	/**
	 * Extract and return as an integer the number of seconds contained in the thousandth given as input<br />
	 * For instance, if the input parameter is 500 thousandth, 0 will be returned. If it is 2075 thousandth, 1 second
	 * will be returned.<br />
	 * In case the input parameter is negative, an exception is returned
	 * 
	 * @param thousandth
	 * @return int
	 * @throws Exception
	 */
	private int extractSecondsFromThousandth(final int thousandth) throws Exception {
		if (thousandth < 0) {
			int seconds = 0;
			int tmpThousandth = thousandth;
			while (tmpThousandth < 0) {
				tmpThousandth += 1000;
				seconds--;
			}
			return seconds;
			// throw new Exception("Thousandth cannot be negative : " + thousandth);
		} else if (thousandth < 1000) {
			return 0;
		} else {
			int seconds = 0;
			int tmpThousandth = thousandth;
			while (tmpThousandth >= 1000) {
				tmpThousandth -= 1000;
				seconds++;
			}
			return seconds;
		}
	}

	/**
	 * Return the number of thousandth which remain from the first input parameter after having subtracted the
	 * corresponding number of seconds given by the second parameter<br />
	 * In case the input parameter is negative, an exception is returned
	 * 
	 * @param thousandths
	 * @param seconds
	 * @return int
	 * @throws Exception
	 */
	private int getRemainingThousandth(final int thousandths, final int seconds) throws Exception {
		final int secondsInThousandth = seconds * 1000;
		if (secondsInThousandth > thousandths) {
			throw new Exception("Number of seconds too great for the number of thousandth. Seconds = " + seconds
					+ " - Thousandth = " + thousandths);
		}
		return thousandths - secondsInThousandth;
	}
}
