package graph;

import edu.princeton.cs.algs4.In;

import java.util.*;

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
			String[] words = word.split(" ");
			ArrayList<String> wordList = new ArrayList<>(Arrays.asList(words));
			idMap.put(id, wordNum);
			totalWords.addNode(id, wordList);
			wordNum += 1;
		}

		/* Second read hyponymsIn */
		while (hyponymsIn.hasNextLine()) {
			String nextLine = hyponymsIn.readLine();
			String[] splitLine = nextLine.split(",");
			int sourceID = Integer.parseInt(splitLine[0]);
			int sourceNum = idMap.get(sourceID);
			for (int i = 1; i < splitLine.length; i += 1) {
				int hyponymsID = Integer.parseInt(splitLine[i]);
				int hypNum = idMap.get(hyponymsID);
				totalWords.addEdge(sourceNum, hypNum);
			}
		}
	}

	public ArrayList<String> hyponymsList(String word) {
		return totalWords.findHyponyms(word);
	}

	public ArrayList<String> sharedHyponyms(List<String> words) {
		/* If the user don't input words */
		if (words == null || words.isEmpty()) {
			return new ArrayList<>();
		}

		HashSet<String> sharedHyponyms = new HashSet<>(hyponymsList(words.getFirst()));
		for (int i = 1; i < words.size(); i += 1) {
			ArrayList<String> hyponyms = hyponymsList(words.get(i));
			sharedHyponyms.retainAll(hyponyms);
			if (sharedHyponyms.isEmpty()) {
				return new ArrayList<>();
			}
		}
		ArrayList<String> sharedList = new ArrayList<>(sharedHyponyms);
		Collections.sort(sharedList);
		return sharedList;
	}
}
