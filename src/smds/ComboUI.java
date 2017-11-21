package smds;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class ComboUI extends BasicComboBoxUI {
	
	private JComboBox comboBox;
	private boolean showArrow;
	
	public ComboUI(JComboBox comboBox, boolean showArrow) {
		this.comboBox = comboBox;
		this.showArrow = showArrow;
	}
	
	@Override
	protected JButton createArrowButton() {
		if(showArrow)
		{
			JButton button = new JButton();
			button.setText("\u25BE");
			button.setBorder(BorderFactory.createEmptyBorder());
			button.setUI(new BasicButtonUI());
			return button;
		}
		return null;
    }
	
	@Override
    protected ComboPopup createPopup() {
        BasicComboPopup bcp = new BasicComboPopup(comboBox) {
            @Override
            protected JScrollPane createScroller() {
                JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scroll.getVerticalScrollBar().setUI(new ScrollUI(Color.WHITE));
                return scroll;
            }
        };
        bcp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return bcp;
	}
	
}
