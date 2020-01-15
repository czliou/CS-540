import java.util.*;

/**
 * Class for internal organization of a Neural Network. There are 5 types of
 * nodes. Check the type attribute of the node for details. Feel free to modify
 * the provided function signatures to fit your own implementation
 */

public class Node {
	private int type = 0; // 0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
	public ArrayList<NodeWeightPair> parents = null; // Array List that will contain the parents (including the bias
														// node) with weights if applicable

	private double inputValue = 0.0;
	private double outputValue = 0.0;
	private double outputGradient = 0.0;
	private double delta = 0.0; // input gradient

	// Create a node with a specific type
	Node(int type) {
		if (type > 4 || type < 0) {
			System.out.println("Incorrect value for node type");
			System.exit(1);

		} else {
			this.type = type;
		}

		if (type == 2 || type == 4) {
			parents = new ArrayList<>();
		}
	}

	// For an input node sets the input value which will be the value of a
	// particular attribute
	public void setInput(double inputValue) {
		if (type == 0) { // If input node
			this.inputValue = inputValue;
		}
	}

	/**
	 * Calculate the output of a node. You can get this value by using getOutput()
	 */
	public void calculateOutput(ArrayList<Node> outputNodes) {
		if (type == 2 || type == 4) { // Not an input or bias node
			outputValue = 0.0;
			for (NodeWeightPair parent : parents)
				outputValue += parent.weight * parent.node.getOutput();

			if (type == 2)
				outputValue = Math.max(0, outputValue);
			else {
				//double sum = 0.0;
				outputValue = Math.exp(outputValue);
			}
		}
	}
	
	public void setOutput(double denominator) {
		outputValue = outputValue/denominator;
		
	}

	// Gets the output value
	public double getOutput() {

		if (type == 0) { // Input node
			return inputValue;
		} else if (type == 1 || type == 3) { // Bias node
			return 1.00;
		} else {
			return outputValue;
		}

	}

	// Calculate the delta value of a node.
	public void calculateDelta(ArrayList<Node> outputNodes, int nodeIndex, double targetvalue) {
		if (type == 2 || type == 4) {
			if (type == 4) {
				delta = targetvalue - getOutput();
			} else {
				double temp = 0.0;
				for (NodeWeightPair parent : parents)
					temp += parent.weight * parent.node.getOutput();
				if (temp <= 0)
					delta = 0.0;
				else {
					delta = 0.0;
					for (Node node : outputNodes) {
						for(int i = 0; i < node.parents.size(); i++) {
							if(node.parents.get(i).node.equals(this)) 
								delta += node.parents.get(i).weight * node.delta;
						}
					}
				}	
			}
		}
	}

	// Update the weights between parents node and current node
	public void updateWeight(double learningRate) {
		if (type == 2 || type == 4) {
			for (NodeWeightPair parent : parents)
				parent.weight += learningRate * parent.node.getOutput() * delta;
		}
	}
}
