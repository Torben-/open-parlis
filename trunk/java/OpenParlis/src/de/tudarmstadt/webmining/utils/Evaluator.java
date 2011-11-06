package de.tudarmstadt.webmining.utils;

import java.util.Map;

import de.tudarmstadt.webmining.utils.collection.Bag;

public class Evaluator<L> {
	private final Map<L, Bag<L>> confusionMatrix;
	
	private int sumRow(final L label) {
		Bag<L> row = this.confusionMatrix.get(label);
		int result = 0;
		for(L rowLabel : row.toSet())
			result += row.frequency(rowLabel);
		
		return result;
	}
	
	private int sumColumn(final L label) {
		int result = 0;
		for(Map.Entry<L, Bag<L>> entry : this.confusionMatrix.entrySet())
			if(entry.getValue()!=null)
				result += entry.getValue().frequency(label);
				
		return result;
	}
	
	private int diag(final L label) {
		return this.confusionMatrix.get(label)==null ? 0 : 
			this.confusionMatrix.get(label).frequency(label);
	}
	
	public double recall(final L label) {
		return (double) diag(label) / sumRow(label);
	}
	
	public double precision(final L label) {
		return (double) diag(label) / sumColumn(label);
	}
	
	public double macroPrecision() {
		int i=0;
		double result=0;
		for(L label : this.confusionMatrix.keySet()) {
			i++;
			result += precision(label);
		}
		return (double) result/i;
	}
	
	public double macroRecall() {
		int i=0;
		double result=0;
		for(L label : this.confusionMatrix.keySet()) {
			i++;
			result += recall(label);
		}
		return (double) result/i;
	}
	
	public double microPrecision() {
		int corr=0;
		int sum=0;
		
		for(L label : this.confusionMatrix.keySet()) {
			corr += diag(label);
			sum += sumColumn(label);
		}
		return (double) corr/sum;
	}
	
	public double microRecall() {
		int corr=0;
		int sum=0;
		
		for(L label : this.confusionMatrix.keySet()) {
			corr += diag(label);
			sum += sumRow(label);
		}
		return (double) corr/sum;
	}
	
	public double accuracy() {
		int corr=0;
		int sum=0;
		
		for(L label : this.confusionMatrix.keySet()) {
			corr += diag(label);
			sum += sumRow(label);
		}
		return (double) corr/sum;
	}
	
	public Evaluator(final Map<L, Bag<L>> confusionMatrix) {
		this.confusionMatrix = confusionMatrix;
	}
}
