package de.tudarmstadt.webmining.classifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import de.tudarmstadt.webmining.classifier.distance.DistanceFunction;

public class NNClassifier<L> extends BatchClassifier<L, Number> {
	private final int k;
	private final DistanceFunction<Number> distanceFunction;

	@Override
	public L predict(final List<Number> example) {
		final Map<? extends List<Number>, ? extends L> examples = 
			getExamples();

		final SortedMap<List<Number>, L> candidates =
			new TreeMap<List<Number>, L>(
					new DistanceFunction.Comparator<Number>(this.distanceFunction,example));

		candidates.putAll(examples);
		if(this.k==1)  {
			Entry<List<Number>, L> next = candidates.entrySet().iterator().next();
			return next.getValue();
		}
		
		final Map<L, Integer> frequencies =
			new HashMap<L, Integer>();

		int i = 0;
		for(Map.Entry<? extends List<Number>, ? extends L> entry : candidates.entrySet()) {
			if(i>=this.k) break;
			final int oldValue = frequencies.containsKey(entry.getValue()) ? frequencies.get(entry.getValue()).intValue() : 0;
			frequencies.put(entry.getValue(), Integer.valueOf(oldValue+1));
			i++;
		}

		L bestLabel = null;
		int bestValue=0;

		for(Map.Entry<L, Integer> entry : frequencies.entrySet()) {  
			if(entry.getValue().intValue() > bestValue) {
				bestLabel = entry.getKey();
				bestValue = entry.getValue().intValue();
			}
		}


		return bestLabel;
	}

	public NNClassifier(Map<? extends List<Number>, L> examples, 
			final int k,
			final DistanceFunction<Number> distanceFunction) {
		super(examples);
		this.k = k;
		this.distanceFunction = distanceFunction;
	}	

}
