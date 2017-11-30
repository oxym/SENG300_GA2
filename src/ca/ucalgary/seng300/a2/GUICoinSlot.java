package ca.ucalgary.seng300.a2;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A panel displaying a coinslot
 *
 */
public class GUICoinSlot extends GUIPanel {

	private static final long serialVersionUID = -8646536275671533920L;

	// components in the JPanel
	private JLabel coinSlot;
	GUIConfigurationMain configPanel;

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.ucalgary.seng300.a2.gui.GUIPanel#init()
	 */
	@Override
	void init() {

		// set look of panel
		setBackground(COLOR_BACKGROUND);
		// GridBagConstraints constraints = new GridBagConstraints();
		// GridBagLayout gridbag = new GridBagLayout();

		// setLayout(gridbag);

		// instantiate components
		ImageIcon coinSlotIcon = new ImageIcon("images/coinslot.png");
		coinSlot = new JLabel(coinSlotIcon);
		MouseListener mouseListener = new MouseListener();
		coinSlot.addMouseListener(mouseListener);

		// add components to the panel and set visibility

		// constraints.anchor = GridBagConstraints.WEST;
		// constraints.fill = GridBagConstraints.HORIZONTAL;
		// constraints.gridx = 0;
		// constraints.gridy = 0;
		// constraints.weightx = 0.33;
		// constraints.weighty = 0.5;
		// constraints.gridwidth = GridBagConstraints.RELATIVE;
		// add(chute, constraints);
		add(coinSlot);

		setVisible(true);
	}

	/**
	 * Adds a coin to coinslot
	 */
	public void addCoin() {

		update();
	}

	/**
	 * Mouse listener to handle clicks for Coin Slot
	 *
	 */
	class MouseListener extends MouseAdapter {
		/*
		 * (non-Javadoc)
		 *
		 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			GUICoinInsert coinWindow = new GUICoinInsert();
			coinWindow.setLocation(720, 0);
			coinWindow.init();

		}
	}

}
