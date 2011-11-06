package de.tudarmstadt.webmining.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import static de.tudarmstadt.webmining.utils.TimeUnit.*;

import de.tudarmstadt.webmining.utils.collection.Bag;
import de.tudarmstadt.webmining.utils.collection.SparseList;

public class Utility {
	private static final String CSV_SEPARATOR = ";";
	
	public static void writeCSV(final Map<File, Bag<String>> results,
			final Map<File, List<String>> sortedResults, final File resultFile)
			throws FileNotFoundException, IOException {
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(resultFile)));
		
		int stillwords = results.keySet().size();
		int i=0;
		
		int j=0;
		for(File key : results.keySet()) {
			writer.write("Word in "+key.getName()+CSV_SEPARATOR+" Frequency");
			if(j++<results.keySet().size())
				writer.write(CSV_SEPARATOR+" ");
		}
		writer.write("\n");
		
		while(stillwords>0) {
			j=0;
			for(File key : results.keySet()) {
				if(i<sortedResults.get(key).size()) {
					if(i==sortedResults.get(key).size()-1)
						stillwords--;
					
					Object word = sortedResults.get(key).get(i);
					writer.write(word+CSV_SEPARATOR+" "+results.get(key).frequency(word));
				} else 
					writer.write(CSV_SEPARATOR);
				
				if(j++<results.keySet().size()-1)
					writer.write(CSV_SEPARATOR+" ");
			}
			writer.write("\n");
			i++;
		}
		writer.flush();
		writer.close();
	}
	
//	static NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
	static NumberFormat nf = new DecimalFormat();
	public static String cleanNumber(final String token) {
		Number number;
		try { 
			number = nf.parse(token);
		} catch(ParseException e) {
			return null;
		}
		if(number.toString().charAt(0)=='-')
			return number.toString().substring(1);
		
		return number.toString();
	}
	
	public static boolean isWord(final String s) {
		boolean result = false;
		for(int i=0; i<s.length(); i++)
			result |= Character.isLetter(s.charAt(i));
		return result;
	}
	
	public static boolean isStrictWord(final String s) {
		for(int i=0; i<s.length(); i++)
			if(!Character.isLetter(s.charAt(i))) return false;
		return true;
	}
	
	public static Set<String> loadTextFile(final String fileName) throws IOException {
		Set<String> result = 
			new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

		final BufferedReader reader =
			new BufferedReader(
					new InputStreamReader(
							new FileInputStream(fileName)));
		while(reader.ready())
			result.add(reader.readLine());

		return result;
	}

	
	public static String formatTimeSpan(long nanos) {
		final StringBuilder builder = new StringBuilder();
		TimeUnit[] order = new TimeUnit[] {HOUR,MINUTE,SECOND,MILLI,MICRO,NANO};
		long remainder = nanos;
		for(TimeUnit unit : order) {
			final long value = remainder/unit.getNanos();
			if(value>0)	builder.append(value+unit.getSymbol()+" ");
			remainder %= unit.getNanos();
		}
		return builder.toString();
	}
	
	public static <V,L> Map<List<V>, L> featureSubSet(final Map<List<V>, L> examples, 
			final Collection<Integer> features) {
		final Map<List<V>, L> result = new HashMap<List<V>, L>();
		
		for(Entry<? extends List<V>, L> example : examples.entrySet()) {
			List<V> newExample = new SparseList<V>();
			int i=0;
			for(Integer index : features) {
				newExample.set(i, example.getKey().get(index.intValue()));
				i++;
			}
			result.put(newExample, example.getValue());
		}
		return result;
	}
	
	private Utility() {
		
	}
}
