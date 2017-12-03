package ca.ucalgary.seng300.a2;

import javax.swing.*;
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
		
		// Number of different types of pops in Vending Machine
		int popTypes = GUIMain.getVendingManager().getNumberOfSelectionButtons();
		btns = new JButton[popTypes];
		
		// Populate btns array with JButtons
		for (int i = 0; i < btns.length; ++i) {
			String imagePath = "images/select_button_" + GUIMain.getVendingManager().getProductName(i) + ".png";
			btns[i] = new JButton("", new ImageIcon(imagePath));
			btns[i].addActionListener(new SelectionButtonListener(i));
		}

		int y = 0;
		int x = 0;
		int counter = 0;
		
		// Place JButtons on buttonPanel
		for(int i=0; i < btns.length; i++){
			
		    gbc.gridx = x;
		    gbc.gridy = y;
		    buttonPanel.add(btns[i], gbc);
		    
		    if (counter >= popTypes / 3) {
		    		y += 1;
		    		x = 0;
		    		counter = 0;
		    } else {
			    counter += 1;
			    x += 1;
		    }
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
