package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import graph.Parser;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;


public class HyponymsHandler extends NgordnetQueryHandler {
	Parser wordParser;
	NGramMap ng;
	int startYear;
	int endYear;
	int k;

	public HyponymsHandler(Parser parser, NGramMap ngramMap) {
		wordParser = parser;
		ng = ngramMap;
	}

	@Override
	public String handle(NgordnetQuery q) {
		List<String> words = q.words();
		startYear = q.startYear();
		endYear = q.endYear();
		k = q.k();
		/* Map for saving the word occurrence, priority queue for selecting the top words,
		ArrayList for getting the all hyponyms
		 */
		if (k == 0) {
			ArrayList<String> allHyponyms = wordParser.sharedHyponyms(words);
			Collections.sort(allHyponyms);
			return allHyponyms.toString();
		}
		HashMap<String, Double> wordsCount = new HashMap<>();
		ArrayList<String> allHyponyms = wordParser.sharedHyponyms(words);

		for (String word : allHyponyms) {
			wordsCount.put(word, ng.sumHistory(word, startYear, endYear));
		}

		PriorityQueue<String> wordQueue = new PriorityQueue<>(Comparator.comparingDouble(wordsCount::get).reversed());
		wordQueue.addAll(allHyponyms);

		ArrayList<String> finalList = new ArrayList<>();
		for (int i = 0; i < k; i += 1) {
			if (wordQueue.isEmpty()) {
				break;
			}
			finalList.add(wordQueue.poll());
		}
		Collections.sort(finalList);
		return finalList.toString();
	}
}
