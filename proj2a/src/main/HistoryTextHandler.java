package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
	NGramMap wordMap;

	public HistoryTextHandler(NGramMap map) {
		wordMap = map;
	}

	@Override
	public String handle(NgordnetQuery q) {
		List<String> words = q.words();
		int startYear = q.startYear();
		int endYear = q.endYear();

		String response = "";
		for (String word : words) {
			response += word + ": ";
			response += wordMap.weightHistory(word, startYear, endYear).toString() + "\n";
		}
		return response;
	}
}