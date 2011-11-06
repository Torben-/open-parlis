package de.tudarmstadt.webmining.testrig;

import java.io.File;
import java.util.List;

public interface TestRig<T> {
	public List<File> getTrainingSet(T category);
	public List<File> getTestSet();
}
