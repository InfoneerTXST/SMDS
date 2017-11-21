package smds;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ScrollUI extends BasicScrollBarUI {

	private Color tc;
	
	public ScrollUI(Color trackColor) {
		this.tc = trackColor;
	}
	
	private JButton createArrow() {
        JButton arrow = new JButton();
        arrow.setPreferredSize(new Dimension(0, 0));
        arrow.setMinimumSize(new Dimension(0, 0));
        arrow.setMaximumSize(new Dimension(0, 0));
        return arrow;
    }
	
	@Override 
    protected void configureScrollBarColors(){
        this.thumbColor = new Color(235, 235, 235);
        this.thumbDarkShadowColor = new Color(235, 235, 235);
        this.thumbHighlightColor = Color.LIGHT_GRAY;
        this.thumbLightShadowColor = Color.LIGHT_GRAY;
        this.trackColor = tc;
    }
	
	@Override
    protected JButton createDecreaseButton(int orientation) {
		return createArrow();
    }

    @Override    
    protected JButton createIncreaseButton(int orientation) {
    	return createArrow();
    }
	
}
