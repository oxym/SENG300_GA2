package ca.ucalgary.seng300.a2.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class GUIDisplayMain extends GUIPanel implements GuiDisplayInterface {

	private JLabel displayOutline;
	private JTextField display;
	private JLabel indicatorOutOfOrder;
	private JLabel labelOutOfOrder;
	private JLabel indicatorExactChange;
	private JLabel labelExactChange;


	@Override
	void init() {
		GridBagConstraints constraints = new GridBagConstraints();
		GridBagLayout gridbag = new GridBagLayout();

		setLayout(gridbag);
		setBackground(COLOR_BACKGROUND);

		//initialize components
		ImageIcon display_background = new ImageIcon("images/display.png");
		displayOutline = new JLabel(display_background);
		ImageIcon imageOutOfOrder = new ImageIcon("images/indicator_off.png");
		indicatorOutOfOrder = new JLabel(imageOutOfOrder);
		ImageIcon imageExactChange = new ImageIcon("images/indicator_off.png");
		indicatorExactChange = new JLabel(imageExactChange);
		labelOutOfOrder = new JLabel("Out Of Order");
		labelExactChange = new JLabel("ExactChange");

		display = new JTextField("Display");
		//TODO: increase border width, etc, system font
		//display.setFont(new Font("MS Gothic", Font.PLAIN, 18));
		display.setFont(new Font("Monospaced", Font.PLAIN, 18));
		display.setBackground(COLOR_BLACK);
		display.setForeground(COLOR_DISPLAYTEXT);
		display.setColumns(60);
		display.setEditable(false);


		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 7);

		display.setBorder(border);


		//add components and layout panel
	    constraints.anchor = GridBagConstraints.FIRST_LINE_START;
	    constraints.fill = GridBagConstraints.BOTH;
	    constraints.gridx = 0;
	    constraints.gridy = 0;
	    constraints.weightx = 0.33;
	    constraints.weighty = 0.5;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.insets = new Insets(5, 5, 10, 5);
		add(display, constraints);

		//indicator lights
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		constraints.gridwidth = 2;
		constraints.weighty = 0.0;

		//TODO: center indicator lights
		constraints.gridx = 0;
		constraints.gridy++;
		add(indicatorExactChange, constraints);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridx++;
		add(indicatorOutOfOrder, constraints);

		//indicator labels
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridy++;
		constraints.gridx = 0;
		add(labelExactChange, constraints);
		constraints.gridheight = GridBagConstraints.REMAINDER;
		constraints.gridx++;
		add(labelOutOfOrder, constraints);


		setVisible(true);
	}

	//TODO methods to update display and indicator lights

	/**
	 * Displays the message in the display field
	 * @param msg The message to display
	 */
	public void updateMessage(String message) {
		display.setText(message);
		update();
	}

	/**
	 * Turns off the Out of Order indicator
	 */
	public void indicatorOutOfOrderOff() {

	}

	/**
	 * Turns on the Out of Order indicator
	 */
	public void indicatorOutOfOrderOn() {

	}

	/**
	 * Turns off the Exact Change indicator
	 */
	public void indicatorExactChangerOff() {

	}

	/**
	 * Turns on the Exact Change indicator
	 */
	public void indicatorExactChangeOn() {

	}
}
