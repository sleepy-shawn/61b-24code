package ngrams;

import edu.princeton.cs.algs4.In;

import java.sql.Time;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    public HashMap<String, TimeSeries> wordMap;
    public TimeSeries totalData;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordMap = new HashMap<String, TimeSeries>();
        totalData = new TimeSeries();

        In wordIn = new In(wordsFilename);
        In totalIn = new In(countsFilename);

        /* First read the words file to build wordMap */
        while (wordIn.hasNextLine()) {
            String nextLine = wordIn.readLine();
            String[] splitLine = nextLine.split("\t");
            if (!wordMap.containsKey(splitLine[0])) {
                TimeSeries wordSeries = new TimeSeries();
                wordMap.put(splitLine[0], wordSeries);
                wordSeries.put(Integer.parseInt(splitLine[1]), Double.parseDouble(splitLine[2]));
            } else {
                wordMap.get(splitLine[0]).put(Integer.parseInt(splitLine[1]), Double.parseDouble(splitLine[2]));
            }
        }

        /* Second read the total data file to build totalData */
        while (totalIn.hasNextLine()) {
            String nextLine = totalIn.readLine();
            String[] splitLine = nextLine.split(",");
            totalData.put(Integer.parseInt(splitLine[0]), Double.parseDouble(splitLine[1]));
        }

    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (wordMap.containsKey(word)) {
            startYear = Math.max(MIN_YEAR, startYear);
            endYear = Math.min(MAX_YEAR, endYear);
            TimeSeries wordSeries = wordMap.get(word);
            if (yearContain(wordSeries, startYear) || yearContain(wordSeries, endYear)) {
                return new TimeSeries(wordSeries, startYear, endYear);
            }
        }
        return new TimeSeries();
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (wordMap.containsKey(word)) {
            TimeSeries copy = new TimeSeries();
            copy.putAll(wordMap.get(word));
            return copy;
        } else {
            return new TimeSeries();
        }
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries copy = new TimeSeries();
        copy.putAll(totalData);
        return copy;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        startYear = Math.max(MIN_YEAR, startYear);
        endYear = Math.min(MAX_YEAR, endYear);
        TimeSeries division = countHistory(word, startYear, endYear);
        TimeSeries totalCopy = new TimeSeries(totalData, startYear, endYear);
        division = division.dividedBy(totalCopy);
        return division;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        if (wordMap.containsKey(word)) {
            TimeSeries division = countHistory(word);
            TimeSeries totalCopy = totalCountHistory();
            division = division.dividedBy(totalCopy);
            return division;
        }
        return new TimeSeries();
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries sumWeight = new TimeSeries();
        for (String word : words) {
            sumWeight = sumWeight.plus(weightHistory(word, startYear, endYear));
        }
        return sumWeight;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries sumWeight = new TimeSeries();
        for (String word : words) {
            sumWeight = sumWeight.plus(weightHistory(word));
        }
        return sumWeight;
    }

    private boolean yearContain(TimeSeries word, int year) {
        return word.years().contains(year);
    }

    private TimeSeries getData(String word) {
        return wordMap.get(word);
    }
}
