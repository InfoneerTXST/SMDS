package smds;

public class CheckboxListItem {
	
	private JsonNodeWrapper jw;
	private boolean isSelected = true;

	public CheckboxListItem(JsonNodeWrapper jw) {
		this.jw = jw;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public JsonNodeWrapper getJsonNodeWrapper() {
		return jw;
	}

	public String toString() {
		return jw.toString();
	}
}
