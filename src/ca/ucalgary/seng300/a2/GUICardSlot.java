package ca.ucalgary.seng300.a2;

import javax.swing.JLabel;

public class GUICardSlot extends GUIPanel {

	private static final long serialVersionUID = -3561070445764413963L;
	private JLabel title;

	@Override
	void init() {
		setOpaque(false);
		title = new JLabel("CardSlot Here");

		add(title);
		setVisible(true);

	}

}
