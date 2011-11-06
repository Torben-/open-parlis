package de.tudarmstadt.webmining.utils;

import de.tudarmstadt.webmining.utils.StringUtils.ConvertingComparator;

public class HierarchicalStringConverter extends ConvertingComparator {
	private final int level;
	@Override
	public CharSequence convert(CharSequence s) {
		String[] parts = s.toString().split("\\.");
		StringBuilder builder = new StringBuilder();
		if(parts.length==0)
			builder.append(s);
		else 
			for(int i=0; i<level && i<parts.length; i++) {
				builder.append(parts[i]);
				if(i+1<level && i+1<parts.length) builder.append(".");
			}
		
		return builder.toString();
	}
	
	public HierarchicalStringConverter(int level) {
		super();
		this.level = level;
	}


}
