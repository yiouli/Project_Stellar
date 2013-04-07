package org.yiouli.datastructure.array;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResizableArray<T> implements Iterable<T> {

	protected static final int MIN_CAPACITY = 4;
	protected T[] items;
	protected int n;

	/**
	 * Create an new array of given size and copy content of {@link items} 
	 * over, then set the {@link items} to the newly created array. size is
	 * expected to be no smaller than n. {@link items} is created if it is
	 * null.
	 * @param size is the size of new array.
	 */
	@SuppressWarnings("unchecked")
	protected void resize(int size) {
		Object[] temp = new Object[size];
		if(items != null)
			for (int i = 0; i < n; i++)
				temp[i] = items[i];
		items = (T[])temp;
	}

	public ResizableArray() {
		resize(MIN_CAPACITY);
	}

	public ResizableArray(int capacity) {
		resize(Math.max(MIN_CAPACITY, capacity));
	}
	
	public ResizableArray(T[] arr) {
		items = arr;
		resize(Math.max(MIN_CAPACITY, arr.length));
	}
	
	public int capacity() {
		return items.length;
	}
	
	public int size() {
		return n;
	}
	
	public boolean isEmpty() {
		return n == 0;
	}
	
	/**
	 * Append item to the end array.
	 * @param item
	 */
	public void add(T item) {
		if(item == null)
			throw new NullPointerException();
		items[n++] = item;
		if(n >= 0.75f*items.length)
			resize(2*items.length);
	}
	
	public T get(int index) {
		if(index >= n || index < 0)
			throw new NoSuchElementException();
		return items[index];
	}
	
	public void set(int index, T item) {
		if(index >= n || index < 0)
			throw new NoSuchElementException();
		items[index] = item;
	}
	
	public T delete() {
		if(isEmpty())
			throw new NoSuchElementException();
		T ret = items[--n];
		items[n] = null;
		if(n <= 0.25f*items.length)
			resize(items.length/2);
		return ret;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new RAIterator(this);
	}
	
	private class RAIterator implements Iterator<T> {

		ResizableArray<T> arr;
		int nextIdx;
		
		RAIterator(ResizableArray<T> arr) {
			this.arr = arr;
			nextIdx = 0;
		}
		
		@Override
		public boolean hasNext() {
			return nextIdx < arr.n;
		}

		@Override
		public T next() {
			return arr.items[nextIdx++];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
}
