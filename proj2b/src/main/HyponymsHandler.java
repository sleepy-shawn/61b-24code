package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import graph.Parser;
import graph.WordGraph;

import java.util.List;


public class HyponymsHandler extends NgordnetQueryHandler {
	Parser wordParser;

	public HyponymsHandler(Parser parser) {
		this.wordParser = parser;
	}

	@Override
	public String handle(NgordnetQuery q) {
		List<String> words = q.words();
		String response = "";
		for (String word : words) {
			response += wordParser.hyponysList(word) + "\n";
		}
		return response;
	}
}
