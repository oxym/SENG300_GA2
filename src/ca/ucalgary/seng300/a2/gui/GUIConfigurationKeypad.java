package ca.ucalgary.seng300.a2.gui;

import javax.swing.JButton;

public class GUIConfigurationKeypad extends GUIPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1803496343446827611L;
	private JButton[] letterButtons;
	private JButton[] numberButtons;
	private String[] letterLabels = { "q", "w", "e", "r", "t", "y", "u", "i",
										"o", "p", "a", "s",
										"d", "f", "g", "h", "j", "k", "l", "z",
										"x", "c", "v", "b" ,"n", "m",
										"Shift", "Enter"};
	private String[] numberLabels = {"0", "1","2", "3", "4", "5", "6", "7", "8", "9"};


	@Override
	void init() {
		//setLayout(grid);
		letterButtons = new JButton[letterLabels.length];
		numberButtons = new JButton[numberLabels.length];

		for(int i = 0; i < letterLabels.length; i++) {
			letterButtons[i] = new JButton(letterLabels[i]);
			add(letterButtons[i]);
		}
		
		for(int i = 0; i < numberLabels.length; i++) {
			numberButtons[i] = new JButton(numberLabels[i]);
			add(numberButtons[i]);
		}

		setVisible(true);

	}

}
