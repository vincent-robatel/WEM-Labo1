package ch.heigvd.wem.labo1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ch.heigvd.wem.data.Metadata;
import ch.heigvd.wem.interfaces.Index;
import ch.heigvd.wem.interfaces.Indexer;

public class Labo1Indexer implements Indexer {
	
	private Labo1Index index = new Labo1Index();
	private static ArrayList<String> stopWords = new ArrayList<String>();

	public Labo1Indexer() {
		super();
		loadStopWords();
	}
	
	private static void loadStopWords() {
		if (stopWords.isEmpty()) {
			try {
				// Chargement des stop words EN
				BufferedReader br = new BufferedReader(new FileReader("common_words"));
			    String line;
			    while ((line = br.readLine()) != null) {
			       stopWords.add(line);
			    }
			    br.close();
			    // Chargement des stop words FR
				br = new BufferedReader(new FileReader("common_words_fr"));
			    while ((line = br.readLine()) != null) {
			       stopWords.add(line);
			    }
			    br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void index(Metadata metadata, String content) {
		ArrayList<String> tokens = tokenize(content);
		
		// Adding index and inverted index
		for (String token : tokens) {
			index.add(metadata.getDocID(), token, metadata.getUrl().toString());
		}
	}
	
	@Override
	public void finalizeIndexation() {
		index.finalize();
		System.out.println("Indexation done");
	}

	@Override
	public Index getIndex() {
		return index;
	}
	
	public static ArrayList<String> tokenize(String content) {
		loadStopWords();
		// Tokenization
		String[] initialTokens = content.split("\\s+|\\.|,|:|;|-|\\+|/|\\\\|\\W+['\"`´]|['\"`´]\\W+");
		// Pas le plus efficace du monde, mais concis et compréhensible.
		for (int i = 0; i < initialTokens.length; i++) {
			initialTokens[i] = initialTokens[i].trim().replaceFirst("^['\"`]", ""); // Enlève l'éventuel premier apostrophe
			initialTokens[i] = initialTokens[i].replaceFirst("['\"`]$", ""); // Enlève l'éventuel dernier apostrophe
			initialTokens[i] = initialTokens[i].toLowerCase().trim();
		}
		ArrayList<String> tokens = new ArrayList<String>();
		
		// Keeping only non-stop-words
		for (String token : initialTokens) {
			boolean toAdd = true;
			for (String stopWord : stopWords) {
				if (stopWord.trim().contains(token)) {
					toAdd = false;
					break;
				}
			}
			if (toAdd) {
				tokens.add(token);
			}
		}
		
		return tokens;
	}
}
