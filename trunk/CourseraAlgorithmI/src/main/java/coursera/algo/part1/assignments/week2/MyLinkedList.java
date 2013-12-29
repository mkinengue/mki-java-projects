package coursera.algo.part1.assignments.week2;

/*************************************************************************
 *  Compilation:  javac Stack.java
 *  Execution:    java Stack < input.txt
 *
 *  A generic stack, implemented using a linked list. Each stack
 *  element is of type Item.
 *  
 *  % more tobe.txt 
 *  to be or not to - be - - that - - - is
 *
 *  % java Stack < tobe.txt
 *  to be not that or be (2 left on stack)
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

import coursera.algo.stdlib.StdOut;
import coursera.algo.stdlib.StdRandom;

/**
 * The <tt>Stack</tt> class represents a last-in-first-out (LIFO) stack of generic items. It supports the usual
 * <em>push</em> and <em>pop</em> operations, along with methods for peeking at the top item, testing if the stack is
 * empty, and iterating through the items in LIFO order.
 * <p>
 * All stack operations except iteration are constant time.
 * <p>
 * For additional documentation, see <a href="/algs4/13stacks">Section 1.3</a> of <i>Algorithms, 4th Edition</i> by
 * Robert Sedgewick and Kevin Wayne.
 */
public class MyLinkedList<Item> implements Iterable<Item> {
	private int N; // size of the stack
	private Node first; // top of stack
	private Node last; // top of stack

	// helper linked list class
	private class Node {
		private Item item;
		private Node previous;
		private Node next;
	}

	/**
	 * Create an empty stack.
	 */
	public MyLinkedList() {
		first = null;
		last = null;
		N = 0;
	}

	/**
	 * Is the stack empty?
	 */
	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * Return the number of items in the stack.
	 */
	public int size() {
		return N;
	}

	/**
	 * Return the element in the queue in index position index (index from 0 to size()- 1).<br />
	 * The element returned is not deleted<br />
	 * Throw NoSuchElementException in case the queue is empty<br />
	 * Throw IndexOutOfBoundsException in case index is negative or greater than or equal to size()<br />
	 * 
	 * @param index
	 * @return Item
	 */
	public Item get(final int index) {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		} else if (index < 0 || index >= N) {
			throw new IndexOutOfBoundsException("The index " + index
					+ " has to be positive and smaller that the number of items in the queue");
		}

		if (index == 0) {
			// Means that toDelete is the first element
			return this.peek();
		} else if (index == (N - 1)) {
			// Means that toDelete is the last element
			return last.item;
		}

		int cpt = 0;
		Node toReturn = first;
		for (int i = 0; i < N && cpt != index; i++) {
			cpt++;
			toReturn = toReturn.next;
		}

		return toReturn.item;
	}

	/**
	 * Add the item to the stack.
	 */
	public void addFirst(final Item item) {
		final Node oldfirst = first;
		first = new Node();
		first.previous = null;
		first.item = item;
		first.next = oldfirst;

		if (oldfirst != null) {
			oldfirst.previous = first;
		}

		if (last == null) {
			// Fisrt time that an element is inserted or the stack was empty
			last = first;
		}

		N++;
	}

	/**
	 * Add the item at the end of the stack
	 * 
	 * @param item
	 */
	public void addLast(final Item item) {
		if (isEmpty()) {
			// If the stack is empty, same operation as push
			addFirst(item);
			return;
		}

		final Node oldLast = last;
		last = new Node();
		last.item = item;
		last.previous = oldLast;
		last.next = null;
		oldLast.next = last;

		N++;
	}

	/**
	 * Delete and return the item in first position into the stack.
	 * 
	 * @return Item
	 * @throws java.util.NoSuchElementException if stack is empty.
	 */
	public Item removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		}
		final Item item = first.item; // save item to return
		first = first.next; // delete first node
		if (first != null) {
			// The stack is not empty yet
			first.previous = null;
		} else {
			// Last element of the list being removed
			last = null;
		}

		N--;

		return item; // return the saved item
	}

	/**
	 * Delete and return the item in last position in the stack
	 * 
	 * @return Item
	 * @throws java.util.NoSuchElementException if stack is empty.
	 */
	public Item removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		}

		final Item item = last.item; // save item to return
		final Node oldPrevious = last.previous;
		last = oldPrevious; // Delete last node
		if (last != null) {
			oldPrevious.next = null; // Deleting last element
		} else {
			// Last element being removed
			first = null;
		}

		N--;

		return item; // return the saved item
	}

	/**
	 * Remove the element in the queue in index position index (index from 0 to size()- 1).<br />
	 * Throw NoSuchElementException in case the queue is empty<br />
	 * Throw IndexOutOfBoundsException in case index is negative or greater than or equal to size()<br />
	 * Note that in case index corresponds to the index position of either the first or the last element, the
	 * corresponding remove method is called
	 * 
	 * @param index
	 * @return Item
	 */
	public Item remove(final int index) {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		} else if (index < 0 || index >= N) {
			throw new IndexOutOfBoundsException("The index " + index
					+ " has to be positive and smaller that the number of items in the queue");
		}

		if (index == 0) {
			// Means that toDelete is the first element
			return this.removeFirst();
		} else if (index == (N - 1)) {
			// Means that toDelete is the last element
			return this.removeLast();
		}

		int cpt = 0;
		Node toDelete = first;
		for (int i = 0; i < N && cpt != index; i++) {
			cpt++;
			toDelete = toDelete.next;
		}

		// Remove the node
		final Node previous = toDelete.previous;
		final Item toReturn = toDelete.item;
		final Node next = toDelete.next;

		// Neither toDelete is the first item nor the last
		previous.next = next;
		next.previous = previous;

		toDelete = null; // to avoid loitering

		// decrease the number of elements
		N--;

		return toReturn;
	}

	/**
	 * Return the item most recently added to the stack.
	 * 
	 * @throws java.util.NoSuchElementException if stack is empty.
	 */
	public Item peek() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		}
		return first.item;
	}

	/**
	 * Return the item most recently added to the stack.
	 * 
	 * @throws java.util.NoSuchElementException if stack is empty.
	 */
	public Item displayLast() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		}
		return last.item;
	}

	/**
	 * Return string representation.
	 */
	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder();
		for (final Item item : this) {
			s.append(item + " ");
		}
		return s.toString();
	}

	/**
	 * Return an iterator to the stack that iterates through the items in LIFO order.
	 */
	public Iterator<Item> iterator() {
		return new ListIterator();
	}

	// an iterator, doesn't implement remove() since it's optional
	private class ListIterator implements Iterator<Item> {
		private Node current = first;

		/**
		 * @{inherited
		 */
		@Override
		public boolean hasNext() {
			return current != null;
		}

		/**
		 * @{inherited
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * @{inherited
		 */
		@Override
		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			final Item item = current.item;
			current = current.next;
			return item;
		}
	}

	/**
	 * A test client.
	 */
	public static void main(final String[] args) {
		final MyLinkedList<String> s = new MyLinkedList<String>();
		s.addFirst("T");
		s.addFirst("O");
		s.addFirst("U");
		s.addFirst("S");

		// s.remove(0);
		//
		for (int i = 0; i < 4; i++) {
			if (s.isEmpty()) {
				break;
			}
			final int idxToRem = StdRandom.uniform(s.size());
			StdOut.println("index:" + idxToRem + " - removed = " + s.remove(idxToRem));
		}

		// while (!StdIn.isEmpty() && i < 10) {
		// final String item = StdIn.readString();
		// if (!item.equals("-")) {
		// s.addLast(item);
		// StdOut.print("last = " + s.displayLast());
		// } else if (!s.isEmpty()) {
		// StdOut.print("last = " + s.displayLast());
		// StdOut.print(s.removeFirst() + " ");
		// }
		// i++;
		// StdOut.println("s = " + s);
		// }
		// StdOut.println("(" + s.size() + " left on stack)");
	}
}
