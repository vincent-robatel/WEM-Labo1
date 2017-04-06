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
		String targetDomain = Labo1.START_URL;
		String path = url.getPath();
		Matcher m = p.matcher(path);
		
		if(Labo1.DEBUG) {
			System.out.println(url + "to add, by " + referringPage.getWebURL());
		}
		
		// Only visit if it is on the same domain
		if (url.getURL().startsWith(targetDomain)) {
			// Does the path have an extension at the end
			Boolean found = m.find();
			// If not then we don't know what it is, visit it.
			if (!found) {
				System.out.println(url + " ADDED");
				return true;
			}
			String ext = m.group();
			
			ArrayList<String> accepted = new ArrayList<String>();
			accepted.addAll(Arrays.asList("html", "php", "xhtml", "aspx", "htm", "txt"));
			// If we know what it is and it's not a recognized format, don't visit it.
			if (accepted.contains(ext) ) {
				System.out.println(url + " ADDED");
				return true;
			}
			System.out.println(url + " REFUSED (extension)");
			return false;
		} else {
			System.out.println(url + " REFUSED (domain)");
			return false;
		}
	
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
