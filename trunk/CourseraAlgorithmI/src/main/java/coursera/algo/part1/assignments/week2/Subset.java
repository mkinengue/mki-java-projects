package coursera.algo.part1.assignments.week2;

import coursera.algo.stdlib.StdIn;
import coursera.algo.stdlib.StdOut;

/**
 * Implementation of a client program that takes a command-line integer k, reads in a sequence of N strings from
 * standard input using StdIn.readString(), and prints out exactly k of them, uniformly at random. Each item from the
 * sequence can be printed out at most once. You may assume that k ≥ 0 and no greater than the number of string on
 * standard input.
 */
public class Subset {

	/**
	 * Method for testing
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("One argument of type int expected");
		}

		if (!args[0].matches("[0-9]*")) {
			throw new NumberFormatException("One argument of type int expected");
		}

		final int k = Integer.parseInt(args[0]);
		int N = 0;

		final RandomizedQueue<String> queue = new RandomizedQueue<String>();
		while (!StdIn.isEmpty()) {
			final String item = StdIn.readString();
			if (item.equals("|")) {
				break;
			}
			queue.enqueue(item);
			N++;
		}

		if (k > N) {
			throw new IllegalArgumentException("The argument should not be strictly greater than the number of items");
		}

		for (int i = 0; i < k; i++) {
			final String rem = queue.dequeue();
			StdOut.println(rem);
		}
	}
}
