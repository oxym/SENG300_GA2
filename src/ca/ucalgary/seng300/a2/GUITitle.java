package ca.ucalgary.seng300.a2;

import java.awt.Font;

import javax.swing.JLabel;

/**
 * Displays the Name and Text at the top of the vending machine
 *
 */
public class GUITitle extends GUIPanel {

	private static final long serialVersionUID = -2845471123091962107L;

	//Objects in the panel
	private JLabel title;

	GUITitle(){

	}

	/* (non-Javadoc)
	 * @see ca.ucalgary.seng300.a2.gui.GUIPanel#display()
	 */
	@Override
	void init() {
		setOpaque(false);
		title = new JLabel("Not Just Any Vending Machine");
		title.setForeground(COLOR2);
		title.setFont(new Font("MS Gothic", Font.BOLD, 32));

		add(title);
		setVisible(true);
	}

}
