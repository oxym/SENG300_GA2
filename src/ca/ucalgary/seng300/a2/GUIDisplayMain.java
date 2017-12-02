package ca.ucalgary.seng300.a2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * This panel holds the feedback to the user including a display, and indicator
 * lights
 *
 */
public class GUIDisplayMain extends GUIPanel {

	private static final long serialVersionUID = 6739741022013889750L;
	private static final String MSG_INIT = "Initializing...";
	private static final int OUT_OF_ORDER = MachineConfiguration.OUT_OF_ORDER;
	private static final int EXACT_CHANGE = MachineConfiguration.EXACT_CHANGE;
	private JLabel displayOutline;
	private JTextField display;
	private JLabel[] label;
	private JLabel[] indicator;

	private String[] indicatorLabelText = MachineConfiguration.indicatorLabelText;

	GUIDisplayMain() {
	}

	@Override
	void init() {

		indicator = new JLabel[indicatorLabelText.length];
		label = new JLabel[indicatorLabelText.length];

		GridBagConstraints constraints = new GridBagConstraints();
		GridBagLayout gridbag = new GridBagLayout();

		setLayout(gridbag);
		setBackground(new Color(0,0,0, 0));
		//setOpaque(false);

		// initialize components
		ImageIcon indicatorOff = new ImageIcon("images/indicator_off.png");
		indicator[OUT_OF_ORDER] = new JLabel(indicatorOff);
		indicator[EXACT_CHANGE] = new JLabel(indicatorOff);
		label[OUT_OF_ORDER] = new JLabel(indicatorLabelText[OUT_OF_ORDER]);
		label[EXACT_CHANGE] = new JLabel(indicatorLabelText[EXACT_CHANGE]);

		//set look of display
		display = new JTextField(MSG_INIT);
		// display.setFont(new Font("MS Gothic", Font.PLAIN, 18));
		display.setFont(new Font("Monospaced", Font.PLAIN, 12));
		display.setBackground(COLOR_BLACK);
		display.setForeground(COLOR_DISPLAYTEXT);
		display.setColumns(40);
		display.setMinimumSize(new Dimension(200, 50));
		display.setEditable(false);
		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 7);
		display.setBorder(border);

		//set look of indicators
		label[OUT_OF_ORDER].setForeground(COLOR_WHITE);
		label[EXACT_CHANGE].setForeground(COLOR_WHITE);

		// add components and layout panel
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 0.33;
		constraints.weighty = 0.5;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.insets = new Insets(10, 0, 15, 0);
		add(display, constraints);

		// indicator lights
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		constraints.gridwidth = 2;
		constraints.weighty = 0.0;

		constraints.gridx = 0;
		constraints.gridy++;
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		add(indicator[EXACT_CHANGE], constraints);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridx++;
		add(indicator[OUT_OF_ORDER], constraints);

		// indicator labels
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridy++;
		constraints.gridx = 0;
		add(label[EXACT_CHANGE], constraints);
		constraints.gridheight = GridBagConstraints.REMAINDER;
		constraints.gridx++;
		add(label[OUT_OF_ORDER], constraints);

		setVisible(true);
	}

	/**
	 * Displays the message in the display field
	 *
	 * @param msg
	 *            The message to display
	 */
	public void updateMessage(String message) {
		display.setText(message);
		//update();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ca.ucalgary.seng300.a2.gui.GuiInterfaceIndicators#indicatorOn(int)
	 */
	public void indicatorSet(int index,boolean isLit) {
		ImageIcon indicatorIcon;
		if (isLit)
			indicatorIcon = new ImageIcon("images/indicator_on.png");
		else
			indicatorIcon = new ImageIcon("images/indicator_off.png");
		indicator[index].setIcon(indicatorIcon);
		//update();
	}

}
