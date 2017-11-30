package ca.ucalgary.seng300.a2;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import ca.ucalgary.seng300.a2.GUIDeliveryChute.MouseListener;

public class GUICoinSlot extends GUIPanel {

	private static final long serialVersionUID = -8646536275671533920L;
	private int itemQty;

	// components in the JPanel
	private JLabel chute;
	private JButton lock;
	private GUIConfigurationListener listener;
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
		//GridBagConstraints constraints = new GridBagConstraints();
		//GridBagLayout gridbag = new GridBagLayout();

		//setLayout(gridbag);

		// instantiate components
		ImageIcon chuteIcon = new ImageIcon("images/coinslot.png");
		chute = new JLabel(chuteIcon);
		MouseListener mouseListener = new MouseListener();
		chute.addMouseListener(mouseListener);

		// add components to the panel and set visibility

//		constraints.anchor = GridBagConstraints.WEST;
//		constraints.fill = GridBagConstraints.HORIZONTAL;
//		constraints.gridx = 0;
//		constraints.gridy = 0;
//		constraints.weightx = 0.33;
//		constraints.weighty = 0.5;
//		constraints.gridwidth = GridBagConstraints.RELATIVE;
//		add(chute, constraints);
		add(chute);

		setVisible(true);
	}

	/**
	 * Adds a coin to coinslot
	 */
	public void addCoin() {

		update();
	}

	/**
	 * Mouse listener to handle clicks for delivery chute
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
			coinWindow.init();

		}
	}

}
