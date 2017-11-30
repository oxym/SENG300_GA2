package ca.ucalgary.seng300.a2;

import javax.swing.JLabel;

public class GUICoinSlot extends GUIPanel {

	private static final long serialVersionUID = -8646536275671533920L;
	private JLabel title;

	@Override
	void init() {
		setBackground(COLOR_BACKGROUND);
		title = new JLabel("CoinSlot Here");

		add(title);
		setVisible(true);


	}

}
