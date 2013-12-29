package coursera.algo.part1.assignments.week2;

import java.util.NoSuchElementException;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Unit test class for testing Deque.java class
 */
public class DequeTest extends TestCase {

	public void testIsEmpty() {
		final Deque<Integer> d = new Deque<Integer>();
		Assert.assertTrue("The Deque should be empty", d.isEmpty());
	}

	public void testAddFirstNullDeque() {
		final Deque<Integer> d = new Deque<Integer>();

		try {
			d.addFirst(null);
			Assert.assertTrue("NullPointerException expected while adding null item", false);
		} catch (final NullPointerException e) {
			Assert.assertTrue(true);
		}
	}

	public void testAddLastNullDeque() {
		final Deque<Integer> d = new Deque<Integer>();

		try {
			d.addLast(null);
			Assert.assertTrue("NullPointerException expected while adding null item", false);
		} catch (final NullPointerException e) {
			Assert.assertTrue(true);
		}
	}

	public void testRemoveFirstOnEmptyDeque() {
		final Deque<Integer> d = new Deque<Integer>();

		try {
			d.removeFirst();
			Assert.assertTrue("NoSuchElementException expected while removing on an empty queue", false);
		} catch (final NoSuchElementException e) {
			Assert.assertTrue(true);
		}
	}

	public void testRemovelastOnEmptyDeque() {
		final Deque<Integer> d = new Deque<Integer>();

		try {
			d.removeLast();
			Assert.assertTrue("NoSuchElementException expected while removing on an empty queue", false);
		} catch (final NoSuchElementException e) {
			Assert.assertTrue(true);
		}
	}
}
