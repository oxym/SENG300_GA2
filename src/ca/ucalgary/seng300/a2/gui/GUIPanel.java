package ca.ucalgary.seng300.a2.gui;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * The parent class for the JPanels
 * This holds color information, that is common on all of
 * the JPanels
 */
public abstract class GUIPanel extends JPanel {

	private static final long serialVersionUID = 3205991961181700449L;

	//Color information for the JPanels,
	//add new colors here
	public static final Color COLOR_BACKGROUND = new Color(224,0,224);
	public static final Color COLOR1 = new Color(230, 230, 193);
	public static final Color COLOR2 = new Color(212, 212, 144);
	public static final Color COLOR3 = new Color(193, 221, 230);
	public static final Color COLOR_FONT = new Color(255, 255, 255);
	public static final Color COLOR_BLACK = new Color(0,0,0);
	public static final Color COLOR_DARKGRAY = Color.DARK_GRAY;
	public static final Color COLOR_DISPLAYTEXT = new Color(0,222,255);


	/**
	 *
	 * update - repaints the panel after an event
	 *
	 *
	 */
	public void update() {
		revalidate();
		repaint();
	}

	/**
	 * Lays out and displays the components of the JPanel
	 */
	abstract void init();

}
