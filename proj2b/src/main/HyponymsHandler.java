package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import graph.WordGraph;

import java.util.List;


public class HyponymsHandler extends NgordnetQueryHandler {
	WordGraph wg;

	public HyponymsHandler(WordGraph wg) {
		this.wg = wg;
	}

	@Override
	public String handle(NgordnetQuery q) {
		List<String> words = q.words();

		String response = "";
		for (String word : words) {
			response += word + ": ";
		}
		return response;
	}
