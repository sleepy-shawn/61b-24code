package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import graph.Parser;

import java.util.List;


public class HyponymsHandler extends NgordnetQueryHandler {
	Parser wordParser;

	public HyponymsHandler(Parser parser) {
		this.wordParser = parser;
	}

	@Override
	public String handle(NgordnetQuery q) {
		List<String> words = q.words();
		return wordParser.sharedHyponyms(words).toString();
	}
}
