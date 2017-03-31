package ch.heigvd.wem;

import java.util.regex.*;

import java.util.ArrayList;
import java.util.Arrays;

import ch.heigvd.wem.data.Metadata;
import ch.heigvd.wem.data.VisitedPage;
import ch.heigvd.wem.labo1.Labo1;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class WebPageCrawler extends WebCrawler {

	private static WebPageIndexerQueue indexer = null;
	
	static Pattern p = Pattern.compile("(?:\\.)(\\w+)$");

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String currentPageDomain = referringPage.getWebURL().getDomain();
		String path = url.getPath();
		Matcher m = p.matcher(path);
		
		if(Labo1.DEBUG) {
			System.out.println("shouldVisit called");
			System.out.println("Referring page: " + referringPage.getWebURL());
			System.out.println("Url: " + url);
		}
		
		Boolean found = m.find();
		if (!found) {
			return true;
		}
		String ext = m.group();
		String linkDomain = url.getDomain();
		
		ArrayList<String> accepted = new ArrayList<String>();
		accepted.addAll(Arrays.asList("html", "php", "xhtml", "aspx", "htm"));
		
		if (currentPageDomain.equals(linkDomain) && accepted.contains(ext) ) {
			return true;
		}
		
		return false;
	
	}
	
	@Override
	public void visit(Page page) {
		
		if(indexer == null) {
			System.err.println("WebPageSaver doesn't contains a WebPageIndexer, you must set it in Labo1.java before starting the crawling");
			System.exit(1);
		}

		Metadata metadata = new Metadata();
		String content = null;
		
		ParseData data = page.getParseData();
		if(data instanceof HtmlParseData) {
			HtmlParseData htmlData = (HtmlParseData) data;
			metadata.setTitle(htmlData.getTitle());
			metadata.setUrl(page.getWebURL());
			metadata.setLinks(htmlData.getOutgoingUrls());
			content = htmlData.getText();
		}
		
		//we queue the page for indexer
		VisitedPage visitedPage = new VisitedPage(metadata, content);
		indexer.queueVisitedPage(visitedPage);
	}

	public static void setIndexerQueue(WebPageIndexerQueue indexer) { WebPageCrawler.indexer = indexer; }
	public static WebPageIndexerQueue getIndexerQueue() { return indexer; }
	
}
