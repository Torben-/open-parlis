package de.tudarmstadt.webmining.classifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import de.tudarmstadt.webmining.classifier.distance.DistanceFunction;
import de.tudarmstadt.webmining.utils.collection.SparseList;

public class Relief<L,V> {
	private final int m;
	private final DistanceFunction<V> distanceFunction;
	private final DistanceFunction<V> attDistanceFunction;
	private final Random random = 
		new Random(42);

	public Relief(final int m, final DistanceFunction<V> distanceFunction, final DistanceFunction<V> attDistanceFunction) {
		this.m = m;
		this.distanceFunction = distanceFunction;
		this.attDistanceFunction = attDistanceFunction;
	}

	public Collection<Integer> filterFeatures(final Map<? extends List<V>, L> examples,
			final double threshold) {
		if(examples.isEmpty()) return new ArrayList<Integer>();
		
		final Collection<Integer> result = new ArrayList<Integer>();
		
		final List<Double> weights = new ArrayList<Double>();
		final int numAttributes = examples.keySet().iterator().next().size();
		for(int i=0; i<numAttributes; i++)
			weights.add(new Double(0));

		double maxWeight = Double.NEGATIVE_INFINITY;
		
		final List<List<V>> exampleList = new ArrayList<List<V>>(examples.keySet());
		for(int i=0; i<m; i++) {

			final List<V> pivotExample = 
				exampleList.get(this.random.nextInt(examples.size()));
			final L pivotLabel = examples.get(pivotExample); 

			List<V> nnSame = null;
			List<V> nnDifferent = null;

			double distSame = Double.POSITIVE_INFINITY;
			double distDifferent = Double.POSITIVE_INFINITY;

			for(Entry<? extends List<V>, L> example : examples.entrySet()) {
				if(example==pivotExample) continue;
				final double dist = 
					this.distanceFunction.getDistance(pivotExample, example.getKey());
				if(example.getValue().equals(pivotLabel)) {
					if(dist<distSame) {
						distSame = dist;
						nnSame = example.getKey();
					}
				} 
				else if(dist<distDifferent) {
					distDifferent = dist;
					nnDifferent = example.getKey();
				}
			}
			
			for(int j=0; j<numAttributes; j++) {
				double oldValue = weights.get(j).doubleValue();
				
				final List<V> attSame = new ArrayList<V>(1);
				attSame.add(nnSame.get(j));
				final List<V> attDifferent = new ArrayList<V>(1);
				attDifferent.add(nnDifferent.get(j));
				final List<V> attPivot = new ArrayList<V>(1);
				attPivot.add(pivotExample.get(j));
				
				double newValue 
					= oldValue - this.attDistanceFunction.getDistance(attPivot,attSame) / this.m
					           + this.attDistanceFunction.getDistance(attPivot,attDifferent) / this.m;
				if(newValue>maxWeight)
					maxWeight = newValue;
				weights.set(j, Double.valueOf(newValue));
			}
		}
		
		for(int i=0; i<numAttributes; i++) {
			final double weight = weights.get(i).doubleValue();
			if(weight>=maxWeight * threshold) result.add(Integer.valueOf(i));
		}
		return result;
	}
}
