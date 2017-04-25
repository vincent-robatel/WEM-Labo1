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
		HashMap<String, Integer> mapping = fileReader.getNodeMapping();
		
		Vector<Double> hc = new Vector<Double>();
		Vector<Double> ac = new Vector<Double>();
		Vector<Double> pr = new Vector<Double>();
		
		Vector<Double> newHc = new Vector<Double>();
		Vector<Double> newAc = new Vector<Double>();
		
		int mSize = matrix.size();
		for (int i = 0; i < mSize; i++) {
			for (int j = 0; j < mSize; j++) {
				newHc = LinkAnalysis.calculateHc(matrix, ac);
				newAc = LinkAnalysis.calculateHc(matrix, hc);
				hc = newHc;
				ac = newAc;
				pr = LinkAnalysis.calculatePRc(matrix, pr);
			}
		}
	}
}
