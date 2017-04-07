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
			HashMap<Long, HashMap<Retriever.WeightingType, Float>> documents = this.weightByWord.get(word);
			
			float df = 0;
			
			// TODO : Compute tf, idf and normalized frequency. This code was an attempt, don't hesitate to delete
			// everything.
			
			for (Long docId : documents.keySet()) {
				if (!documents.containsKey(docId)) {
					documents.put(docId, new HashMap<Retriever.WeightingType, Float>());
				}
				HashMap<Retriever.WeightingType, Float> weights = documents.get(docId);
				HashMap<String, Integer> words = this.docToWord.get(docId);
				float tf = words.get(word);
				if (tf > 0) {
					df += 1;
				}
				float tfidf = 0; // TODO
				weights.put(Retriever.WeightingType.NORMALIZED_FREQUENCY, tf);
				weights.put(Retriever.WeightingType.TF_IDF, tfidf);
			}
			
		}
	}
	
	private float normFreq(int freq, int maxFreq) {
		return (float)((float)freq / (float)maxFreq);
	}
}
