package ch.heigvd.wem.labo2;

import java.util.HashMap;
import java.util.Vector;

import ch.heigvd.wem.linkanalysis.AdjacencyMatrix;
import ch.heigvd.wem.linkanalysis.GraphFileReader;
import ch.heigvd.wem.linkanalysis.*;

public class Labo2 {

	public static void main(String[] args) {
		// Initialize necessary structures
		GraphFileReader fileReader = new GraphFileReader("graph_reference.txt");
		AdjacencyMatrix matrix = fileReader.getAdjacencyMatrix();
		HashMap<String, Integer> initialMap = fileReader.getNodeMapping();
		HashMap<Integer, String> reversedMap = new HashMap<>(); // The map we get from GraphFileReader is more useful once reversed
		for(HashMap.Entry<String, Integer> entry : initialMap.entrySet()){
		    reversedMap.put(entry.getValue(), entry.getKey());
		}
		
		int mSize = matrix.size();
		
		Vector<Double> hc = new Vector<Double>(); // Hub vector
		Vector<Double> ac = new Vector<Double>(); // Authority vector
		Vector<Double> pr = new Vector<Double>(); // PageRank vector
		
		// Initialization
		for (int i = 0; i < mSize; i++) {
			hc.add(1.d); // Hub and authority values are initialized to 1
			ac.add(1.d);
			pr.add(1.d/(double)mSize); // PageRank values are initialized to 1/N
		}
		
		// Computation
		for (int i = 0; i < 5; i++) {
			Vector<Double> newHc = LinkAnalysis.calculateHc(matrix, ac);
			Vector<Double> newAc = LinkAnalysis.calculateAc(matrix, hc);
			hc = newHc;
			ac = newAc;
			pr = LinkAnalysis.calculatePRc(matrix.getTransitionMatrix(), pr);
		}
		
		// Print results
		for (int i = 0; i < mSize; i++) {
			System.out.println("*** Node " + i + " : " + reversedMap.get(i) + " ***");
			System.out.println("      Hub : " + hc.get(i));
			System.out.println("Authority : " + ac.get(i));
			System.out.println(" PageRank : " + pr.get(i));
			System.out.println();
		}
	}
}
