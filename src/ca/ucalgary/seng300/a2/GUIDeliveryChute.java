package ca.ucalgary.seng300.a2;

import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * This panel simulates the Delivery Chute users may click to remove the item
 *
 */
public class GUIDeliveryChute extends GUIPanel implements GuiInterfaceDeliveryChute {

	private static final long serialVersionUID = 4619366058643743527L;

	private int itemQty;

	// components in the JPanel
	private JLabel chute;
	private JButton lock;
	private GUIConfigurationListener listener;
	GUIConfigurationMain configPanel;

	/**
	 * Default Constructor
	 */
	public GUIDeliveryChute() {
		itemQty = 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.ucalgary.seng300.a2.gui.GUIPanel#init()
	 */
	@Override
	void init() {

		// set look of panel
		setBackground(COLOR_BACKGROUND);
		GridBagConstraints constraints = new GridBagConstraints();
		GridBagLayout gridbag = new GridBagLayout();

		setLayout(gridbag);

		// instantiate components
		ImageIcon chuteIcon = new ImageIcon("images/dispenser_empty.png");
		chute = new JLabel(chuteIcon);
		MouseListener mouseListener = new MouseListener();
		chute.addMouseListener(mouseListener);

		lock = new JButton ("unlock");
		lock.addActionListener(new LockListener());
		lock = new JButton("unlock");
		lock.setPreferredSize(new Dimension(80,30));
		lock.addActionListener(new LockListener());

		// add components to the panel and set visibility

		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 0.33;
		constraints.weighty = 0.5;
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		constraints.insets = new Insets(0,0,50,0);
		add(chute, constraints);

		constraints.weightx = 0.05;
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridx++;
		add(lock, constraints);
		setVisible(true);
	}

	/**
	 * Adds an item to the delivery chute
	 */
	public void addItem() {
		ImageIcon chuteIcon;

		// update quantity of items in the chute
		itemQty++;

		// select correct image
		if (itemQty > 5) {
			chuteIcon = new ImageIcon("images/dispenser_over5.png");
		} else {
			String path = "images/dispenser_" + itemQty + ".png";
			chuteIcon = new ImageIcon(path);
		}

		// update the display
		chute.setIcon(chuteIcon);
		update();
	}

	/**
	 * Removes an item from the delivery chute
	 */
	public void removeItems() {

		// update quantity of items in the chute
		if (itemQty > 0) {
			itemQty = 0;

			ImageIcon chuteIcon;
			// select correct image
			if (itemQty > 5) {
				chuteIcon = new ImageIcon("images/dispenser_over5.png");
			} else if (itemQty == 0) {
				chuteIcon = new ImageIcon("images/dispenser_empty.png");
			} else {
				String path = "images/dispenser_" + itemQty + ".png";
				chuteIcon = new ImageIcon(path);
			}

			// update image
			chute.setIcon(chuteIcon);
			update();
		}
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
			GUIMain.getVM().getDeliveryChute().removeItems();
		}
	}

	/**
	 * Listener for lock/unlock button events
	 *
	 */
	private class LockListener implements ActionListener{

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event) {

			String command = event.getActionCommand();

			switch (command) {

			case "lock":
				lock.setText("unlock");
				GUIMain.getVendingManager().disableSafety();
				configPanel.dispose();
				break;

			case "unlock":
				lock.setText("lock");
				GUIMain.getVendingManager().enableSafety();
				configPanel = new GUIConfigurationMain();
				configPanel.init();
				break;
			}
		}
	}

}
