package de.tudarmstadt.webmining.utils.collection;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SparseList<E> extends AbstractList<E> implements Serializable {
	
	private final Map<Integer, E> data =
		new HashMap<Integer, E>();
	private int size=0;

	@Override
	public E set(int index, E element) {
		if(index+1>this.size)
			this.size = index+1;
		return this.data.put(Integer.valueOf(index), element);
	}

	@Override
	public E get(int index) {
		return this.data.get(Integer.valueOf(index));
	}

	@Override
	public int size() {
		return this.size;
	}
	
	public Set<Map.Entry<Integer, E>> sparseSet() {
		return this.data.entrySet();
	}
}
