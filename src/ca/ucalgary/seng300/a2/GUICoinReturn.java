package ca.ucalgary.seng300.a2;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * This panel simulates the coin return users click to get change back
 */
public class GUICoinReturn extends GUIPanel {

	private static final long serialVersionUID = -375736403542638963L;

	private JLabel title;
	
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
		title = new JLabel("Coin Return Here");
		GridBagConstraints constraints = new GridBagConstraints();
		GridBagLayout gridbag = new GridBagLayout();
		
		setLayout(gridbag);
		
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
		
		add(title);
		setVisible(true);


	}
	
	/*
	 * Adds coins to coin return
	 */
	public void addCoin() {
		ImageIcon coinIcon;
		
		//Update coins in the coin return
		coinQty++;
		
		//Select proper coin return image
		if( coinQty == 0 ) 
			coinIcon = new ImageIcon("images/coin_return_empty.png");
		else if( coinQty >= 1 && coinQty < 200 ) {
			coinIcon = new ImageIcon("images/coin_return_full.png");
			//GUIMain.getVendingManager().enableSafety();
		}
		else {
			coinIcon = new ImageIcon("images/coin_return_full.png");
			GUIMain.getVendingManager().enableSafety(); //Enable safety until coins are removed?
		}
		
		//Update image
		coinReturn.setIcon(coinIcon);
		update();
	}
	
	/*
	 * Removes coins from coin return
	 */
	public void removeCoin() {
		
		//Update quantity of coins in the return
		if(coinQty > 0) {
			coinQty = 0;
		
			ImageIcon coinIcon;
			
			if(coinQty == 0)
				coinIcon = new ImageIcon("images/coin_return_empty.png");
			else 
				coinIcon = new ImageIcon("images/coin_return_full.png");
		
			coinReturn.setIcon(coinIcon);
			update();
		}
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
	    	GUIMain.getVendingManager().guiRemoveCoinFromReturn();
	    	GUIMain.getVM().getCoinReturn().unload();
	    	
	    	JFrame changeFrame = new JFrame("Change Returned");
	    	changeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    	changeFrame.setSize(300, 200);
	    	JLabel changeLabel = new JLabel("Change returned is: " + coinQty);
	    	GUIMain.getVM();
	    	setVisible(true);
	    	
	    	
	    	
	    }
	}
}
