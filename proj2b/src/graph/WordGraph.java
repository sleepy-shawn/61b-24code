package graph;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public class WordGraph {

	ArrayList<ArrayList<Integer>> adjacentList;
	HashMap<ArrayList<String>, Integer> wordMap;
	int verticesNum;
	int edgesNum;

	/* Constructor, create a graph with V vertices. */
	public WordGraph() {
		adjacentList = new ArrayList<ArrayList<Integer>>();
		verticesNum = 0;
		edgesNum = 0;
	}

	/* Add an edge v-w, remember graph is directed*/
	public void addEdge(int v, int w) {
		adjacentList.get(v).add(w);
		edgesNum += 1;
	}

	/* Vertices adjacent to v */
	Iterable<Integer> adj(int v) {
		return adjacentList.get(v);
	}

	/* Number of vertices */
	int V() {
		return verticesNum;
	}

	/* Number of edges */
	int E() {
		return edgesNum;
	}

	/* Create node */
	public void addNode(ArrayList<String> words) {
		adjacentList.add(new ArrayList<Integer>());
		wordMap.put(words, verticesNum);
		verticesNum += 1;
	}

	/* Get nodes which contain the aim word */
	public ArrayList<Integer> getWordLists(String aimWord) {
		ArrayList<Integer> wordLists = new ArrayList<>();
		for (ArrayList<String> s : wordMap.keySet()) {
			if (s.contains(aimWord)) {
				wordLists.add(wordMap.get(s));
			}
		}
		return wordLists;
	}

}
