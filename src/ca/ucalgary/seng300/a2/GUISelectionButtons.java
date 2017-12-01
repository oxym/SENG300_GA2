package ca.ucalgary.seng300.a2;

import javax.swing.JLabel;

public class GUISelectionButtons extends GUIPanel {

	private static final long serialVersionUID = 2601893967280917982L;

	private JLabel title;

	@Override
	void init() {
		setBackground(COLOR_BACKGROUND);
		title = new JLabel("Selection Buttons Here");
		setOpaque(false);

		add(title);
		setVisible(true);
	}

}
