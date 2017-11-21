package smds;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonNodeWrapper implements Comparable {

	private JsonNode j;
	
	public JsonNodeWrapper(JsonNode j) {
		this.j = j;
	}
	
	public JsonNode getJsonNode() {
		return j;
	}
	
	public String toString() {
		return j.get("http://www.w3.org/2004/02/skos/core#prefLabel").get(0).get("value").asText();
	}
	
	public int compareTo(Object a) {
		return(this.toString().toLowerCase().compareTo(a.toString().toLowerCase()));
	}
}
