package smds;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

public class CustomCheckbox extends JCheckBox {
	
	public CustomCheckbox() {
		super();
		setIcon(new ImageIcon(getClass().getResource("/images/checkbox3.png")));
        setSelectedIcon(new ImageIcon(getClass().getResource("/images/checkbox4.png")));
	}
	
}
