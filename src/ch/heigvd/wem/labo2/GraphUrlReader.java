package ch.heigvd.wem.labo2;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.heigvd.wem.linkanalysis.AdjacencyMatrix;
import ch.heigvd.wem.linkanalysis.ArrayListMatrix;

public class GraphUrlReader {

	private AdjacencyMatrix am;
	private LinkedHashMap<String,Integer> map;
	
	public GraphUrlReader(String startUrl, List<String> urls) {
		
		// Init structures for storing information
		List<String> nodes = new LinkedList<String>();
		HashMap<String, LinkedList<String>> edges = 
						new HashMap<String, LinkedList<String>>();
		
		Document doc;
		LinkedList<String> list;
		//For each urls extract all <a href=".."> then stock it in nodes and egdes
		for(String url : urls){
			try {
				nodes.add(url);
				System.out.println(url);
				//get the html code of the url
				doc = Jsoup.connect(url).get();
				Elements links = doc.select("a[href]");
			
				for (Element link : links) {
					String href = link.attr("href");
	            	if(linkIntern(urls, url, href, startUrl)){
	            		String linkFormated = formatLink(startUrl, href);
	            		if(!edges.containsKey(url)){
	            			list = new LinkedList<String>();
	            			edges.put(url, list);
	            		}
	            		if(!edges.get(url).contains(linkFormated)){
	            			edges.get(url).add(linkFormated);
	            			System.out.println("  -"+linkFormated);
	            		}
	            	}
	            }
				System.out.println("---------------------------------------------");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Init members
		am  = new ArrayListMatrix(nodes.size());
		map = new LinkedHashMap<String,Integer>();

		// Build nodes
		int i=0;
		for (String node : nodes)
			map.put(node, i++);

		// Build edges
		for (String key : edges.keySet())
			for (String value : edges.get(key))
				am.set(map.get(key), map.get(value), 1);
	}

	/**
	 * Returns the adjacency matrix.
	 * @return the adjacency matrix.
	 */
	public AdjacencyMatrix getAdjacencyMatrix() {
		return am;
	}

	/**
	 * Returns the node name mapping. This map associates a node name to its matrix 
	 * index.
	 * @return a map associating a node name to its index in the matrix.
	 */
	public HashMap<String,Integer> getNodeMapping() {
		return map;
	}
	
	
	//Format the link in order to have always the same url
	private String formatLink(String startUrl, String href) {
		StringBuilder sb = new StringBuilder();
		if(!href.contains(startUrl))
			if(href.startsWith("/"))
				sb.append(startUrl).append(href);
			else
				sb.append(startUrl).append("/").append(href);
		return sb.toString();
	}

	//Check if the href link is an inside link 
	private boolean linkIntern(List<String> urls, String currentUrl, String link, String startUrl){
		boolean check = false;
		for(String url : urls)
			if(url.contains(link) && !currentUrl.equals(formatLink(startUrl,link)) && !link.equals("/")) check = true;
		return check;
	}
}
