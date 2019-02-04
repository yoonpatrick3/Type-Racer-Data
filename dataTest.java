package TypeRacer;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class dataTest {
	@SuppressWarnings("resource")
	public static void main(String [] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		  java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		  
		  
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
			
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setJavaScriptEnabled(true);
			
			String raceHistoryUrl = "https://data.typeracer.com/pit/result?id=|tr:yoontestdata|86";
			
			HtmlPage page = webClient.getPage(raceHistoryUrl);
			String pageContent=page.asText();
			
			//System.out.println(pageContent);
			
			String text = pageContent.split("Race text:")[1].split("Time:")[0];
		
			
		
			System.out.println(text);
			
			String punctuations = "!?.,:;'-" + "\"";
			String[] allChars = text.split("");
			
			double puncCounter = 0;
		//	double charCounter = 0;
			
			for(int j = 0; j<allChars.length;j++) {
			 if(punctuations.contains(allChars[j])) {
					puncCounter++;
				}
			}
			
			System.out.println(puncCounter);
	}
}
