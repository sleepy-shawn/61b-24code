package main;

import browser.NgordnetQueryHandler;
import graph.Parser;
import ngrams.NGramMap;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymsHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {

        Parser parser = new Parser(synsetFile, hyponymFile);
	    NGramMap ng = new NGramMap(wordFile, countFile);
	    return new HyponymsHandler(parser, ng);
    }
}
