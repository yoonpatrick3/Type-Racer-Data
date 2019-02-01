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
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class dataTracker {
	
	@SuppressWarnings("resource")
	public static void main(String [] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		System.out.println("\fWhich account should I check?");
		Scanner in = new Scanner(System.in);
		String username = in.nextLine();
		
		createData(username);
	}
	
	@SuppressWarnings("resource")
	public static void createData(String username) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		  java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		
		String raceHistoryUrl = "https://data.typeracer.com/pit/race_history?user=" + username + "&n=100&startDate";
		
		HtmlPage page = webClient.getPage(raceHistoryUrl);
		
		webClient.getOptions().setJavaScriptEnabled(true);
		
		
		String pageContent=page.asText();
		String[] test = pageContent.split(" ");
		
		ArrayList<String> data = new ArrayList<String>();
		boolean firstIndex = false;
		
		for(int i = 0; i<test.length ;i++) {
			test[i] = test[i].replaceAll("\\s","");
			//System.out.println(test[i]);
			
			if (firstIndex) {
				data.add(test[i]);
			}
			else if (test[i].contains("Options")) {
				firstIndex = true;
				data.add(test[i]);
			}
		}
		
		ArrayList<Integer> raceNum =  new ArrayList<Integer>();
		ArrayList<Integer> wPM = new ArrayList<Integer>();
		ArrayList<Double> accuracy = new ArrayList<Double>();
		
		int firstRace = Integer.parseInt(data.get(0).substring(36,38));
		int firstWPM = Integer.parseInt(data.get(0).substring(38));
		
		double firstPerc =  Double.parseDouble(data.get(2).substring(0,4));
		
		raceNum.add(firstRace);
		wPM.add(firstWPM);
		accuracy.add(firstPerc);
		
		for(int i =7 ;i<data.size(); i++) {
			if(data.get(i).equals("WPM")) {
				int numRace, numWPM;
				
				if(data.get(i-1).length()==3) {
					numRace = Integer.parseInt(data.get(i-1).substring(0,1));
					numWPM =  Integer.parseInt(data.get(i-1).substring(1));
				}
				else {
					numRace = Integer.parseInt(data.get(i-1).substring(0,2));
					numWPM =  Integer.parseInt(data.get(i-1).substring(2));
					
				}
				double perc = Double.parseDouble(data.get(i+1).substring(0,4));
				
				raceNum.add(numRace);
				wPM.add(numWPM);
				accuracy.add(perc);
				
			
			}
		}
		
		double[][] textData = numWords(raceNum, username);
		
		
		String textFile = username + "DATAFILE.txt";
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile)));
		//out.println("Race\tWPM\tAccuracy\t#ofWords\tAVG Word Length");
		for(int i = 0; i< raceNum.size(); i++) {
			out.println(raceNum.get(i) + " " + wPM.get(i) + " " + accuracy.get(i) + " " + textData[i][0] + " " + textData[i][1]);
		}
		
		out.close();
		
		
		
	}
	
	public static String getText(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		@SuppressWarnings("resource")
		
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		  java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		HtmlPage page = webClient.getPage(url);
		
		webClient.getOptions().setJavaScriptEnabled(true);
		
		
		String pageContent=page.asText();
		String text = pageContent.split("Race text:")[1].split("Time:")[0];
		return text;
	}
	
	public static double[][] numWords(ArrayList<Integer> race, String user) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		double[][] numWords = new double[race.size()][2];
		String punctuations = ".,:; ";
		
		System.out.println("Now creating data file");
		
		for(int i =0; i<race.size(); i++) {
			String url = "https://data.typeracer.com/pit/result?id=|tr:" +  user +  "|" + race.get(i);
			String text = getText(url);
			System.out.println((race.size() - i) + " pages left to check...");
			
			int numWordsLength = text.split(" ").length;
			
			String[] allChars = text.split("");
			
			double counter = 0;
			for(int j = 0; j<allChars.length; j++) {
				if(!punctuations.contains(allChars[j])) {
					counter++;
				}
			}
			
			numWords[i][0] = numWordsLength;
			numWords[i][1] = counter/numWordsLength;
			
		}
		
		System.out.println("Finished with creation!");
		return numWords;
	}
	
	
	
	
}