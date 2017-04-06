package ch.heigvd.wem.labo1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import ch.heigvd.wem.data.Metadata;
import ch.heigvd.wem.interfaces.Index;
import ch.heigvd.wem.interfaces.Indexer;

public class Labo1Indexer implements Indexer {
	
	private Labo1Index index = new Labo1Index();
	private ArrayList<String> stopWords;

	public Labo1Indexer() {
		super();
		ArrayList<String> stopWordsTemp = new ArrayList<String>();
		try {
			// Chargement des stop words EN
			BufferedReader br = new BufferedReader(new FileReader("common_words"));
		    String line;
		    while ((line = br.readLine()) != null) {
		       stopWordsTemp.add(line);
		    }
		    // Chargement des stop words FR
			br = new BufferedReader(new FileReader("common_words_fr"));
		    while ((line = br.readLine()) != null) {
		       stopWordsTemp.add(line);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void index(Metadata metadata, String content) {
		// Tokenization
		String[] initialTokens = content.split(" |,|_|\\+|\\(|\\)|\\[|\\]|\\.|;|/");
		// Pas le plus efficace du monde, mais concis et compréhensible.
		for (int i = 0; i < initialTokens.length; i++) {
			initialTokens[i] = initialTokens[i].replaceFirst("^['\"`]", ""); // Enlève l'éventuel premier apostrophe
			initialTokens[i] = initialTokens[i].replaceFirst("['\"`]$", ""); // Enlève l'éventuel dernier apostrophe
		}
		ArrayList<String> tokens = new ArrayList<String>();
		
		// Keeping only non-stop-words
		for (String token : initialTokens) {
			for (String stopWord : stopWords) {
				if (!stopWord.trim().contains(token)) {
					tokens.add(token);
				}
			}
		}
		
		
	}

	@Override
	public void finalizeIndexation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Index getIndex() {
		return index;
	}
}
