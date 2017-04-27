package ch.heigvd.wem.linkanalysis;

import java.util.Vector;

/**
 * This class provides static methods to make link analysis.
 */
public class LinkAnalysis {

	/**
	 * Calculates and returns the hub vector.
	 * @param m Adjacency matrix.
	 * @param ac Auhtority vector of the previous step.
	 * @return Hub vector.
	 */
	public static Vector<Double> calculateHc (AdjacencyMatrix m, Vector<Double> ac) {
		int mSize = m.size();
		Vector<Double> result = new Vector<Double>(mSize);
		double sqSum = 0.d;

		// Initial algorithm
		for (int i = 0; i < mSize; i++) {
			double current = 0;
			for (int j = 0; j < mSize; j++) {
				if (m.get(i, j) == 1) {
					current += ac.get(j);
				}
			}
			result.add(current);
			sqSum += current * current;
		}
		
		// Normalization
		Vector<Double> normalized = new Vector<Double>(mSize);
		
		sqSum = Math.sqrt(sqSum);
		for (int i = 0; i < mSize; i++) {
			normalized.add(result.get(i) / sqSum);
		}

		return normalized;
	}

	/**
	 * Calculates and returns the authority vector.
	 * @param m Adjacency matrix.
	 * @param hc Hub of the previous step.
	 * @return Authority vector.
	 */
	public static Vector<Double> calculateAc (AdjacencyMatrix m, Vector<Double> hc) {
		int mSize = m.size();
		Vector<Double> result = new Vector<Double>(mSize);
		double sqSum = 0.d;

		// Initial algorithm
		for (int i = 0; i < mSize; i++) {
			double current = 0;
			for (int j = 0; j < mSize; j++) {
				if (m.get(j, i) == 1) {
					current += hc.get(j);
				}
			}
			result.add(current);
			sqSum += current * current;
		}
		
		// Normalization
		Vector<Double> normalized = new Vector<Double>(mSize);
		
		sqSum = Math.sqrt(sqSum);
		for (int i = 0; i < mSize; i++) {
			normalized.add(result.get(i) / sqSum);
		}

		return normalized;
	}

	/**
	 * Calculates and returns the pagerank vector.
	 * @param m Adjacency matrix.
	 * @param pr Pagerank vector of the previous step.
	 * @return Pagerank vector.
	 */
	public static Vector<Double> calculatePRc (AdjacencyMatrix m, Vector<Double> pr) {
		double d = 0.85d;
		int mSize = m.size();
		Vector<Double> result = new Vector<Double>(m.size());
		double sum = 0.d;

		// Initial algorithm
		for (int i = 0; i < mSize; i++) {
			double current = 0;
			for (int j = 0; j < mSize; j++) {
				current += (m.get(j, i) * pr.get(j));
			}
			current = current * d + (1.d - d);
			result.add(current);
			sum += current;
		}
		
		// Normalization
		Vector<Double> normalized = new Vector<Double>(mSize);
		
		for (int i = 0; i < mSize; i++) {
			normalized.add(result.get(i) / sum);
		}

		return normalized; 
	}
	
}
