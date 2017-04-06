package ch.heigvd.wem.labo1;

import java.util.HashMap;

import ch.heigvd.wem.interfaces.Index;

public class Labo1Index extends Index {
	private static final long serialVersionUID = -9025028578135182266L;
	private HashMap<String, HashMap<Long, Integer>> wordToDoc;
	private HashMap<Long, HashMap<String, Integer>> docToWord;
	
	public Labo1Index() {
		this.wordToDoc = new HashMap<String, HashMap<Long, Integer>>();
		this.docToWord = new HashMap<Long, HashMap<String, Integer>>();
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
}
