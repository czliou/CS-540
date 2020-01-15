import java.util.ArrayList;
import java.util.List;

public class CrossValidation {
    /*
     * Returns the k-fold cross validation score of classifier clf on training data.
     */
    public static double kFoldScore(Classifier clf, List<Instance> trainData, int k, int v) {
    	int size = trainData.size();
    	int [] arr = new int[k];
    	
    	for(int i = 0; i < arr.length; i++)
    		arr[i] = i*(size/k);
    	
    	double kFoldScore = 0.0;
    	for(int i = 0; i < arr.length; i++) {
    		List<Instance> train = new ArrayList<Instance>();
    		List<Instance> test = new ArrayList<Instance>();
    		
    		for(int j = 0; j < size; j++) {
    			if(j < arr[i] || j >= arr[i] + size/k)
    				train.add(trainData.get(j));
    			else
    				test.add(trainData.get(j));
    		}
    		
    		clf.train(train, v);
    		double correct = 0;
    		for (Instance ti : test) {
    			ClassifyResult cr = clf.classify(ti.words);
    			if(ti.label.equals(cr.label))
    				correct += 1.0;
    		}
    		
    		correct /= (test.size() + 0.0);
    		kFoldScore += correct;
    	}
        return kFoldScore/(k + 0.0);
    }
}
