package coursera.algo.part1.assignments.week2;

import java.util.Iterator;
import java.util.NoSuchElementException;

import coursera.algo.stdlib.StdRandom;

/**
 * Implementation of a randomized queue that is similar to a stack or queue, except that the item removed is chosen
 * uniformly at random from items in the data structure
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

	private final MyLinkedList<Item> myLinkedList;

	/**
	 * Construct an empty queue
	 */
	public RandomizedQueue() {
		myLinkedList = new MyLinkedList<Item>();
	}

	/**
	 * Return true if this queue is empty
	 * 
	 * @return true/false
	 */
	public boolean isEmpty() {
		return (size() == 0);
	}

	/**
	 * Return as a positive or equal to zero integer, the number of elements in the queue
	 * 
	 * @return int
	 */
	public int size() {
		return myLinkedList.size();
	}

	/**
	 * Add the item given as argument in the queue as the last element<br />
	 * Throws a NullPointerException in case the item to add is null
	 * 
	 * @param item
	 */
	public void enqueue(final Item item) {
		if (item == null) {
			throw new NullPointerException("Trying to enqueue a null element");
		}
		myLinkedList.addLast(item);
	}

	/**
	 * Remove and return a random element of this queue<br />
	 * Throws a NoSuchElementException in case the queue is empty
	 * 
	 * @return Item
	 */
	public Item dequeue() {
		if (this.isEmpty()) {
			throw new NoSuchElementException("Cannot dequeue from empty queue");
		}

		final int idxToDel = StdRandom.uniform(size());

		final Item removed = myLinkedList.remove(idxToDel);
		return removed;
	}

	/**
	 * Return a random item in the queue
	 * 
	 * @return Item
	 */
	public Item sample() {
		if (this.isEmpty()) {
			throw new NoSuchElementException("Cannot sample from empty queue");
		}

		final int idxToRet = StdRandom.uniform(size());

		int cpt = 0;
		Item item = null;
		final Iterator<Item> it = iterator();
		while (it.hasNext()) {
			item = it.next();
			if (cpt == idxToRet) {
				break;
			}
			cpt++;
		}

		return item;
		// return myLinkedList.get(idxToRet);
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		return myLinkedList.iterator();
	}
}
