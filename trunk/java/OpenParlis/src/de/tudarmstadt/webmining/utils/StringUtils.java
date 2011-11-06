package de.tudarmstadt.webmining.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//import org.tartarus.snowball.ext.englishStemmer;

/**
 * Compilation of frequently used word based String comparators.
 */
public class StringUtils {
	/**
	 * A converter converts a typed object into another.
	 * 
	 * @param <Type> type of the converted object
	 *
	 */
	public static interface Converter<Type> {
		/**
		 * Converts the Strings to compare to a base form.
		 * 
		 * @param s character sequence to convert 
		 * @return conversion result
		 */
		public abstract Type convert(final Type s);
	}
	
	/**
	 * Abstract base class for all String comparators in this compilation.
	 */
	public static abstract class ConvertingComparator implements Comparator<String>, Converter<CharSequence> {
		
		/** {@inheritDoc} */
		@Override
		public abstract CharSequence convert(final CharSequence s);
		
		@Override
		public int compare(String o1, String o2) {
			return convert(o1).toString().compareTo(convert(o2).toString());
		}		
	}
	
	/**
	 * The composite comparator allows to create chains of conversions
	 * for comparison.
	 */
	public static final class Composite extends ConvertingComparator {
		/* Chain of components for conversion */
		private final List<ConvertingComparator> components =
			new ArrayList<ConvertingComparator>();
		
		/** {@inheritDoc} */
		@Override
		public CharSequence convert(CharSequence s) {
			CharSequence result = new StringBuilder(s);
			for(ConvertingComparator comparator : this.components)
				result = comparator.convert(result);
			
			return result;
		}
		
		/**
		 * Adds a comparator component to this composite.
		 * 
		 * @param component converting comparator component
		 */
		public void add(final ConvertingComparator component) {
			this.components.add(component);
		}
	}
	
	
	/*
	 * Concrete comparators:  
	 */
	
	/**
	 * This comparator compares two String exactly like the compareTo() 
	 * method of the String class.
	 */
	public static final Exact EXACT =
		new Exact();
	
	protected static final class Exact extends ConvertingComparator {
		/** {@inheritDoc} */
		@Override
		public CharSequence convert(CharSequence s) {
			return s;
		}
		
	}
	/**
	 * This comparator regards upper-case and lower-case forms of a 
	 * word as equal. 
	 */
	public static final CaseInsensitive CASE_INSENSITIVE = 
		new CaseInsensitive();

	protected static final class CaseInsensitive extends ConvertingComparator {
		/** {@inheritDoc} */
		@Override
		public CharSequence convert(CharSequence s) {
			return s.toString().toLowerCase();
		}
	}

	/**
	 * This comparator regards upper-case and lower-case beginning forms of a 
	 * word as equal. 
	 */
	public static final IgnoreCapitalization IGNORE_CAPITALIZATION = 
		new IgnoreCapitalization();
	
	protected static final class IgnoreCapitalization extends ConvertingComparator {
		/** {@inheritDoc} */
		@Override
		public CharSequence convert(CharSequence s) {
			StringBuilder result = new StringBuilder();
			boolean convertCase = true;

			for(int i=0; i<s.length(); i++) 
				if(convertCase) {
					result.append(Character.toLowerCase(s.charAt(i)));
					convertCase = false;
				} else {
					if(Character.isWhitespace(s.charAt(i)))
						convertCase = true;

					result.append(s.charAt(i));
				}
			return result.toString();
		}
	}
	
	/**
	 * This comparator ignores everything inside parentheses, 
	 * like "(...)".
	 */
	public static final IgnoreParentheses IGNORE_PARENTHESES =
		new IgnoreParentheses();
	
	protected static final class IgnoreParentheses extends ConvertingComparator {

		/** {@inheritDoc} */
		@Override
		public CharSequence convert(CharSequence s) {
			StringBuilder result = new StringBuilder();
			boolean ignore = false;

			for(int i=0; i<s.length(); i++)
				if(s.charAt(i)=='(') ignore = true;
				else if(s.charAt(i)==')') ignore = false;
				else if(!ignore)
					result.append(s.charAt(i));
			
			return result.toString().trim();
		}
	}
	
	/**
	 * This comparator treats the "_" like as whitespace.
	 */
	public static final IgnoreUnderscore IGNORE_UNDERSCORE =
		new IgnoreUnderscore();
	
	protected static final class IgnoreUnderscore extends ConvertingComparator {
	
		/** {@inheritDoc} */
		@Override
		public CharSequence convert(CharSequence s) {
			return s.toString().replaceAll("_", " ");
		}
	}
	
	/**
	 * This comparator compares the stemmed forms of a word
	 * (English version).
	 */
	public static final EnglishStemming ENGLISH_LEMMATIZATION =
		new EnglishStemming();
	
	protected static final class EnglishStemming extends ConvertingComparator {
		//private final englishStemmer stemmer;
		
		/** {@inheritDoc} */
		@Override
		public CharSequence convert(CharSequence s) {
			/*
			stemmer.setCurrent(s.toString());
			stemmer.stem();
			return this.stemmer.getCurrent();
			*/
			return s;
		}
		
		public EnglishStemming() {
			//this.stemmer = new englishStemmer();
		}
		
	}

	/*
	 * Do not instantiate!
	 */
	private StringUtils() {
		/* Empty */
	}
}
