package smds;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Popup;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;

public class PopupUI extends BasicPopupMenuUI {

	public static ComponentUI createUI(JComponent c){
		return new PopupUI();
	}

	public Popup getPopup(JPopupMenu popup, int x, int y) {
		Popup p = super.getPopup(popup, x,y);
		JPanel panel = (JPanel)popup.getParent();
		if(panel.getSize().equals(new Dimension(67, 47)))
			panel.setVisible(false);
		else
			panel.setVisible(true);
        return p;
	}
	
}
