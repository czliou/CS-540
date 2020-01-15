import java.util.ArrayList;
import java.util.List;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0) this.numAttr = trainDataSet.get(0).size() - 1;
		this.root = buildTree(trainData, 0);
	}
	
	private DecTreeNode buildTree(List<List<Integer>> examples, int currDepth) {
		if(examples.size() == 0) return new DecTreeNode(1, -1, -1);
		
		int count1 = 0;
		int count0 = 0;
		for(int i = 0; i < examples.size(); i++) {
			if(examples.get(i).get(examples.get(i).size() - 1) == 1)
				count1+= 1;
			else
				count0+= 1;
		}
		int bestAttribute = 0;
		int bestThreshhold = 0;
		double bestGain = 0.0;
		for(int attribute = 0; attribute <= numAttr - 1; attribute++) {
			for(int threshhold = 1; threshhold <= 9; threshhold++) {
				double gain = infoGain(examples, attribute, threshhold);
				if(gain > bestGain) {
					bestGain = gain;
					bestThreshhold = threshhold;
					bestAttribute = attribute;
				}
			}
		}
		
		if(currDepth == maxDepth || bestGain <= 0.0 || examples.size() <= maxPerLeaf) {
			if(count1 < count0)
				return new DecTreeNode(0, -1, -1);
			return new DecTreeNode(1, -1, -1);
		}
		
		DecTreeNode tree;
		if(count1 < count0)
			tree = new DecTreeNode(0, bestAttribute, bestThreshhold);
		else
			tree = new DecTreeNode(1, bestAttribute, bestThreshhold);
		
		List<List<Integer>> left = new ArrayList<List<Integer>>();
		List<List<Integer>> right = new ArrayList<List<Integer>>();
		for(int i = 0; i < examples.size(); i++) {
			if(examples.get(i).get(bestAttribute) <= bestThreshhold)
				left.add(examples.get(i));
			else
				right.add(examples.get(i));
		}
		
		tree.left = buildTree(left, currDepth + 1);
		tree.right = buildTree(right, currDepth + 1);
		return tree;
	}
	
	public double infoGain(List<List<Integer>> examples, int attribute, int threshold) {
		double entropy = 0.0;
		double condEntropy = 0.0;
		
		double c1 = 0.0;
		double c0 = 0.0;
		for(int i = 0; i < examples.size(); i++) {
			if(examples.get(i).get(examples.get(i).size() - 1) == 1)
				c1 += 1.0;
			else
				c0 += 1.0;
		}
		double total = examples.size() + 0.0;
		
		entropy = -((c1/total)*log(c1/total) + (c0/total)*log(c0/total));
		
		double left1 = 0.0, left0 = 0.0, right1 = 0.0, right0 = 0.0;
		for(int i = 0; i < examples.size(); i++) {
			if(examples.get(i).get(attribute) <= threshold) {
				if(examples.get(i).get(examples.get(i).size() - 1) == 1)
					left1 += 1.0;
				else
					left0 += 1.0;
			}
			else {
				if(examples.get(i).get(examples.get(i).size() - 1) == 1)
					right1 += 1.0;
				else
					right0 += 1.0;
			}
		}
		double leftTotal = left1 + left0;
		double rightTotal = right1 + right0;
		condEntropy = (leftTotal/total)*((-left1/leftTotal)*log(left1/leftTotal) - (left0/leftTotal)*log(left0/leftTotal));
		condEntropy += (rightTotal/total)*((-right1/rightTotal)*log(right1/rightTotal) - (right0/rightTotal)*log(right0/rightTotal));
		
		return entropy - condEntropy;
	}
	
	public double log(double x) {
		if(x == 0.0)
			return 0;
		return (Math.log10(x))/(Math.log10(2));
	}
	
	public int classify(List<Integer> instance) {
		return classifyHelp(root, instance);
	}
	
	private int classifyHelp(DecTreeNode node, List<Integer> instance) {
		if(!node.isLeaf()) {
			if(instance.get(node.attribute) <= node.threshold)
				return classifyHelp(node.left, instance);
			else
				return classifyHelp(node.right, instance);
		}
		return node.classLabel;
	}

	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}

	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if(node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if(node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}
	
	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i ++)
		{
			int prediction = classify(testDataSet.get(i));
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual*100.0 / (double)numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}
}
