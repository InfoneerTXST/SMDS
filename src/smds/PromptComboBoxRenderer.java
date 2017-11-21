package smds;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

class PromptComboBoxRenderer extends BasicComboBoxRenderer
{
	private String prompt;

	public PromptComboBoxRenderer(String prompt) {
		this.prompt = prompt;
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value == null)
			setText(prompt);
		return this;
	}
}