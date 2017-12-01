package ca.ucalgary.seng300.a2;

import javax.swing.JButton;

public class GUIConfigurationKeypad extends GUIPanel {
	
	private VendingManager mgr;
	
	public GUIConfigurationKeypad(VendingManager mgr) {
		this.mgr = mgr;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1803496343446827611L;
	private JButton[] jButtons;
//	private JButton[] numberButtons;
	private String[] buttonLabels = { "q", "w", "e", "r", "t", "y", "u", "i",
										"o", "p", "a", "s",
										"d", "f", "g", "h", "j", "k", "l", "z",
										"x", "c", "v", "b" ,"n", "m",
										"Shift", "Enter", "0", "1","2", "3", "4", 
										"5", "6", "7", "8", "9"};


	@Override
	void init() {
		//setLayout(grid);
		jButtons = new JButton[buttonLabels.length];

		for(int i = 0; i < buttonLabels.length; i++) {
			jButtons[i] = new JButton(buttonLabels[i]);
			add(jButtons[i]);
			jButtons[i].addActionListener(new GUIConfigurationListener(mgr));
		}
		
//		for(int i = 0; i < numberLabels.length; i++) {
//			numberButtons[i] = new JButton(numberLabels[i]);
//			add(numberButtons[i]);
//			letterButtons[i].addActionListener(new GUIConfigurationListener());
//		}

		setVisible(true);

	}


}
