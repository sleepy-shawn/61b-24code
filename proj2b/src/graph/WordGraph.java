package graph;


import java.util.*;

public class WordGraph {

	private HashMap<Integer, ArrayList<Integer>> adjacentList;
	private HashMap<Integer, ArrayList<String>> wordMap;
	private int verticesNum;

	/* Constructor, create a graph with V vertices. */
	public WordGraph() {
		adjacentList = new HashMap<>();
		wordMap = new HashMap<>();
		verticesNum = 0;
	}

	/* Add an edge v-w, remember graph is directed*/
	public void addEdge(int v, int w) {
		if (!adjacentList.containsKey(v)) {
			adjacentList.put(v, new ArrayList<>());
		}
		adjacentList.get(v).add(w);
	}

	/* Vertices adjacent to v */
	private ArrayList<Integer> adj(int v) {
		return adjacentList.get(v);
	}

	/* Number of vertices */
	private int V() {
		return verticesNum;
	}

	/* Create node */
	public void addNode(ArrayList<String> words) {
		wordMap.put(verticesNum, words);
		verticesNum += 1;
	}

	/* Get nodes which contain the aim word,
	* wordLists is an Integer List which contain the node index */
	private ArrayList<Integer> getWordLists(String aimWord) {
		ArrayList<Integer> wordLists = new ArrayList<>();
		for (int i : wordMap.keySet()) {
			if (wordMap.get(i).contains(aimWord)) {
				wordLists.add(i);
			}
		}
		return wordLists;
	}

	private HashSet<String> hyponyms(ArrayList<Integer> indexLists) {
		HashSet<String> allHyponyms = new HashSet<>();
		recursFind(allHyponyms, indexLists);
		return allHyponyms;
	}

	/* Helper methods for recursively find the all below hyponyms */
	private void recursFind(HashSet<String> results, ArrayList<Integer> indexLists) {
		if (indexLists == null) {
			return;
		}
		for (int i : indexLists) {
			results.addAll(wordMap.get(i));
			recursFind(results, adj(i));
		}
	}

	/* First find all nodes that contain the word, add the words in the nodes;
	then recursively find the adjacent nodes of these nodes until we fall off the graph.
	 */
	public ArrayList<String> findHyponyms(String word) {
		ArrayList<Integer> initList = getWordLists(word);
		HashSet<String> wordSet = hyponyms(initList);
		ArrayList<String> hpList = new ArrayList<>(wordSet);
		Collections.sort(hpList);
		return hpList;
	}

}
