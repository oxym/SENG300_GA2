package ca.ucalgary.seng300.a2;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.lsmr.vending.Coin;

/*
 * This panel simulates the coin return users click to get change back
 */
public class GUICoinReturn extends GUIPanel {

	private static final long serialVersionUID = -375736403542638963L;

	//Component in the JPanel
	private JLabel coinReturn;

	private int coinQty;

	/*
	 * Default Constructor for coin return
	 */
	public GUICoinReturn() {
		coinQty = 0;
	}

	/*
	 * (non-Javadoc)
	 * @see ca.ucalgary.seng300.a2.gui.GUIPanel#init()
	 */
	@Override
	void init() {
		setBackground(COLOR_BACKGROUND);
		GridBagConstraints constraints = new GridBagConstraints();
		GridBagLayout gridbag = new GridBagLayout();

		setLayout(gridbag);
		setBackground(COLOR_TRANSPARENT);
		setOpaque(false);

		ImageIcon returnIcon = new ImageIcon("images/coin_return_empty.png");
		coinReturn = new JLabel(returnIcon);

		//Add components to the panel and make visible
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 0.33;
		constraints.weighty = 0.5;
		constraints.gridwidth = GridBagConstraints.RELATIVE;

		add(coinReturn, constraints);

		MouseListener mouselistener = new MouseListener();
		coinReturn.addMouseListener(mouselistener);

		setVisible(true);


	}

	/*
	 * Adds coins to coin return
	 */
	public void addCoin() {
		ImageIcon coinIcon;

		//Update coins in the coin return
		coinQty++;

		//Check is the VendingMachine is locked
		if(GUIMain.getVendingManager().getLock().isLocked()) {
			//Select proper coin return image
			if( coinQty == 0 )
				coinIcon = new ImageIcon("images/coin_return_empty.png");
			else if( coinQty >= 1 && coinQty < 200 ) {
				coinIcon = new ImageIcon("images/coin_return_full.png");
				GUIMain.getVendingManager().getCoinReturn().disable();
			}
			else {
				coinIcon = new ImageIcon("images/coin_return_full.png");
				GUIMain.getVendingManager().getCoinReturn().disable();
			}

			//Update image
			coinReturn.setIcon(coinIcon);
			update();

		}
		else
			//If unlocked disable coin return
			GUIMain.getVendingManager().getCoinReturn().disable();

	}

	/*
	 * Removes coins from coin return
	 */
	public void removeCoin(List<Coin> result) {

		//Update quantity of coins in the return

		if(GUIMain.getVendingManager().getLock().isLocked()) {
			//Update quantity of coins in the return
			if(coinQty > 0) {
				coinQty = 0;

				ImageIcon coinIcon;

				//Select proper coin return image
				if(coinQty == 0) {
					coinIcon = new ImageIcon("images/coin_return_empty.png");
					if(GUIMain.getVendingManager().getCoinReturn().hasSpace())
						GUIMain.getVendingManager().getCoinReturn().enable();

				}
				else
					coinIcon = new ImageIcon("images/coin_return_full.png");

				//Update image
				coinReturn.setIcon(coinIcon);

				//Display change returned window
				JOptionPane.showMessageDialog(null,
					    //("Change returned is: " + coinQty),
						("Coin Return Emptied"),
					    "Change Returned",
					    JOptionPane.PLAIN_MESSAGE);

				update();
			}
		}
		else
			//If unlocked disable coin return
			GUIMain.getVendingManager().getCoinReturn().disable();
	}

	/**
	 * Mouse listener to handle clicks for coin return
	 *
	 */
	class MouseListener extends MouseAdapter{
	    /* (non-Javadoc)
	     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	     */
	    @Override
	    public void mousePressed(MouseEvent e)
	    {
	    	//Remove coins from coin return
	    	GUIMain.getVendingManager().guiRemoveCoinFromReturn();

	    }
	}
}
