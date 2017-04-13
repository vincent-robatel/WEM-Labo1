package ch.heigvd.wem.labo1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
		Map<Long, Double> searchedDoc = new HashMap<Long, Double>();
		for (Entry<Long, Float> entry : wordDocs.entrySet()) {
			searchedDoc.put(entry.getKey(), entry.getValue().doubleValue());
		}
		return searchedDoc;
	}

	@Override
	public Map<Long, Double> executeQuery(String query) {
		//tokenize the query
		List<String> words = Labo1Indexer.tokenize(query);
		List<String> terms = new ArrayList<String>();
		for(String word : words){
			if(((Labo1Index) index).isWordExist(word))
				terms.add(word);
		}
		Map<Long, Double> termPonderationByDocs = null;
		//Map contain all documents vectors
		Map<Long, Map<String,Double>> vectorsD = new HashMap<Long, Map<String,Double>>();
		Map<String, Double> vectorQ = new HashMap<String, Double>();
		Map<Long, Double> resultCosScore = new HashMap<Long, Double>();
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
		
		return sortResults(resultCosScore);
	}

	
	//Calcul the COSINE SCORE
	private Double cosineScore(Map<String,Double> d, Map<String,Double> q){
		Double result = pairDot(d, q) / (vectorNorm(d) * vectorNorm(q));
		if(Double.isNaN(result))
			result = new Double(0);
			return result;
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
	
	private Map<Long, Double> sortResults(Map<Long, Double> results){
		List<Map.Entry<Long, Double>> list = new LinkedList<Map.Entry<Long, Double>>(results.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Long, Double>>() {
            public int compare(Map.Entry<Long, Double> o1,
                               Map.Entry<Long, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
		Map<Long, Double> sortedMap = new LinkedHashMap<Long, Double>();
        for (Map.Entry<Long, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
		return sortedMap;
	}
}


