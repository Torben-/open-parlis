package de.tudarmstadt.webmining.classifier.distance;

import java.util.List;
import java.util.Map;

import de.tudarmstadt.webmining.utils.collection.SparseList;

public class ManhattanDistance implements DistanceFunction<Number> {

	@Override
	public double getDistance(List<? extends Number> v1,
			List<? extends Number> v2) {
		double result = 0;
		if(v1.size()==0 || v2.size()==0)
			return Double.POSITIVE_INFINITY;
		if(v1 instanceof SparseList<?>) {
			SparseList<Number> sparseList1 = (SparseList<Number>) v1;
			for(Map.Entry<Integer, Number> entry : sparseList1.sparseSet()) {
				int i = entry.getKey().intValue();
				double n1 = (entry.getValue() == null ? 0 :
					entry.getValue().doubleValue());
				double n2 = (v2.get(i) == null ? 0 :
					v2.get(i).doubleValue());

				result += Math.abs(n1-n2);
			}

			SparseList<Number> sparseList2 = (SparseList<Number>) v2;
			for(Map.Entry<Integer, Number> entry : sparseList2.sparseSet()) {
				if(entry.getValue()==null) continue;
				int i = entry.getKey().intValue();
				if(v1.get(i) == null || v1.get(i).doubleValue()==0)
					result += entry.getValue().doubleValue();
			}
		} else {
			for(int i=0; i<Math.min(v1.size(), v2.size()); i++) {
				double n1 = (v1.get(i) == null ? 0 :
					v1.get(i).doubleValue());
				double n2 = (v2.get(i) == null ? 0 :
					v2.get(i).doubleValue());

				result += Math.abs(n1-n2);
			}		
		}
		return result;
	}

	private ManhattanDistance() {

	}

	public static ManhattanDistance MANHATTAN_DISTANCE =
		new ManhattanDistance();

}
