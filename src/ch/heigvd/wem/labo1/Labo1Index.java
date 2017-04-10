package ch.heigvd.wem.labo1;

import java.util.HashMap;

import ch.heigvd.wem.interfaces.Index;
import ch.heigvd.wem.interfaces.Retriever;

public class Labo1Index extends Index {
	private static final long serialVersionUID = -9025028578135182266L;
	private HashMap<String, HashMap<Long, Integer>> wordToDoc;
	private HashMap<Long, HashMap<String, Integer>> docToWord;
	private HashMap<String, HashMap<Long, HashMap<Retriever.WeightingType, Float>>> weightByWord;
	private HashMap<Long, HashMap<String, HashMap<Retriever.WeightingType, Float>>> weightByDoc;
	
	public Labo1Index() {
		this.wordToDoc = new HashMap<String, HashMap<Long, Integer>>();
		this.docToWord = new HashMap<Long, HashMap<String, Integer>>();
		this.weightByWord = new HashMap<String, HashMap<Long, HashMap<Retriever.WeightingType, Float>>>();
		this.weightByDoc  = new HashMap<Long, HashMap<String, HashMap<Retriever.WeightingType, Float>>>();
	}

	public synchronized void add(Long docId, String word) {
		
		if (!this.wordToDoc.containsKey(word)) {
			this.wordToDoc.put(word, new HashMap<Long, Integer>());
		}
		if (!this.docToWord.containsKey(docId)) {
			this.docToWord.put(docId, new HashMap<String, Integer>());
		}
		
		HashMap<Long, Integer> wordMap =  this.wordToDoc.get(word);
		HashMap<String, Integer> docMap = this.docToWord.get(docId);
		
		if (!wordMap.containsKey(docId)) {
			wordMap.put(docId, 0);
		}
		Integer currentWord = wordMap.get(docId);
		wordMap.put(docId, currentWord + 1);
		
		if (!docMap.containsKey(word)) {
			docMap.put(word, 0);
		}
		Integer currentDoc = docMap.get(word);
		docMap.put(word, currentDoc + 1);
	}
	
	public void finalize() {
		// Calc weight by word
		for (String word : this.wordToDoc.keySet()) {
			if (!this.weightByWord.containsKey(word)) {
				this.weightByWord.put(word, new HashMap<Long, HashMap<Retriever.WeightingType, Float>>());
			}			
			int df = this.wordToDoc.get(word).keySet().size();
			float idf = 1.0f / (float)df;
			for (Long docId : this.wordToDoc.get(word).keySet()) {
				int tf = this.wordToDoc.get(word).get(docId);
				this.weightByWord.get(word).get(docId).put(Retriever.WeightingType.TF_IDF,               (float)tf * idf);
				this.weightByWord.get(word).get(docId).put(Retriever.WeightingType.NORMALIZED_FREQUENCY, (float)tf);
				this.weightByDoc.get(docId).get(word) .put(Retriever.WeightingType.TF_IDF,               (float)tf * idf);
				this.weightByDoc.get(docId).get(word) .put(Retriever.WeightingType.NORMALIZED_FREQUENCY, (float)tf);
			}			
		}
	}
	
	public HashMap<String, HashMap<Retriever.WeightingType, Float>> getDocWeights(Long docId) {
		return this.weightByDoc.get(docId);
	}
	
	public HashMap<Long, HashMap<Retriever.WeightingType, Float>> getWordWeights(String word) {
		return this.weightByWord.get(word);
	}
	
	private float normFreq(int freq, int maxFreq) {
		return (float)((float)freq / (float)maxFreq);
	}
}
