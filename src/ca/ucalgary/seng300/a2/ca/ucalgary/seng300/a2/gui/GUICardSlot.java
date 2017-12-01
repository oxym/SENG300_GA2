package ca.ucalgary.seng300.a2.gui;

import javax.swing.JLabel;

public class GUICardSlot extends GUIPanel {

	private JLabel title;

	@Override
	void init() {
		setBackground(COLOR_BACKGROUND);
		title = new JLabel("CardSlot Here");

		add(title);
		setVisible(true);


	}

}
