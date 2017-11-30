package ca.ucalgary.seng300.a2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.DisabledException;

public class GUICoinInsert extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 6019037857587046592L;
	public static final boolean DEBUG = true;
	public static final int X_SIZE = 480;
	public static final int Y_SIZE = 480;
	public static final String TITLE = "Insert Coins";

	private JButton[] buttons;
	private int[] coinValues;
	public CoinButtonListener listener;

	/**
	 * @param vm
	 * @param acceptedCoins
	 */
	public GUICoinInsert() {
		setSize(X_SIZE, Y_SIZE);
		setTitle(TITLE);
		coinValues = GUIMain.acceptedCoins;
		listener = new CoinButtonListener();
	}

	/**
	 *
	 * Initializes coin insert window
	 *
	 */
	public void init() {

		Icon icon;
		Container pane = getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 10);
		((JComponent) pane).setBorder(border);

		icon = new ImageIcon("images/coin.png");
		buttons = new JButton[coinValues.length];
		pane.add(Box.createVerticalGlue());


		for (int i = 0; i < coinValues.length; i++) {
			buttons[i] = new JButton(Integer.toString(coinValues[i]), icon);
			buttons[i].addActionListener(listener);
			buttons[i].setAlignmentX(CENTER_ALIGNMENT);
			buttons[i].setMaximumSize(new Dimension(300,50));
			pane.add(buttons[i]);
			pane.add(Box.createVerticalGlue());
		}

		setVisible(true);
	}

	/**
	 * Listener for button events
	 *
	 */
	public class CoinButtonListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event) {

			String command = event.getActionCommand();
			int coinVal = Integer.parseInt(command);
			Coin coin = new Coin(coinVal);
			try {
				GUIMain.getVM().getCoinSlot().addCoin(coin);
			} catch (DisabledException e) {
				System.out.println("System is disabled");
			}
		}
	}

}
