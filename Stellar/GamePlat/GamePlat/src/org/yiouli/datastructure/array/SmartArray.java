package org.yiouli.datastructure.array;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Array with free list.
 * @author leoSU
 *
 */
public class SmartArray<T> {

	private ArrayList<Integer> free;
	private int size;
	private T[] item;

	@SuppressWarnings("unchecked")
	public SmartArray(int capacity) {
		item = (T[])new Object[capacity];
		free = new ArrayList<Integer>(capacity);
		for(int i = 0; i < capacity; i++)
			free.add(i);
	}

	public int capacity() {
		return item.length;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean has(int index) {
		return item[index] != null;
	}
	
	public int add(T itm) {
		if(free.isEmpty())
			throw new UnsupportedOperationException("Array is full");
		int idx = free.remove(free.size()-1);
		assert !has(idx);
		item[idx] = itm;
		size++;
		return idx;
	}
	
	public T delete(int index) {
		if(!has(index))
			throw new NoSuchElementException();
		T ret = item[index];
		item[index] = null;
		free.add(index);
		size--;
		return ret;
	}
	
	public T get(int index) {
		if(!has(index))
			throw new NoSuchElementException();
		return item[index];
	}
	
	public int[] indices() {
		int[] ret = new int[size];
		int ri = 0;
		for(int i = 0; i < item.length; i++)
			if(item[i] != null)
				ret[ri++] = i;
		return ret;
	}
}
