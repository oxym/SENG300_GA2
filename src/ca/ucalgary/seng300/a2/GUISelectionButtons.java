package ca.ucalgary.seng300.a2;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUISelectionButtons extends GUIPanel {

	private static final long serialVersionUID = 2601893967280917982L;

	private JPanel titlePanel;
	private JPanel buttonPanel;

	private JLabel title;

	private JButton[] btns;

	@Override
	void init() {

		setBackground(COLOR_BACKGROUND);
		title = new JLabel("Make your choice!");
		setOpaque(false);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6,6,6,6); // Adds padding to elements

		// Add title to titlePanel
		titlePanel = new JPanel();
		titlePanel.setLayout(new GridBagLayout());
		titlePanel.setBackground(COLOR_TRANSPARENT);
		gbc.gridx = 0;
		gbc.gridy = 0;
		titlePanel.add(title);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(titlePanel, gbc);

		// Create buttonPanel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setBackground(COLOR_TRANSPARENT);

		Border emptyBorder = BorderFactory.createEmptyBorder();


		// Number of different types of pops in Vending Machine
		int popTypes = GUIMain.getVendingManager().getNumberOfSelectionButtons();
		btns = new JButton[popTypes];

		// Populate btns array with JButtons
		for (int i = 0; i < btns.length; ++i) {
			String productName = GUIMain.getVendingManager().getProductName(i);
			String imagePath = "images/select_button_" + i + ".png";
			btns[i] = new JButton(productName, new ImageIcon(imagePath));
			btns[i].setFont(new Font("MS Gothic", Font.BOLD, 20));
			btns[i].setBackground(COLOR_TRANSPARENT);
			btns[i].setBorder(emptyBorder);

			btns[i].addActionListener(new SelectionButtonListener(i));
			btns[i].setVerticalTextPosition(SwingConstants.CENTER);
			btns[i].setHorizontalTextPosition(SwingConstants.CENTER);
		}

		// Place JButtons on buttonPanel
		for(int i=0; i < btns.length; i++){

			gbc.insets = new Insets(20,15,20,15);
		    gbc.gridx = i % 2;
		    gbc.gridy = Math.floorDiv(i,  2);
		    buttonPanel.add(btns[i], gbc);
		}

		gbc.gridx = 0;
		gbc.gridy = 1;
		add(buttonPanel, gbc);

		setVisible(true);
	}

	private class SelectionButtonListener implements ActionListener {

		private int btnID;

		public SelectionButtonListener(int btnID) {
			this.btnID = btnID;
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			GUIMain.getVM().getSelectionButton(this.btnID).press();
		}
	}
}
