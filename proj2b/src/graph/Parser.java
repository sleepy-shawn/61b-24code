package graph;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Parser {

	private HashMap<Integer, Integer> idMap;
	private WordGraph totalWords;
	private int wordNum;

	public Parser(String synsetName, String hyponymsName) {

		totalWords = new WordGraph();
		idMap = new HashMap<>();
		wordNum = 0;

		In synsetIn = new In(synsetName);
		In hyponymsIn = new In(hyponymsName);

		/* First read synsetIn */
		while (synsetIn.hasNextLine()) {
			String nextLine = synsetIn.readLine();
			String[] splitLine = nextLine.split(",");
			int id = Integer.parseInt(splitLine[0]);
			String word = splitLine[1];
			String[] words = word.split("");
			ArrayList<String> wordList = new ArrayList<>(Arrays.asList(words));
			idMap.put(id, wordNum);
			totalWords.addNode(wordList);
			wordNum += 1;
		}

		/* Second read hyponymsIn */
		while (hyponymsIn.hasNextLine()) {
			String nextLine = hyponymsIn.readLine();
			String[] splitLine = nextLine.split(",");
			int sourceID = Integer.parseInt(splitLine[0]);
			int sourceNum = idMap.get(sourceID);
			for (int i = 1; i< splitLine.length; i += 1) {
				int hyponymsID = Integer.parseInt(splitLine[i]);
				int hypNum = idMap.get(hyponymsID);
				totalWords.addEdge(sourceNum, hypNum);
			}
		}
	}

	public ArrayList<String> hyponysList (String word) {
		return totalWords.findHyponyms(word);
	}

}
