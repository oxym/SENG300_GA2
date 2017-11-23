package ca.ucalgary.seng300.a2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.lsmr.vending.hardware.VendingMachine;

/**
 * The main window, this class is responsible for setting up the main JFrame and
 * populating it with various JPanels, that correspond to the hardware
 * interface.
 *
 * The vending machine hardware must be passed as a constructor so interactions
 * with the machine are possible, i.e. button presses
 *
 */
public class GUIMain extends JFrame {

	private static final long serialVersionUID = 8968054066670899143L;

	public static final boolean DEBUG = true;
	public static final int X_SIZE = 1024;
	public static final int Y_SIZE = 768;
	public static final double V_SPLIT = 0.3;
	public static final String TITLE = "Vending Machine";

	// The right panel holds the JPanels for the
	// display, coinslot, cardslot, and coinreturn
	private GUIPanel sidePanel;
	private GUIPanel titlePanel;
	private GUIPanel selectionButtonPanel;
	private GUIPanel deliveryChutePanel;

	private VendingMachine vm;

	/**
	 * @param vm
	 */
	public GUIMain(VendingMachine vm) {
		this.vm = vm;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(X_SIZE, Y_SIZE);
		setTitle(TITLE);
	}

	/**
	 *
	 * Initializes main graphical window
	 *
	 */
	public void init() {
		Container pane = getContentPane();

		Dimension panelSize = new Dimension((int) (X_SIZE * V_SPLIT), Y_SIZE);
		sidePanel = new GUISidePanel();
		titlePanel = new GUITitle();
		selectionButtonPanel = new GUISelectionButtons();
		deliveryChutePanel = new GUIDeliveryChute();

		sidePanel.setPreferredSize(panelSize);

		titlePanel.init();
		sidePanel.init();
		selectionButtonPanel.init();
		deliveryChutePanel.init();

		// for debugging to assist with layout of the panels
		if (DEBUG)
			sidePanel.setBackground(Color.WHITE);

		pane.add(sidePanel, BorderLayout.EAST);
		pane.add(titlePanel, BorderLayout.NORTH);
		pane.add(deliveryChutePanel, BorderLayout.SOUTH);
		pane.add(selectionButtonPanel, BorderLayout.CENTER);

		setVisible(true);
	}

	/**
	 *
	 * updatePanel - repaints the panel
	 *
	 */
	public void updatePanel() {
		revalidate();
		repaint();
	}

}
