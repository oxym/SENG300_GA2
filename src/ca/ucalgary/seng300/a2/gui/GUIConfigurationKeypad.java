package ca.ucalgary.seng300.a2.gui;

import javax.swing.JButton;

public class GUIConfigurationKeypad extends GUIPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1803496343446827611L;
	private JButton[] buttons;
	private String[] buttonLabels = { "0", "1", "2", "3", "4", "5", "6", "7",
										"8", "9", "Shift", "Enter",
										"a", "b", "c", "d", "e", "f", "g", "h",
										"i", "j", "k", "l" ,"m", "n", "o", "p",
										"q", "r", "s", "t", "u", "v", "w", "x",
										"y", "z"};


	@Override
	void init() {
		//setLayout(grid);
		buttons = new JButton[buttonLabels.length];

		for(int i = 0; i < buttonLabels.length; i++) {
			buttons[i] = new JButton(buttonLabels[i]);
			add(buttons[i]);
		}

		setVisible(true);

	}

}
