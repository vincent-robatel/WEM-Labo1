package ch.heigvd.wem.labo1;

import java.util.HashMap;

import ch.heigvd.wem.interfaces.Index;

public class Labo1Index extends Index {
	private HashMap<String, HashMap<Integer, Integer>> map;
	
	public Labo1Index() {
		super();
		this.map = new HashMap<String, HashMap<Integer, Integer>>();
	}

	public void add(Integer docId, String word) {
		if (!this.map.containsKey(word)) {
			this.map.put(word, new HashMap<Integer, Integer>());
		}
		HashMap<Integer, Integer> wordMap = this.map.get(word);
		if (!wordMap.containsKey(docId)) {
			wordMap.put(docId, 0);
		}
		int current = wordMap.get(docId);
		wordMap.put(docId, current++);
	}
}
