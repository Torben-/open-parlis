package de.tudarmstadt.webmining;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import de.tudarmstadt.webmining.analytics.Corpus;
import de.tudarmstadt.webmining.analytics.Corpus.ViewType;
import de.tudarmstadt.webmining.classifier.Classifier;
import de.tudarmstadt.webmining.classifier.NNClassifier;
import de.tudarmstadt.webmining.classifier.NaiveBayesClassifier;
import de.tudarmstadt.webmining.classifier.distance.CosineDistance;
import de.tudarmstadt.webmining.classifier.distance.DistanceFunction;
import de.tudarmstadt.webmining.classifier.distance.EuclideanDistance;
import de.tudarmstadt.webmining.classifier.distance.Intersect;
import de.tudarmstadt.webmining.classifier.distance.ManhattanDistance;
import de.tudarmstadt.webmining.testrig.PercentageSplit;
import de.tudarmstadt.webmining.testrig.TestRig;
import de.tudarmstadt.webmining.tokenizer.WordTokenizer;
import de.tudarmstadt.webmining.utils.Evaluator;
import de.tudarmstadt.webmining.utils.HeadlessReader;
import de.tudarmstadt.webmining.utils.HierarchicalStringConverter;
import de.tudarmstadt.webmining.utils.Stopwatch;
import de.tudarmstadt.webmining.utils.StringUtils;
import de.tudarmstadt.webmining.utils.Utility;
import de.tudarmstadt.webmining.utils.StringUtils.ConvertingComparator;
import de.tudarmstadt.webmining.utils.collection.Bag;
import de.tudarmstadt.webmining.utils.collection.ListFilter;
import de.tudarmstadt.webmining.utils.collection.SparseList;
import de.tudarmstadt.webmining.utils.collection.View;
import de.tudarmstadt.webmining.utils.collection.View.Filter;

public class Main {
	private static final int K = 2;
	private static final DistanceFunction<Number> DISTANCE_FUNCTION = ManhattanDistance.MANHATTAN_DISTANCE;
	private static final ViewType FEATURE_TYPE = ViewType.BOOLEAN;
	private static final boolean NAIVE_BAYES = false;
	private static final boolean skipHeader = false;
	/** Random seed for training/test split */
	public static final int RANDOM_SEED = 42;
	/** Filter stop words */
	public static final boolean FILTER_STOPWORDS = false;
	/** Stem words */
	public static boolean STEM = false;
	/** Stop word file (English) */
	public static String ENGLISH_STOPWORDS =
		"data/stopwords/englishST.txt";
	/** Stop word file (punctuation) */
	public static String PUNCTUATION_STOPWORDS =
		"data/stopwords/punctuation.txt";
	public static int domainLevel = 0;

	private static Map<File, String> documentLabels =
		new HashMap<File, String>();

	private static Map<String, Collection<File>> 
	collectFiles(final String root) {

		final File dir = new File(root);

		final Map<String, Collection<File>> result =
			new HashMap<String, Collection<File>>();

		for(File subDir : dir.listFiles()) 
			if(subDir.isDirectory() && !subDir.isHidden()) {
				final Collection<File> files =
					new ArrayList<File>();
				final String label = subDir.getName();
				for(File file : subDir.listFiles()) 
					if(file.isFile())  {
						files.add(file);
						documentLabels.put(file, label);
					}

				result.put(label, files);
			}

		return result;
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Stopwatch sw = new Stopwatch();
		Map<List<Number>, String> trainingExamples;
		Map<List<Number>, String> testExamples;
		Set<String> labels;
		if(args.length>0) {

			sw.start();

			final String root = args[0];
			System.out.print("Searching file names in "+root+"... ");
			final Map<String, Collection<File>> allFiles = collectFiles(root);
			System.out.println("found "+documentLabels.size()+" files:");
			for(Map.Entry<String, Collection<File>> entry : allFiles.entrySet())
				System.out.println("\t- "+entry.getKey()+": "+entry.getValue().size());
			labels = allFiles.keySet();

			System.out.println();
			System.out.println("Splitting data... ");
			final TestRig<String> testRig =
				new PercentageSplit<String>(allFiles, 0.5, RANDOM_SEED);

			System.out.println("Loading stop words...");
			Filter stopwordFilter = new View.AndFilter(
					new ListFilter(Utility.loadTextFile(ENGLISH_STOPWORDS)),
					new ListFilter(Utility.loadTextFile(PUNCTUATION_STOPWORDS)));

			Comparator<String> comparator;
			if(STEM) {
				StringUtils.Composite comparatorBuilder = new StringUtils.Composite();
				comparatorBuilder.add(StringUtils.CASE_INSENSITIVE);
				comparatorBuilder.add(StringUtils.ENGLISH_LEMMATIZATION);
				comparator = comparatorBuilder;
			} else comparator = String.CASE_INSENSITIVE_ORDER;


			System.out.print("Tokenizing files... ");
			Corpus trainingCorpus = new Corpus();
			for(String label : labels)
				for(File file : testRig.getTrainingSet(label)) {
					try {
						Reader reader;
						if(skipHeader) reader = new HeadlessReader(new FileReader(file));
						else reader = new FileReader(file);

						WordTokenizer tokenizer = 
							new WordTokenizer(new BufferedReader(reader));
						List<String> wordList = new ArrayList<String>();
						for(String word : tokenizer)
							if(!word.isEmpty() && Utility.isStrictWord(word))
								wordList.add(word.trim());
						Bag<String> words = new Bag<String>(comparator);
						if(FILTER_STOPWORDS)
							words.addAll(new View<String>(wordList, stopwordFilter));
						else
							words.addAll(wordList);

						//					words.removeAll(words.toSet());

						trainingCorpus.addDocument(file.getAbsolutePath(), words);
					} catch (FileNotFoundException e) {
						// Just ignore it!
					}
				}

			Corpus testCorpus = new Corpus();
			for(File file : testRig.getTestSet()) {
				try {
					Reader reader;
					if(skipHeader) reader = new HeadlessReader(new FileReader(file));
					else reader = new FileReader(file);

					WordTokenizer tokenizer = 
						new WordTokenizer(new BufferedReader(reader));
					List<String> wordList = new ArrayList<String>();
					for(String word : tokenizer)
						if(!word.isEmpty() && Utility.isWord(word))
							wordList.add(word.trim());
					Bag<String> words = new Bag<String>(comparator);
					words.addAll(new View<String>(wordList, stopwordFilter));
					testCorpus.addDocument(file.getAbsolutePath(), words);
				} catch (FileNotFoundException e) {
					// Just ignore it!
				}
			}

			List<String> trainedWords = new ArrayList<String>(trainingCorpus.getWords());
			System.out.println("found "+trainedWords.size()+" words.");

			System.out.println("Creating feature vectors...");
			trainingExamples =
				new HashMap<List<Number>, String>();

			for(String label : labels) 
				for(File file : testRig.getTrainingSet(label)) {
					List<Number> featureVector = new SparseList<Number>();
					Map<String, Double> mapping = trainingCorpus.getDocumentView(file.getAbsolutePath(), FEATURE_TYPE).asMap();
					int index = 0;
					for(String word : trainedWords) {
						if(mapping.containsKey(word))
							featureVector.set(index, mapping.get(word));
						index++;
					}
					trainingExamples.put(featureVector,label);					
				}

			writeModel(trainingExamples, "training.model");

			testExamples =
				new HashMap<List<Number>, String>();

			for(File file : testRig.getTestSet()) {
				List<Number> featureVector = new SparseList<Number>();
				Map<String, Double> mapping = testCorpus.getDocumentView(file.getAbsolutePath(), FEATURE_TYPE).asMap();
				int index = 0;
				for(String word : trainedWords) {
					if(mapping.containsKey(word))
						featureVector.set(index, mapping.get(word));
					index++;
				}
				testExamples.put(featureVector, documentLabels.get(file));
			}

			writeModel(testExamples, "test.model");

			System.out.println("Preprocessing time: "+Utility.formatTimeSpan(sw.stop()));
		} else {
			trainingExamples = readModel("training.model"); 
			testExamples = readModel("test.model");
			labels = new HashSet<String>(trainingExamples.values());
		}

		System.out.println("Training documents...");
		sw.start();
		Classifier<String, Number> classifier = (NAIVE_BAYES ? 
				new NaiveBayesClassifier<String>(trainingExamples) :
					new NNClassifier<String>(trainingExamples, K, DISTANCE_FUNCTION));
		long trainTime = sw.stop();
		System.out.println("Training time: "+Utility.formatTimeSpan(trainTime)
				+" ("+Utility.formatTimeSpan(trainTime/trainingExamples.size())+")");

		System.out.print("Predicting documents...");
		sw.start();

		final Map<String, Bag<String>> confusionMatrix;
		final ConvertingComparator domainComparator = 
			new HierarchicalStringConverter(domainLevel);

		if(domainLevel==0) 
			confusionMatrix = new HashMap<String, Bag<String>>();
		else
			confusionMatrix = new TreeMap<String, Bag<String>>(domainComparator);

		for(String label : labels)
			if(domainLevel==0)
				confusionMatrix.put(label, new Bag<String>());
			else
				confusionMatrix.put(label, new Bag<String>(domainComparator));

		int i=0;
		for(Map.Entry<List<Number>, String> entry : testExamples.entrySet()) {
			Bag<String> row = confusionMatrix.get(entry.getValue());
			row.add(classifier.predict(entry.getKey()));
		}
		System.out.println();

		long predTime = sw.stop();
		System.out.println("Prediction time: "+Utility.formatTimeSpan(predTime)
				+" ("+Utility.formatTimeSpan(predTime/testExamples.size())+")");

		System.out.println();
		System.out.println("Confusion matrix:");
		System.out.println();
		for(Entry<String, Bag<String>> entry : confusionMatrix.entrySet()) {
			if(domainLevel==0)
				System.out.print(entry.getKey()+" ");
			else 
				System.out.print(domainComparator.convert(entry.getKey())+" ");

			for(String predictedLabel : confusionMatrix.keySet())		
				System.out.print(" "+entry.getValue().frequency(predictedLabel));

			System.out.println();
		}

		Evaluator<String> evaluator = new Evaluator<String>(confusionMatrix);
		System.out.println();
		System.out.printf("Macro Recall: %.2f%%\n",evaluator.macroRecall()*100);
		System.out.printf("Micro Recall: %.2f%%\n",evaluator.microRecall()*100);
		System.out.printf("Macro Precision: %.2f%%\n",evaluator.macroPrecision()*100);
		System.out.printf("Micro Precision: %.2f%%\n",evaluator.microPrecision()*100);
	}

	private static Map<List<Number>, String> readModel(String modelFileName)
			throws IOException, FileNotFoundException, ClassNotFoundException {
		Map<List<Number>, String> trainingExamples;
		File modelFile = new File(modelFileName);
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(modelFile)));
		
		trainingExamples = (Map<List<Number>, String>) ois.readObject();
		return trainingExamples;
	}

	private static void writeModel(Map<List<Number>, String> trainingExamples,
			String pathname) throws IOException, FileNotFoundException {
		final File trainingModelFile = new File(pathname);
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(trainingModelFile)));
		oos.writeObject(trainingExamples);
		oos.flush();
		oos.close();
	}

}
