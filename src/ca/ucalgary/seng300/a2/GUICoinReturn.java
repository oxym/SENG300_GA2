package ca.ucalgary.seng300.a2;

import javax.swing.JLabel;

public class GUICoinReturn extends GUIPanel {

	private static final long serialVersionUID = -375736403542638963L;

	private JLabel title;

	@Override
	void init() {
		setBackground(COLOR_BACKGROUND);
		title = new JLabel("Coin Return Here");

		add(title);
		setVisible(true);


	}
}
