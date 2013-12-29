package coursera.algo.part1.assignments.week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of a double-ended queue or deque (pronounced "deck") is a generalization of a stack and a queue that
 * supports inserting and removing items from either the front or the back of the data structure
 */
public class Deque<Item> implements Iterable<Item> {

	private final MyLinkedList<Item> myLinkedList;

	/**
	 * Construct an empty deque
	 */
	public Deque() {
		myLinkedList = new MyLinkedList<Item>();
	}

	/**
	 * Return true if this deque is empty
	 * 
	 * @return true/false
	 */
	public boolean isEmpty() {
		return (size() == 0);
	}

	/**
	 * Return as a positive or equal to zero integer, the number of elements in the deque
	 * 
	 * @return int
	 */
	public int size() {
		return myLinkedList.size();
	}

	/**
	 * Add the item given as argument in the deque as the first element<br />
	 * Throws a NullPointerException in case the item to add is null
	 * 
	 * @param item
	 */
	public void addFirst(final Item item) {
		if (item == null) {
			throw new NullPointerException("Trying to add a null element");
		}
		myLinkedList.addFirst(item);
	}

	/**
	 * Add the item given as argument in the deque as the last element<br />
	 * Throws a NullPointerException in case the item to add is null
	 * 
	 * @param item
	 */
	public void addLast(final Item item) {
		if (item == null) {
			throw new NullPointerException("Trying to add a null element");
		}
		myLinkedList.addLast(item);
	}

	/**
	 * Remove the first element of this deque<br />
	 * Throws a NoSuchElementException in case the deque is empty
	 * 
	 * @return Item
	 */
	public Item removeFirst() {
		if (this.isEmpty()) {
			throw new NoSuchElementException("Cannot remove from empty Deque");
		}
		return myLinkedList.removeFirst();
	}

	/**
	 * Remove the last element of this deque<br />
	 * Throws a NoSuchElementException in case the deque is empty
	 * 
	 * @return Item
	 */
	public Item removeLast() {
		if (this.isEmpty()) {
			throw new NoSuchElementException("Cannot remove from empty Deque");
		}
		return myLinkedList.removeLast();
	}

	/**
	 * Return an iterator over items in order from front to end
	 * 
	 * @return Iterator&lt;Item&gt;
	 */
	public Iterator<Item> iterator() {
		return myLinkedList.iterator();
	}

	public static void main(final String[] args) {
		final Integer[] items = new Integer[] { 1, 2, 3 };

		int i = 0;
		System.out.println("i = " + i);
		System.out.println(items[i++]);
		System.out.println("i = " + i);
	}
}
