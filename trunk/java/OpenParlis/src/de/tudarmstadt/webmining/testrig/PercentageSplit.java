package de.tudarmstadt.webmining.testrig;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

public class PercentageSplit<T> implements TestRig<T> {
	private List<File> testSet =
		new ArrayList<File>();
	private Map<T, List<File>> trainingSets =
		new HashMap<T, List<File>>();
	
	@Override
	public List<File> getTestSet() {
		return this.testSet;
	}

	@Override
	public List<File> getTrainingSet(T category) {
		return this.trainingSets.get(category);
	}
	
	public PercentageSplit(final Map<T, Collection<File>> allFiles, 
			final double percent, final long seed) {
		if(percent<0 || percent>1)
			throw new IllegalArgumentException("Percentage split must be between 0 and 1");
		Random random = new Random(seed);
		final Set<T> categories = allFiles.keySet();
		final Map<T, Integer> trainingFraction = 
			new HashMap<T, Integer>();
		final Map<T, Integer> testFraction =
			new HashMap<T, Integer>();
		final Map<T, List<File>> allFileList =
			new HashMap<T, List<File>>();
		
		int totalTestFiles = 0;
		int totalTrainingFiles = 0;
		for(Entry<T, Collection<File>> entry : allFiles.entrySet()) {
			final int size = entry.getValue().size();
			final int testSize = (int) (size * percent);
			final int trainingSize = (int) (size * (1-percent));
			
			testFraction.put(entry.getKey(), Integer.valueOf(testSize));
			trainingFraction.put(entry.getKey(), Integer.valueOf(trainingSize));
			totalTestFiles += testSize;
			totalTrainingFiles += trainingSize;
			
			ArrayList<File> list = new ArrayList<File>(entry.getValue());
			Collections.shuffle(list,random);
			
			allFileList.put(entry.getKey(), list);
		}
		
		for(Entry<T, Collection<File>> entry : allFiles.entrySet()) {
			final T key = entry.getKey();
			final List<File> list = allFileList.get(key);
			final List<File> trainingList = 
				new ArrayList<File>();
			
			for(int i=0; i<trainingFraction.get(key); i++)
				trainingList.add(list.get(i));
			for(int i=trainingFraction.get(key); i<list.size(); i++)
				this.testSet.add(list.get(i));
			
			this.trainingSets.put(key, trainingList);
		}
		
	}
}
