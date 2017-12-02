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
		//setBackground(COLOR_BACKGROUND);
		setOpaque(false);

		// instantiate components and set behaviour
		ImageIcon coinSlotIcon = new ImageIcon("images/coinslot.png");
		coinSlot = new JLabel(coinSlotIcon);
		MouseListener mouseListener = new MouseListener();
		coinSlot.addMouseListener(mouseListener);

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
