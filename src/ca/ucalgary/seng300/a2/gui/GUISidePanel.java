package ca.ucalgary.seng300.a2.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * This panel holds all of the panels that are on the right hand side of the vending machine
 * display, coinslot, cardslot, and coinreturns.
 *
 */
public class GUISidePanel extends GUIPanel {

	private static final long serialVersionUID = -7159132536080286074L;

	//components in this JPanel
	private GUIDisplayMain displayPanel;
	private GUICoinSlot coinSlotPanel;
	private GUICardSlot cardSlotPanel;
	private GUICoinReturn coinReturnPanel;

	/* (non-Javadoc)
	 * @see ca.ucalgary.seng300.a2.gui.GUIPanel#init()
	 */
	@Override
	void init() {

		//setup panel
		setBackground(COLOR_BACKGROUND);
		GridBagConstraints constraints = new GridBagConstraints();
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);

		displayPanel = new GUIDisplayMain();
		coinSlotPanel = new GUICoinSlot();
		cardSlotPanel = new GUICardSlot();
		coinReturnPanel = new GUICoinReturn();

		//subpanels
		displayPanel.init();
		coinSlotPanel.init();
		cardSlotPanel.init();
		coinReturnPanel.init();

		// layout side panel
		//constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		//constraints.gridwidth = GridBagConstraints.RELATIVE;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weighty = 0.0;
		add(displayPanel, constraints);
		constraints.gridy++;
		add(coinSlotPanel, constraints);
		constraints.weighty = 0.0;
		constraints.gridy++;
		add(cardSlotPanel, constraints);;
		constraints.weighty = 1.0;
		constraints.gridy++;
		add(coinReturnPanel, constraints);

		setVisible(true);
	}

}
