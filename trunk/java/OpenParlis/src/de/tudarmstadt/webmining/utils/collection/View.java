package de.tudarmstadt.webmining.utils.collection;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * The <code>View</code> class is a wrapper around any <code>Collection</code>
 * that filters certain elements not accepted by the given <code>Filter</code>
 * instance.
 *  
 * @param <E> type of the elements
 */
public class View<E> implements Collection<E> {
	
	/**
	 * The <code>Filter</code> interface provides a method that checks 
	 * the acceptance of a certain object.
	 */
	public static interface Filter {
		/**
		 * Checks, whether the given object will be accepted by this
		 * filter.
		 * 
		 * @param o object to check
		 * @return true, if accepted; false, if refused
		 */
		public boolean accept(Object o);
	}
	
	/**
	 * The NotFilter accepts elements, if its base filter does
	 * not accept them.
	 */
	public static class NotFilter implements Filter {
		private final Filter base;
		
		/** {@inheritDoc} */
		@Override
		public boolean accept(Object o) {
			return !this.base.accept(o);
		}
		
		/**
		 * Creates a new Not wrapper around the given
		 * base filter.
		 * 
		 * @param base
		 */
		public NotFilter(final Filter base) {
			this.base = base;
		}
	}
	
	/**
	 * The AndFilter accepts only if both filters provided accept.
	 */
	public static class AndFilter implements Filter {
		private final Filter filter1, filter2;
		
		/** {@inheritDoc} */
		@Override
		public boolean accept(Object o) {
			return filter1.accept(o) && filter2.accept(o);
		}
		
		/**
		 * Creates a new AndFilter for tow base filters.
		 *
		 * @param filter1
		 * @param filter2
		 */
		public AndFilter(final Filter filter1, 
				final Filter filter2) {
			this.filter1 = filter1;
			this.filter2 = filter2;
		}
	}
	
	private final Filter filter;
	final Collection<E> base;
	
	private final class Iterator implements java.util.Iterator<E> {
		private final Filter filter;
		private final java.util.Iterator<E> baseIterator = View.this.base.iterator();
		private E next=null;
		private E current=null;
		
		/*
		 * Seek the next available element that is accepted
		 * by the filter. Sets the next field to this element. 
		 */
		private void seekNext() {
			this.next = null;
			while(this.baseIterator.hasNext()) {
				E cursor = this.baseIterator.next();
				if(this.filter.accept(cursor)) {
					this.next = cursor;
					break;
				}
			}
			
		}
		
		/** {@inheritDoc} */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		/** {@inheritDoc} */
		@Override
		public E next() {
			this.current = this.next; // Remember the next element
			seekNext(); // before seeking the new next
			
			if(this.current!=null)
				return this.current;
			
			throw new NoSuchElementException();
		}

		/** {@inheritDoc} */
		@Override
		public void remove() {
			View.this.remove(this.current);
		}

		public Iterator(final Filter filter) {
			this.filter = filter;
			seekNext();
		}
		
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean add(E e) {
		return this.base.add(e);
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.base.addAll(c);
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		this.base.clear();
	}

	/** {@inheritDoc} */
	@Override
	public boolean contains(Object o) {
		if(this.base.contains(o))
			return this.filter.accept(o);
		
		return false;
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
		return this.size()==0;
	}

	/** {@inheritDoc} */
	@Override
	public java.util.Iterator<E> iterator() {
		return new Iterator(this.filter);
	}

	/** {@inheritDoc} */
	@Override
	public boolean remove(Object o) {
		if(!this.filter.accept(o)) return false;
		
		return this.base.remove(o);
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
		
		for(java.util.Iterator<E> iterator = this.base.iterator();
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
		for(E e : this.base)
			if(this.filter.accept(e)) result++;
		
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public Object[] toArray() {
		Object[] result = new Object[size()];
		int i = 0;
		for(final E e : this.base)
			result[i++] = e;
		
		return result;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
        if (a.length < size()) 
        	a = (T[]) java.lang.reflect.Array.newInstance(
        			a.getClass(), size());
		int i = 0;
		for(final E e : this.base)
			a[i++] = (T)e;
        
        if (a.length > size())
            a[size()] = null;
        return a;

	}
	
	/**
	 * Creates a new view on the given collection using
	 * the specified filter.
	 * 
	 * @param base base collection to filter 
	 * @param filter filter to use
	 */
	public View(final Collection<E> base, final Filter filter) {
		this.base = base;
		this.filter = filter;
	}
}
