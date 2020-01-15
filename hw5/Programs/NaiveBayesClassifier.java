import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Your implementation of a naive bayes classifier. Please implement all four
 * methods.
 */

public class NaiveBayesClassifier implements Classifier {
	double v = 0;
	
	Map<String, Integer> posWords = new HashMap<String, Integer>();
	Map<String, Integer> negWords = new HashMap<String, Integer>();
	
	Map<Label, Integer> wordCount = new HashMap<Label, Integer>();
	Map<Label, Integer> docCount = new HashMap<Label, Integer>();
	
	/**
	 * Trains the classifier with the provided training data and vocabulary size
	 */
	@Override
	public void train(List<Instance> trainData, int v) {
		// TODO : Implement
		// Hint: First, calculate the documents and words counts per label and store
		// them.
		// Then, for all the words in the documents of each label, count the number of
		// occurrences of each word.
		// Save these information as you will need them to calculate the log
		// probabilities later.
		//
		// e.g.
		// Assume m_map is the map that stores the occurrences per word for positive
		// documents
		// m_map.get("catch") should return the number of "catch" es, in the documents
		// labeled positive
		// m_map.get("asdasd") would return null, when the word has not appeared before.
		// Use m_map.put(word,1) to put the first count in.
		// Use m_map.replace(word, count+1) to update the value
		wordCount = new HashMap<Label, Integer>();
		docCount = new HashMap<Label, Integer>();
		
		posWords = new HashMap<String, Integer>();
		negWords = new HashMap<String, Integer>();
		
		getWordsCountPerLabel(trainData);
		getDocumentsCountPerLabel(trainData);
		this.v = v;
		
		for(Instance data: trainData) {
			if(data.label.equals(Label.POSITIVE)) {
				for(String word: data.words) {
					if(posWords.containsKey(word)) {
						int num = posWords.get(word) + 1;
						posWords.put(word, num);
					}
					else
						posWords.put(word, 1);
				}
			}
			else {
				for(String word: data.words)
					if(negWords.containsKey(word)) {
						int num = negWords.get(word) + 1;
						negWords.put(word, num);
					}
					else
						negWords.put(word, 1);
			}
		}
	}

	/*
	 * Counts the number of words for each label
	 */
	@Override
	public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
		// TODO : Implement
		int pSize = 0;
		int nSize = 0;
		for(Instance data: trainData) {
			if(data.label.equals(Label.POSITIVE))
				pSize += data.words.size();
			else
				nSize += data.words.size();
		}
		
		wordCount.put(Label.POSITIVE, pSize);
		wordCount.put(Label.NEGATIVE, nSize);
		return wordCount;
	}

	/*
	 * Counts the total number of documents for each label
	 */
	@Override
	public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
		// TODO : Implement
		int pSize = 0;
		int nSize = 0;
		for(Instance data: trainData) {
			if(data.label.equals(Label.POSITIVE))
				pSize++;
			else
				nSize++;
		}
		
		docCount.put(Label.POSITIVE, pSize);
		docCount.put(Label.NEGATIVE, nSize);
		return docCount;
	}

	/**
	 * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or
	 * P(NEGATIVE)
	 */
	private double p_l(Label label) {
		// TODO : Implement
		// Calculate the probability for the label. No smoothing here.
		// Just the number of label counts divided by the number of documents.
		int total = docCount.get(Label.POSITIVE)+docCount.get(Label.NEGATIVE);
		
		
		if(label.equals(Label.POSITIVE))
			return ((double)(docCount.get(Label.POSITIVE)))/((double)(total));
		else
			return ((double)(docCount.get(Label.NEGATIVE)))/((double)(total));
	}

	/**
	 * Returns the smoothed conditional probability of the word given the label,
	 * i.e. P(word|POSITIVE) or P(word|NEGATIVE)
	 */
	private double p_w_given_l(String word, Label label) {
		// TODO : Implement
		// Calculate the probability with Laplace smoothing for word in class(label)
		if(label.equals(Label.POSITIVE)) {
			double posCount = wordCount.get(Label.POSITIVE) + 0.0;
			if(!posWords.containsKey(word))
				return 1.0/(v + posCount);
			else
				return (1.0 + ((double)(posWords.get(word))))/(v + posCount);
		}
		else {
			double negCount = wordCount.get(Label.NEGATIVE) + 0.0;
			if(!negWords.containsKey(word))
				return 1.0/(v + negCount);
			else
				return (1.0 + ((double)(negWords.get(word))))/(v + negCount);
		}
	}

	/**
	 * Classifies an array of words as either POSITIVE or NEGATIVE.
	 */
	@Override
	public ClassifyResult classify(List<String> words) {
		// TODO : Implement
		// Sum up the log probabilities for each word in the input data, and the
		// probability of the label
		// Set the label to the class with larger log probability
		double log_POSITIVE = Math.log(p_l(Label.POSITIVE));
		for (String word : words)
			log_POSITIVE += Math.log(p_w_given_l(word, Label.POSITIVE));

		double log_NEGATIVE = Math.log(p_l(Label.NEGATIVE));
		for (String word : words)
			log_NEGATIVE += Math.log(p_w_given_l(word, Label.NEGATIVE));

		// Create the ClassifyResult
		ClassifyResult result = new ClassifyResult();
		result.logProbPerLabel = new HashMap<Label, Double>();
		result.logProbPerLabel.put(Label.POSITIVE, log_POSITIVE);
		result.logProbPerLabel.put(Label.NEGATIVE, log_NEGATIVE);
		
		if (log_POSITIVE > log_NEGATIVE)
			result.label = Label.POSITIVE;
		else
			result.label = Label.NEGATIVE;

		return result;
	}
}
