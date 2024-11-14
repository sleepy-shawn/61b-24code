package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.List;


public class HistoryHandler extends NgordnetQueryHandler {

	NGramMap wordMap;

	public HistoryHandler(NGramMap ng) {
		wordMap = ng;
	}
	@Override
	public String handle(NgordnetQuery q) {
		List<String> words = q.words();
		int startYear = q.startYear();
		int endYear = q.endYear();
		ArrayList<TimeSeries> lts = new ArrayList<>();
		ArrayList<String> labels = new ArrayList<>();

		for (String word : words) {
			TimeSeries wordTimeSeries = wordMap.weightHistory(word, startYear, endYear);
			lts.add(wordTimeSeries);
			labels.add(word);
		}

		XYChart chart = Plotter.generateTimeSeriesChart(labels, lts);
		String encodedImage = Plotter.encodeChartAsString(chart);

		return encodedImage;
	}
}
