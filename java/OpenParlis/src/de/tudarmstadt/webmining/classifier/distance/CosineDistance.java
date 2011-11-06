package de.tudarmstadt.webmining.classifier.distance;

import java.util.List;
import java.util.Map;

import de.tudarmstadt.webmining.utils.collection.SparseList;

public class CosineDistance implements DistanceFunction<Number> {
	private double normalisationFactor(final SparseList<Number> list) {
		double result = 0;
		for(Map.Entry<Integer, Number> entry : list.sparseSet()) {
			result += entry.getValue()!=null ? entry.getValue().doubleValue() : 0;
		}
		return result;
	}
	
	@Override
	public double getDistance(List<? extends Number> v1, List<? extends Number> v2) {
		double result=0.0d;
		if(v1.size()==0 || v2.size()==0)
			return Double.POSITIVE_INFINITY;
		if(v1 instanceof SparseList<?>) {
			SparseList<Number> sparseList1 = (SparseList<Number>) v1;
			SparseList<Number> sparseList2 = (SparseList<Number>) v2;
			final double n1 = normalisationFactor(sparseList1);
			final double n2 = normalisationFactor(sparseList2);
			for(Map.Entry<Integer, Number> entry : sparseList1.sparseSet()) {
				if(v2.get(entry.getKey())!=null && entry.getValue()!=null)
					result += entry.getValue().doubleValue()
						* v2.get(entry.getKey()).doubleValue();
			}
			result /= (n1*n2);
		} else {
			throw new RuntimeException("Not implemented");
		}
		return 1-result;
	}
	
	public static final CosineDistance COSINE_DISTANCE = 
		new CosineDistance();

}
