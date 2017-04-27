package ch.heigvd.wem.labo2;

import java.util.*;

import ch.heigvd.wem.linkanalysis.AdjacencyMatrix;

public class SearchIntegration {
	
	private AdjacencyMatrix am;
	private LinkedHashMap<String,Integer> map;
	
	public void createNodeEdge(List<String> urls){
		List<String> nodes = new LinkedList<String>();
		HashMap<String, LinkedList<String>> edges = 
				new HashMap<String, LinkedList<String>>();
		
		for(String url : urls){
			url = url.replaceFirst("^(http(s)?://)?(www\\./)?", "");
			for(String node : url.split("/")){
				if(!nodes.contains(node))
					nodes.add(node);
			}
		}
	}

		
}
