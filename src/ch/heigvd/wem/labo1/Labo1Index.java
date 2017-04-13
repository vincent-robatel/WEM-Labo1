package ch.heigvd.wem.labo1;

import java.util.HashMap;

import ch.heigvd.wem.interfaces.Index;
import ch.heigvd.wem.interfaces.Retriever.WeightingType;

public class Labo1Index extends Index {
	private static final long serialVersionUID = -9025028578135182266L;
	private HashMap<String, HashMap<Long, Integer>> inversedIndex;
	private HashMap<Long, HashMap<String, Integer>> documentIndex;
	private HashMap<String, HashMap<WeightingType, HashMap<Long, Float>>> weightByWord;
	private HashMap<Long, HashMap<WeightingType, HashMap<String, Float>>> weightByDoc;
	
	public Labo1Index() {
		this.inversedIndex = new HashMap<String, HashMap<Long, Integer>>();
		this.documentIndex = new HashMap<Long, HashMap<String, Integer>>();
		this.weightByWord = new HashMap<String, HashMap<WeightingType, HashMap<Long, Float>>>();
		this.weightByDoc  = new HashMap<Long, HashMap<WeightingType, HashMap<String, Float>>>();
	}

	public synchronized void add(Long docId, String word) {
		
		if (!this.inversedIndex.containsKey(word)) {
			this.inversedIndex.put(word, new HashMap<Long, Integer>());
		}
		if (!this.documentIndex.containsKey(docId)) {
			this.documentIndex.put(docId, new HashMap<String, Integer>());
		}
		
		HashMap<Long, Integer> wordMap =  this.inversedIndex.get(word);
		HashMap<String, Integer> docMap = this.documentIndex.get(docId);
		
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
		// Compute weights for each word
		for (String word : this.inversedIndex.keySet()) {
			if (!this.weightByWord.containsKey(word)) {
				this.weightByWord.put(word, new HashMap<WeightingType, HashMap<Long, Float>>());
				this.weightByWord.get(word).put(WeightingType.NORMALIZED_FREQUENCY, new HashMap<Long, Float>());
				this.weightByWord.get(word).put(WeightingType.TF_IDF, new HashMap<Long, Float>());
			}
			// df is : in how many docs does the word appear
			int df = this.inversedIndex.get(word).keySet().size();
			int nbDocs = this.documentIndex.size();
			// idf is : log2(N / df) where N is the total number of docs
			float idf = (float) log2(nbDocs / (float)df);
			// Now check for each doc :
			for (Long docId : this.inversedIndex.get(word).keySet()) {
				if (!this.weightByDoc.containsKey(docId)) {
					this.weightByDoc.put(docId, new HashMap<WeightingType, HashMap<String, Float>>());
					this.weightByDoc.get(docId).put(WeightingType.NORMALIZED_FREQUENCY, new HashMap<String, Float>());
					this.weightByDoc.get(docId).put(WeightingType.TF_IDF, new HashMap<String, Float>());
				}
				// tf is : how many times does the term appear in that doc
				int tf = this.inversedIndex.get(word).get(docId);
				this.weightByWord.get(word).get(WeightingType.TF_IDF).put(docId,               (float)log2(tf + 1) * idf);
				this.weightByWord.get(word).get(WeightingType.NORMALIZED_FREQUENCY).put(docId, (float)tf);
				this.weightByDoc.get(docId).get(WeightingType.TF_IDF).put(word,                (float)log2(tf + 1) * idf);
				this.weightByDoc.get(docId).get(WeightingType.NORMALIZED_FREQUENCY) .put(word, (float)tf);
			}			
		}
		
		// Now we have all the weights, but no normalization has been done. We need to look for maximums in order to
		// reduce the range of values to [0; 1]
		
		// We normalize by document, as stated in the page 4 of the :
		// "max j () est la valeur maximale de la pondération dans _une_ page web"
		for (Long docId: this.documentIndex.keySet()) {
			float maxTfIdf = 0;
			float maxNormF = 0;
			float currentTfIdf = 0;
			float currentNormF = 0;
			// Look for the max value of that document
			for (String word : this.documentIndex.get(docId).keySet()) {
				currentTfIdf = this.weightByWord.get(word).get(WeightingType.TF_IDF).get(docId);
				currentNormF = this.weightByWord.get(word).get(WeightingType.NORMALIZED_FREQUENCY).get(docId);
				if (currentTfIdf > maxTfIdf) {
					maxTfIdf = currentTfIdf;
				}
				if (currentNormF > maxNormF) {
					maxNormF = currentNormF;
				}
			}
			float tfIdfValue;
			float normFValue;
			// Actually replace the raw values by the normalized ones
			for (String word : this.documentIndex.get(docId).keySet()) {
				// TfIdf in the index
				tfIdfValue = this.weightByDoc.get(docId).get(WeightingType.TF_IDF).get(word);
				this.weightByDoc.get(docId).get(WeightingType.TF_IDF).put(word, tfIdfValue / maxTfIdf);
				// Normalized Frequency in the index
				normFValue = this.weightByDoc.get(docId).get(WeightingType.NORMALIZED_FREQUENCY).get(word);
				this.weightByDoc.get(docId).get(WeightingType.NORMALIZED_FREQUENCY).put(word, normFValue / maxNormF);
				// TfIdf in the inverted index
				tfIdfValue = this.weightByWord.get(word).get(WeightingType.TF_IDF).get(docId);
				this.weightByWord.get(word).get(WeightingType.TF_IDF).put(docId, tfIdfValue / maxTfIdf);
				// Normalized Frequency in the inverted index
				normFValue = this.weightByWord.get(word).get(WeightingType.NORMALIZED_FREQUENCY).get(docId);
				this.weightByWord.get(word).get(WeightingType.NORMALIZED_FREQUENCY).put(docId, normFValue / maxNormF);
			}
		}
	}
	
	public HashMap<String, Float> getDocWeights(WeightingType weightingType, Long docId) {
		return this.weightByDoc.get(weightingType).get(docId);
	}
	
	public HashMap<Long, Float> getWordWeights(WeightingType weightingType, String word) {
		HashMap<Long, Float> map = this.weightByWord.get(weightingType).get(word);
		return map;
	}
	
	private float log2(float n) {
		return (float) (Math.log(n) / Math.log(2));
	}
}
