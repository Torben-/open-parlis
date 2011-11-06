package de.tudarmstadt.webmining.classifier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import de.tudarmstadt.webmining.classifier.distance.DistanceFunction;
import de.tudarmstadt.webmining.utils.Stopwatch;
import de.tudarmstadt.webmining.utils.collection.Bag;
import de.tudarmstadt.webmining.utils.collection.SparseList;

public class NaiveBayesClassifier<L> extends BatchClassifier<L, Number> {
	private boolean learned=false;
	private final Map<L, Double> apriori =
		new HashMap<L, Double>();
	private final Map<L, List<Double>> classProbability =
		new HashMap<L, List<Double>>();

	private double factor = 1.0d;


	private void learnApriori() {
		final Map<? extends List<Number>, ? extends L> examples = getExamples();
		final int numExamples = examples.size();
		final Bag<L> frequencies = new Bag<L>();

		/* Estimate a priori probability for each class */
		for(L label : examples.values())
			frequencies.add(label);

		for(L label : examples.values())
			this.apriori.put(label, 
					Double.valueOf(frequencies.frequency(label) / (double) numExamples));
	}

	private void learn() {
		final Map<L, SparseList<Integer>> valuePerClass =
			new HashMap<L, SparseList<Integer>>();
		final Map<L, Integer> totalValuesPerClass =
			new HashMap<L, Integer>();

		final Map<? extends List<Number>, ? extends L> examples = getExamples();
		final Set<L> labels = new HashSet<L>(examples.values());
		learnApriori();

		for(Map.Entry<? extends List<Number>, ? extends L> example : examples.entrySet()) {
			SparseList<Integer> valueList = valuePerClass.get(example.getValue());
			if(valueList==null) valueList = new SparseList<Integer>();
			int totalValue = totalValuesPerClass.get(example.getValue())==null ? 0 : totalValuesPerClass.get(example.getValue()).intValue();

			if(example.getKey() instanceof SparseList<?>) {
				SparseList<Number> sparseFeature = (SparseList<Number>) example.getKey();
				for(Map.Entry<Integer, Number> value : sparseFeature.sparseSet()) {
					int index = value.getKey().intValue();
					int oldValue = valueList.get(index)==null ? 
							0 : valueList.get(index).intValue();

					if(value.getKey()!=null 
							&& value.getKey().intValue() > 0) {
						valueList.set(index, Integer.valueOf(value.getKey().intValue() + oldValue));
						totalValue += value.getKey().intValue();
					}
				}	
			} else {

			}

			valuePerClass.put(example.getValue(), valueList);
			totalValuesPerClass.put(example.getValue(), Integer.valueOf(totalValue));
		}


		// Apply laplace correction
		for(L label : labels) {
			SparseList<Integer> v = valuePerClass.get(label);
			List<Double> probabilities = new ArrayList<Double>();
			int t = (totalValuesPerClass.get(label)==null ? 0 : totalValuesPerClass.get(label).intValue()) +v.size();
			if(v==null) { v = new SparseList<Integer>(); valuePerClass.put(label, v); }

			for(int i=0; i<v.size(); i++) {
				int value = v.get(i)==null?1:v.get(i).intValue()+1;
				v.set(i, value);
				probabilities.add(Double.valueOf((double)value/t));
			}
			totalValuesPerClass.put(label, Integer.valueOf(t));
			this.classProbability.put(label, probabilities);
			try {
				Writer writer = new BufferedWriter(new FileWriter("model",true));
				writer.write(probabilities+"\n");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		this.learned = true;
	}

	@Override
	public L predict(final List<Number> example) {
		if(!this.learned) learn();

		L bestClass = null;
		double bestProbability = Double.NEGATIVE_INFINITY;

		for(Map.Entry<L, List<Double>> entry : this.classProbability.entrySet()) {
			double prob = 0.0d;
			List<Double> featureVector = entry.getValue();
			if(example instanceof SparseList<?>) {
				SparseList<Number> sparseExample = (SparseList<Number>) example;
				for(Entry<Integer, Number> exampleEntry : sparseExample.sparseSet()) {
					int i = exampleEntry.getKey();
					if(i<featureVector.size())
						prob += Math.log(featureVector.get(i).doubleValue());
				}
			} else {

			}

			prob += Math.log(this.apriori.get(entry.getKey()));

			if(prob>bestProbability) {
				bestProbability = prob;
				bestClass = entry.getKey();
			}
		}

		return bestClass;
	}

	public NaiveBayesClassifier(Map<? extends List<Number>, L> examples, boolean lazy) {
		super(examples);
		if(!lazy) learn();
	}

	public NaiveBayesClassifier(Map<? extends List<Number>, L> examples){
		this(examples,false);
	}

}
