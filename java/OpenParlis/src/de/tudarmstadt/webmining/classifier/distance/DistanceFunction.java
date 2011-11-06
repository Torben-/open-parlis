package de.tudarmstadt.webmining.classifier.distance;

import java.util.List;

public interface DistanceFunction<V> {
	public double getDistance(
			final List<? extends V> v1, final List<? extends V> v2);
	
	
	public static class Comparator<V> implements java.util.Comparator<List<? extends V>> {
		private final DistanceFunction<V> distanceFunction; 
		private final List<? extends V> example;
		
		@Override
		public int compare(List<? extends V> o1, List<? extends V> o2) {
			int i = (int) Math.signum((distanceFunction.getDistance(o1, example)
					- distanceFunction.getDistance(o2, example)));
			return i;
		}

		public Comparator(DistanceFunction<V> distanceFunction, final List<? extends V> example) {
			super();
			this.distanceFunction = distanceFunction;
			this.example = example;
		}
		 
		
	}
}
