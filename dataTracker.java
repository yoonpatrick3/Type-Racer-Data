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

public class dataTracker {
	//final static WebClient web2 = new WebClient(BrowserVersion.CHROME);
	
	@SuppressWarnings("resource")
	public static void main(String [] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		System.out.println("Which account should I check?");
		Scanner in = new Scanner(System.in);
		String username = in.nextLine();
		
		createData(username);
	}
	
	@SuppressWarnings("resource")
	public static void createData(String username) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
			
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
			
		WebClient webClient = new WebClient(BrowserVersion.CHROME); 
		
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		
		String raceHistoryUrl = "https://data.typeracer.com/pit/race_history?user=" + username + "&n=100&startDate";
		
		HtmlPage page = webClient.getPage(raceHistoryUrl);
		
		//webClient.getOptions().setJavaScriptEnabled(true);
		
		
		String pageContent=page.asText();
		
		
	
		
		ArrayList<Integer> raceNum =  new ArrayList<Integer>();
		ArrayList<Integer> wPM = new ArrayList<Integer>();
		ArrayList<Double> accuracy = new ArrayList<Double>();
		
		boolean continueLoop = true;
		
		while(continueLoop) {
			String[] splitWords = pageContent.split("\\s");
			for(int i =0 ;i<splitWords.length; i++) {
			//	System.out.println(splitWords[i]);
				if(splitWords[i].contains("WPM")) {
					int numRace, numWPM;
				
					
		
					numRace = Integer.parseInt(splitWords[i-2]);
					numWPM = Integer.parseInt(splitWords[i-1]);
					double perc = Double.parseDouble(splitWords[i+2].substring(0,4));
					
					raceNum.add(numRace);
					wPM.add(numWPM);
					accuracy.add(perc);
					
				
				}
			}
			
			if(pageContent.contains("load older results »")) {
				HtmlAnchor anchor = page.getAnchorByText("load older results »");
				HtmlPage newPage =  anchor.click();
				pageContent=newPage.asText();
			}
			else {
				continueLoop = false;
			}
			
		}
		webClient.close();
		double[][] textData = numWords(raceNum, username);
		
		
		String textFile = username + "DATAFILE.txt";
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile)));
		//out.println("Race\tWPM\tAccuracy\t#ofWords\tAVG Word Length");
		for(int i = 0; i< raceNum.size(); i++) {
			out.println(raceNum.get(i) + " " + wPM.get(i) + " " + accuracy.get(i) + " " + textData[i][0] + " " + textData[i][1]  + " " + textData[i][2]);
		}
		
		out.close();
		
		
		
	}
	
	public static String getText(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		@SuppressWarnings("resource")
		WebClient web2 = new WebClient(BrowserVersion.CHROME);
		  
		web2.getOptions().setThrowExceptionOnScriptError(false);
		web2.getOptions().setJavaScriptEnabled(true);
		
		HtmlPage page = web2.getPage(url);
		
		
		String pageContent=page.asText();
		String text = pageContent.split("Race text:")[1].split("Time:")[0];

		
		web2.close();
		return text;
	}
	
	public static double[][] numWords(ArrayList<Integer> race, String user) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		double[][] numWords = new double[race.size()][3];
		String punctuations = "!?.,:;'-\"";
		
	//	String numbers = "1234567890";
		
		System.out.println("Now creating data file");
		
		for(int i =0; i<race.size(); i++) {
			String url = "https://data.typeracer.com/pit/result?id=|tr:" +  user +  "|" + race.get(i);
			String text = getText(url);
			System.out.println((race.size() - i) + " pages left to check...");
			
			int numWordsLength = text.split(" ").length;
			
			String[] allChars = text.split("");
			
			double puncCounter = 0;
			double charCounter = 0;
		//	double numCounter = 0;
			
			for(int j = 0; j<allChars.length; j++) {
				if(!punctuations.contains(allChars[j]) && !allChars[j].equals(" ")) {
					charCounter++;
				}
				else if(punctuations.contains(allChars[j])) {
					puncCounter++;
				}
				/*else if(numbers.contains(allChars[j])){
					numCounter++;
				}*/
			}
			
			numWords[i][0] = numWordsLength;
			numWords[i][1] = charCounter/numWordsLength;
			numWords[i][2] = puncCounter;
		//	numWords[i][3] = numCounter;
			
		}
		
		System.out.println("Finished with creation!");
		return numWords;
	}
	
	
	
	
}