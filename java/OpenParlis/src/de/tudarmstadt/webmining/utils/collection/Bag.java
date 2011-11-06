package de.tudarmstadt.webmining.utils.collection;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * The Bag represents a <code>Collection</code> that counts equal 
 * elements. In contrast to the Apache Commons implementation it
 * is generic.
 * 
 * @param <T> Type of the elements
 */
public final class Bag<T> implements Collection<T> {
	final Map<T,Integer> data;
	
	/*
	 * The iterator class that transit every element
	 * (not only the families of equal elements) 
	 */
	class BagIterator implements Iterator<T> {
		
		// TODO Improve performance
		private int cursor = 0;
		
		@Override
		public boolean hasNext() {
			return this.cursor < size();
		}

		private T now() {
			int i=0;
			T e = null;
			Iterator<T> keyIterator = Bag.this.data.keySet().iterator();
			
			do {
				e = keyIterator.next();
				i += frequency(e);
			} while(i<=this.cursor);
			
			
			return e;
		}
		
		public T next() {
			T result = now();
			this.cursor++;
			
			return result;
		}

		@Override
		public void remove() {
			Bag.this.remove(now());
		}
		
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean add(T e) {
		Integer newVal = Integer.valueOf(frequency(e) + 1);
			
		return this.data.put(e, newVal)==null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean result = false;
		
		for(final T e : c)
			result |= !add(e);
		
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		this.data.clear();
	}

	/** {@inheritDoc} */
	@Override
	public boolean contains(Object o) {
		return this.data.containsKey(o);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o : c)
			if(!contains(o)) return false;
		
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return this.data.isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<T> iterator() {
		return new BagIterator();
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		if(!this.data.containsKey(o)) return false;
		
		if(frequency(o)==1) 
			this.data.remove(o);
		else
			this.data.put((T)o, Integer.valueOf(frequency(o)-1));
		
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = false;
		
		for(final Object e : c)
			result |= !remove(e);
		
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean result = false;
		
		for(Iterator<T> iterator = this.data.keySet().iterator();
			iterator.hasNext(); )
			if(!c.contains(iterator.next())) {
				iterator.remove();
				result = true;
			}
		
		return result;
		
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		int result=0;
		
		for(final T e : this.data.keySet())
			result += frequency(e);
		
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public Object[] toArray() {
		Object[] result = new Object[size()];
		int i = 0;
		for(final T e : this.data.keySet())
			for(int j=0; j<frequency(e); j++) {
				result[i++] = e;
			}
		
		return result;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public <E> E[] toArray(E[] a) {
        if (a.length < size()) 
        	a = (E[]) java.lang.reflect.Array.newInstance(
        			a.getClass(), size());
		int i = 0;
		for(final T e : this.data.keySet())
			for(int j=0; j<frequency(e); j++) {
				a[i++] = (E)e;
			}
        
        if (a.length > size())
            a[size()] = null;
        return a;

	} 
	
	/**
	 * Returns the frequency of the given element
	 * and equal ones.
	 *  
	 * @param e element to count
	 * @return the frequency, zero if e is not
	 * in the bag
	 */
	public int frequency(Object e) {
		return (this.data.get(e)==null) ?
			0 : this.data.get(e).intValue();
	}
	
	/**
	 * Converts the bag to a set disregarding
	 * element multiplicity.
	 * 
	 * @return a set from this bag 
	 */
	public Set<T> toSet() {
		return new HashSet<T>(this.data.keySet());
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder("[");
		
		int i=0;
		for(final T e : this.data.keySet()) {
			result.append("["+frequency(e)+"*"+e+"]");
			i++;
			if(i<this.data.keySet().size()) result.append(",");
		}
			
		return result.append("]").toString();
	}
	
	/**
	 * Creates a new bag with an unsorted map backend.s
	 */
	public Bag() { 
		this.data = new HashMap<T,Integer>();
	}
	
	/**
	 * Creates a new bag with an comparator to group equal
	 * elements.
	 * 
	 * @param comparator comparator for element grouping
	 */
	public Bag(final Comparator<T> comparator) {
		this.data = new TreeMap<T,Integer>(comparator);
	}

}
