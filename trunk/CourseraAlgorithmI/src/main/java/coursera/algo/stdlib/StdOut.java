package coursera.algo.stdlib;

/*************************************************************************
 *  Compilation:  javac StdOut.java
 *  Execution:    java StdOut
 *
 *  Writes data of various types to standard output.
 *
 *************************************************************************/

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * <i>Standard output</i>. This class provides methods for writing strings and numbers to standard output.
 * <p>
 * For additional documentation, see <a href="http://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 * <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 */
public final class StdOut {

	// force Unicode UTF-8 encoding; otherwise it's system dependent
	private static final String charsetName = "UTF-8";

	// assume language = English, country = US for consistency with StdIn
	private static final Locale US_LOCALE = new Locale("en", "US");

	// send output here
	private static PrintWriter out;

	// this is called before invoking any methods
	static {
		try {
			out = new PrintWriter(new OutputStreamWriter(System.out, charsetName), true);
		} catch (final UnsupportedEncodingException e) {
			System.out.println(e);
		}
	}

	// don't instantiate
	private StdOut() {
	}

	// close the output stream (not required)
	/**
	 * Close standard output.
	 */
	public static void close() {
		out.close();
	}

	/**
	 * Terminate the current line by printing the line separator string.
	 */
	public static void println() {
		out.println();
	}

	/**
	 * Print an object to standard output and then terminate the line.
	 */
	public static void println(final Object x) {
		out.println(x);
	}

	/**
	 * Print a boolean to standard output and then terminate the line.
	 */
	public static void println(final boolean x) {
		out.println(x);
	}

	/**
	 * Print a char to standard output and then terminate the line.
	 */
	public static void println(final char x) {
		out.println(x);
	}

	/**
	 * Print a double to standard output and then terminate the line.
	 */
	public static void println(final double x) {
		out.println(x);
	}

	/**
	 * Print a float to standard output and then terminate the line.
	 */
	public static void println(final float x) {
		out.println(x);
	}

	/**
	 * Print an int to standard output and then terminate the line.
	 */
	public static void println(final int x) {
		out.println(x);
	}

	/**
	 * Print a long to standard output and then terminate the line.
	 */
	public static void println(final long x) {
		out.println(x);
	}

	/**
	 * Print a short to standard output and then terminate the line.
	 */
	public static void println(final short x) {
		out.println(x);
	}

	/**
	 * Print a byte to standard output and then terminate the line.
	 */
	public static void println(final byte x) {
		out.println(x);
	}

	/**
	 * Flush standard output.
	 */
	public static void print() {
		out.flush();
	}

	/**
	 * Print an Object to standard output and flush standard output.
	 */
	public static void print(final Object x) {
		out.print(x);
		out.flush();
	}

	/**
	 * Print a boolean to standard output and flush standard output.
	 */
	public static void print(final boolean x) {
		out.print(x);
		out.flush();
	}

	/**
	 * Print a char to standard output and flush standard output.
	 */
	public static void print(final char x) {
		out.print(x);
		out.flush();
	}

	/**
	 * Print a double to standard output and flush standard output.
	 */
	public static void print(final double x) {
		out.print(x);
		out.flush();
	}

	/**
	 * Print a float to standard output and flush standard output.
	 */
	public static void print(final float x) {
		out.print(x);
		out.flush();
	}

	/**
	 * Print an int to standard output and flush standard output.
	 */
	public static void print(final int x) {
		out.print(x);
		out.flush();
	}

	/**
	 * Print a long to standard output and flush standard output.
	 */
	public static void print(final long x) {
		out.print(x);
		out.flush();
	}

	/**
	 * Print a short to standard output and flush standard output.
	 */
	public static void print(final short x) {
		out.print(x);
		out.flush();
	}

	/**
	 * Print a byte to standard output and flush standard output.
	 */
	public static void print(final byte x) {
		out.print(x);
		out.flush();
	}

	/**
	 * Print a formatted string to standard output using the specified format string and arguments, and flush standard
	 * output.
	 */
	public static void printf(final String format, final Object... args) {
		out.printf(US_LOCALE, format, args);
		out.flush();
	}

	/**
	 * Print a formatted string to standard output using the specified locale, format string, and arguments, and flush
	 * standard output.
	 */
	public static void printf(final Locale locale, final String format, final Object... args) {
		out.printf(locale, format, args);
		out.flush();
	}

	// This method is just here to test the class
	public static void main(final String[] args) {

		// write to stdout
		StdOut.println("Test");
		StdOut.println(17);
		StdOut.println(true);
		StdOut.printf("%.6f\n", 1.0 / 7.0);
	}

}
