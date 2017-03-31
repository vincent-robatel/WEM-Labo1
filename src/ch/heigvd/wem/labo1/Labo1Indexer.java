package ch.heigvd.wem.labo1;

import ch.heigvd.wem.data.Metadata;
import ch.heigvd.wem.interfaces.Index;
import ch.heigvd.wem.interfaces.Indexer;

public class Labo1Indexer implements Indexer {
	
	private Labo1Index index = new Labo1Index();

	@Override
	public void index(Metadata metadata, String content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finalizeIndexation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Index getIndex() {
		// TODO Auto-generated method stub
		return null;
	}
}
