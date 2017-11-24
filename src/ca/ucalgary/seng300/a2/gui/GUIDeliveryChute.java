package ca.ucalgary.seng300.a2.gui;

import javax.swing.JLabel;

public class GUIDeliveryChute extends GUIPanel {

	private static final long serialVersionUID = 4619366058643743527L;

	private JLabel title;

	@Override
	void init() {
		setBackground(COLOR_BACKGROUND);
		title = new JLabel("Delivery Chute Here");

		add(title);
		setVisible(true);

	}

}
