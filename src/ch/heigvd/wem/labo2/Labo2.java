package ch.heigvd.wem.labo2;

import java.util.HashMap;
import java.util.Vector;

import ch.heigvd.wem.linkanalysis.AdjacencyMatrix;
import ch.heigvd.wem.linkanalysis.GraphFileReader;
import ch.heigvd.wem.linkanalysis.*;

public class Labo2 {

	public static void main(String[] args) {
		GraphFileReader fileReader = new GraphFileReader("graph_reference.txt");
		AdjacencyMatrix matrix = fileReader.getAdjacencyMatrix();
		HashMap<String, Integer> map = fileReader.getNodeMapping();
		
		int mSize = matrix.size();
		
		Vector<Double> hc = new Vector<Double>();
		Vector<Double> ac = new Vector<Double>();
		Vector<Double> pr = new Vector<Double>();
		
		// Initialization
		for (int i = 0; i < mSize; i++) {
			hc.add(1.d);
			ac.add(1.d);
			pr.add(1.d/(double)mSize);
		}
		
		// Computation
		for (int i = 0; i < 20; i++) {
			System.out.println("Iteration " + i);
			Vector<Double> newHc = LinkAnalysis.calculateHc(matrix, ac);
			Vector<Double> newAc = LinkAnalysis.calculateAc(matrix, hc);
			hc = newHc;
			ac = newAc;
			pr = LinkAnalysis.calculatePRc(matrix.getTransitionMatrix(), pr);
			System.out.println(hc);
			System.out.println(ac);
			System.out.println(pr);
		}
	}
}
