package ch.heigvd.wem.labo2;

import java.util.*;

import org.jsoup.Jsoup;

public class test {

	
	public static void main(String[] args) {
			List<String> sss = new ArrayList<String>();
			sss.add("http://iict.heig-vd.ch/recherche");
			sss.add("http://iict.heig-vd.ch/a-propos");
			sss.add("http://iict.heig-vd.ch/team");
			sss.add("http://iict.heig-vd.ch/team/show/85/brochet-xavier/");
			GraphUrlReader gur = new GraphUrlReader("http://iict.heig-vd.ch",sss);
		}

}
