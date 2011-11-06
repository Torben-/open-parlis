package de.tudarmstadt.webmining.utils.collection;

import java.util.Collection;

import de.tudarmstadt.webmining.utils.collection.View.Filter;

public class ListFilter implements Filter {
	private final Collection<?> ignoreCollection;
	
	@Override
	public boolean accept(Object o) {
		return !ignoreCollection.contains(o);
	}
	
	public ListFilter(final Collection<?> ignoreCollection) {
		this.ignoreCollection = ignoreCollection;
	}
}
