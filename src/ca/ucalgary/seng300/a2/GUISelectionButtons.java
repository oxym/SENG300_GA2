package ca.ucalgary.seng300.a2;

import javax.swing.*;
import java.awt.*;

public class GUISelectionButtons extends GUIPanel {

	private static final long serialVersionUID = 2601893967280917982L;

	private JPanel titlePanel;
	private JPanel buttonPanel;

	private JLabel title;

	// TODO: remove
	private JButton pop1;
	private JButton pop2;
	private JButton pop3;
	private JButton pop4;

	@Override
	void init() {
		
		setBackground(COLOR_BACKGROUND);
		title = new JLabel("Selection Buttons Here");
		setOpaque(false);

		// TODO: add actionListeners for the selection buttons

		// New code

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6,6,6,6); // Adds padding to elements

		// Add title to titlePanel
		titlePanel = new JPanel();
		titlePanel.setLayout(new GridBagLayout());
		titlePanel.setBackground(COLOR_BACKGROUND);
		gbc.gridx = 0;
		gbc.gridy = 0;
		titlePanel.add(title);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(titlePanel, gbc);

		// Create buttonPanel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setBackground(COLOR_BACKGROUND);

		// TODO: replace with pops from VM
		pop1 = new JButton("POP 1");
		pop2 = new JButton("POP 2");
		pop3 = new JButton("POP 3");
		pop4 = new JButton("POP 4");

		JButton[] buttonArray = new JButton[]{pop1, pop2, pop3, pop4};
		for(int i=0; i < 3; i++){
		    gbc.gridx = i;
		    gbc.gridy = 0;
		    buttonPanel.add(buttonArray[i], gbc);
		}

		gbc.gridx = 0;
		gbc.gridy = 1;
		add(buttonPanel, gbc);

		// New code

		setVisible(true);
	}
}
