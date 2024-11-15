package graph;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Parser {

	HashMap<Integer, ArrayList<String>> idMap;
	WordGraph totalWords;

	public Parser(String synsetName, String hyponymsName) {

		totalWords = new WordGraph();
		idMap = new HashMap<>();

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
			idMap.put(id, wordList);
			totalWords.addNode(wordList);
		}

		/* Second read hyponymsIn */
		while (hyponymsIn.hasNextLine()) {
			String nextLine = hyponymsIn.readLine();
			String[] splitLine = nextLine.split(",");
			int sourceID = Integer.parseInt(splitLine[0]);
			for (int i = 1; i< splitLine.length; i += 1) {
				int hyponymsID = Integer.parseInt(splitLine[i]);
				totalWords.addEdge(sourceID, hyponymsID);
			}

		}
	}

}
