package ca.ucalgary.seng300.a2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

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
	public static final int X_SIZE = 768;
	public static final int Y_SIZE = 1024;
	public static final double V_SPLIT = 0.35;
	public static final String TITLE = "Vending Machine";

	// The right panel holds the JPanels for the
	// display, coinslot, cardslot, and coinreturn
	private GUISidePanel sidePanel;
	private GUIPanel titlePanel;
	private GUIPanel selectionButtonPanel;
	private GUIPanel deliveryChutePanel;
	private GUIConfigurationDisplay configurationDisplay;

	protected static VendingMachine vm;
	protected static VendingManager mgr;
	protected static int[] acceptedCoins;

	/**
	 * @param vm
	 * @param acceptedCoins
	 */
	public GUIMain(VendingMachine vm, VendingManager mgr, int[] acceptedCoins) {
		GUIMain.vm = vm;
		GUIMain.mgr = mgr;
		this.acceptedCoins = acceptedCoins;
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

		setLayout(new BorderLayout());
		Container pane = getContentPane();

		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 10);
		((JComponent) pane).setBorder(border);

		BackgroundPanel bgPanel = new BackgroundPanel();
		bgPanel.setLayout(new BorderLayout());

		Dimension panelSize = new Dimension((int) (X_SIZE * V_SPLIT), Y_SIZE);
		sidePanel = new GUISidePanel();
		titlePanel = new GUITitle();
		selectionButtonPanel = new GUISelectionButtons();
		deliveryChutePanel = new GUIDeliveryChute(mgr);

		sidePanel.setPreferredSize(panelSize);
		sidePanel.setMinimumSize(panelSize);

		titlePanel.init();
		sidePanel.init();
		selectionButtonPanel.init();
		deliveryChutePanel.init();

		// for debugging to assist with layout of the panels
		//if (DEBUG)
		//	sidePanel.setBackground(Color.WHITE);
		pane.add(bgPanel);
		bgPanel.add(sidePanel, BorderLayout.EAST);
		bgPanel.add(titlePanel, BorderLayout.NORTH);
		bgPanel.add(deliveryChutePanel, BorderLayout.SOUTH);
		bgPanel.add(selectionButtonPanel, BorderLayout.CENTER);

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

	/**
	 * Returns the side panel
	 * @return the side panel object
	 */
	public GUISidePanel getSidePanel() {
		return sidePanel;
	}

	/**
	 * Returns the delivery chute panel
	 * @return the delivery chute panel object
	 */
	public GuiInterfaceDeliveryChute getDeliveryChutePanel() {
		return (GuiInterfaceDeliveryChute) deliveryChutePanel;
	}

	/**
	 * Returns the vending machine hardware that this gui is simulating
	 * @return vending machine object
	 */
	public static VendingMachine getVM() {
		return vm;
	}

	/**
	 * Returns the vending manager that this gui is connected to
	 * @return vending manager object
	 */
	public static VendingManager getVendingManager() {
		return mgr;
	}

	/**
	 * Returns the configuration display object
	 * @return the configuration display object
	 */
	public GUIConfigurationDisplay getConfigPanel() {
		return configurationDisplay;
	}


    /**
     * A panel that has a background image
     *
     */
    private class BackgroundPanel extends JPanel{

        private Image bgImage;

		protected void paintComponent(Graphics g) {

			try {
				bgImage = ImageIO.read(new File("images/bg.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

            super.paintComponent(g);

			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
