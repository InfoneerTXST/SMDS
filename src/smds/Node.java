package smds;

import java.util.ArrayList;
import com.fasterxml.jackson.databind.JsonNode;

public class Node implements Comparable {
	
	private String categoryID;
	private String id;
	private String pref_label;
	private ArrayList<String> alt_labels;
	private ArrayList<JsonNodeWrapper> problems; // symptoms and causes
	private ArrayList<JsonNodeWrapper> treatments;
	
	public Node(String categoryID, String id, String pref_label, ArrayList<String> alt_labels, ArrayList<JsonNodeWrapper> problems, ArrayList<JsonNodeWrapper> treatments) {
		this.categoryID = categoryID;
		this.id = id;
		this.pref_label = pref_label;
		this.alt_labels = new ArrayList<String>();
		for(int x = 0; x < alt_labels.size(); x++)
			this.alt_labels.add(alt_labels.get(x));
		this.problems = new ArrayList<JsonNodeWrapper>();
		for(int x = 0; x < problems.size(); x++) {
			boolean exists = false;
			for(int y = 0; y < this.problems.size(); y++) {
				if(this.problems.get(y).getJsonNode().toString().equals(problems.get(x).getJsonNode().toString())) {
					exists = true;
					y = this.problems.size();
				}
			}
			if(!exists)
				this.problems.add(problems.get(x));
		}
		this.treatments = new ArrayList<JsonNodeWrapper>();
		for(int x = 0; x < treatments.size(); x++) {
			boolean exists = false;
			for(int y = 0; y < this.treatments.size(); y++) {
				if(this.treatments.get(y).getJsonNode().toString().equals(treatments.get(x).getJsonNode().toString())) {
					exists = true;
					y = this.treatments.size();
				}
			}
			if(!exists)
				this.treatments.add(treatments.get(x));
		}
	}
	
	public String getPrefLabel() {
		return pref_label;
	}
	
	public ArrayList<String> getAltLabels() {
		return alt_labels;
	}
	
	public ArrayList<JsonNodeWrapper> getProblems() {
		return problems;
	}
	
	public ArrayList<JsonNodeWrapper> getTreatments() {
		return treatments;
	}
	
	public String toString() {
		return pref_label;
	}
	
	public int compareTo(Object a) {
		return(this.toString().toLowerCase().compareTo(a.toString().toLowerCase()));
	}
}
