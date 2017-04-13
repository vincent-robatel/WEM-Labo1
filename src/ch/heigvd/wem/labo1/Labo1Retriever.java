package ch.heigvd.wem.labo1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import ch.heigvd.wem.interfaces.Index;
import ch.heigvd.wem.interfaces.Retriever;

public class Labo1Retriever extends Retriever {

	public Labo1Retriever(Index index, WeightingType weightingType) {
		super(index, weightingType);
	}

	@Override
	public Map<String, Double> searchDocument(Integer docmentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, Double> searchTerm(String term) {
		HashMap<Long, Float> wordDocs = ((Labo1Index) index).getWordWeights(this.weightingType, term);
		Map<Long, Double> searchedDoc = new TreeMap<Long, Double>();
		for (Entry<Long, Float> entry : wordDocs.entrySet()) {
			searchedDoc.put(entry.getKey(), entry.getValue().doubleValue());
		}
		return searchedDoc;
	}

	@Override
	public Map<Long, Double> executeQuery(String query) {
		//tokenize the query
		ArrayList<String> terms = Labo1Indexer.tokenize(query);
		Map<Long, Double> termPonderationByDocs = null;
		//Map contain all documents vectors
		Map<Long, Map<String,Double>> vectorsD = new HashMap<Long, Map<String,Double>>();
		Map<String, Double> vectorQ = new HashMap<String, Double>();
		Map<Long, Double> resultCosScore = new TreeMap<Long, Double>(Collections.reverseOrder());
		for(String term : terms){
			//create the vector q
			vectorQ.put(term, new Double(1));
			//get all doc and ponderation for one term
			termPonderationByDocs = searchTerm(term);
			//add doc/ponderation to the map doc Vector
			for(Entry<Long, Double> entry : termPonderationByDocs.entrySet()){
				if(!vectorsD.containsKey(entry.getKey()))
					vectorsD.put(entry.getKey(), new HashMap<String,Double>());
				vectorsD.get(entry.getKey()).put(term, entry.getValue());
			}
		}
		//add missing term with a 0 ponderation and compute cosine score
		for(Entry<Long, Map<String,Double>> entry : vectorsD.entrySet()){
			for(String term : terms){
				if(!entry.getValue().containsKey(term))
					entry.getValue().put(term, new Double(0));
			}
			resultCosScore.put(entry.getKey(), cosineScore(entry.getValue(), vectorQ));
		}
		
		return resultCosScore;
	}

	//Calcul the COSINE SCORE
	private Double cosineScore(Map<String,Double> d, Map<String,Double> q){
		return pairDot(d, q) / (vectorNorm(d) * vectorNorm(q));
	}
	
	private Double pairDot(Map<String,Double> d, Map<String,Double> q){
		Double sum = new Double(0);
		for(Entry<String,Double> entry : d.entrySet()){
			sum += entry.getValue() * q.get(entry.getKey());
		}
		return sum;
	}
	
	private Double vectorNorm(Map<String,Double> x){
		Double sum = new Double(0);
		for(Entry<String,Double> entry : x.entrySet()){
			sum += entry.getValue() * entry.getValue();
		}
		return Math.sqrt(sum);
	}
	
}


