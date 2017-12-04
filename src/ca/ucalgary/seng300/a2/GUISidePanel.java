package ca.ucalgary.seng300.a2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * This panel holds all of the panels that are on the right hand side of the vending machine
 * display, coinslot, cardslot, and coinreturns.
 *
 */
public class GUISidePanel extends GUIPanel {

	private static final long serialVersionUID = -7159132536080286074L;

	//Components in this JPanel
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
		GridBagConstraints constraints = new GridBagConstraints();
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);
		setOpaque(false);
		setBackground(COLOR_TRANSPARENT_SEMI);

		Border border = BorderFactory.createMatteBorder(4, 4, 1, 1, COLOR_DARKGRAY);
		this.setBorder(border);

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
		constraints.weighty = 0.33;
		constraints.gridy++;
		add(coinSlotPanel, constraints);
//		constraints.weighty = 0.0;
//		constraints.gridy++;
//		add(cardSlotPanel, constraints);
		constraints.weighty = 1.0;
		constraints.gridy++;
		add(coinReturnPanel, constraints);

		setVisible(true);
	}

	protected void paintComponent(Graphics g)
    {
        g.setColor( getBackground() );
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

	/**
	 * Returns the main display panel
	 * @return the display panel object
	 */
	public GUIDisplayMain getDisplayPanel() {
		return displayPanel;
	}
	/**
	 * Returns the main coin return panel
	 * @return the coin return panel object
	 */
	public GUICoinReturn getCoinReturnPanel(){
		return coinReturnPanel;
	}

}
