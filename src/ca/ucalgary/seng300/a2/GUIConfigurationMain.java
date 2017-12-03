package ca.ucalgary.seng300.a2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

/**
 * The configuration window of a simulated vending machine
 *
 */
public class GUIConfigurationMain extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = -8393833057316799549L;
	private static final int X_SIZE = 510;
	private static final int Y_SIZE = 640;
	private static final String TITLE = "Configuration Panel";
	private static final int H_SPLIT = 25;

	private GUIConfigurationDisplay displayPanel;
	private GUIConfigurationKeypad  keypadPanel;
	private VendingManager mgr;

	/**
	 * @param mgr
	 *
	 */
	public GUIConfigurationMain(VendingManager mgr) {
		setSize(X_SIZE, Y_SIZE);
		setTitle(TITLE);
		this.mgr = mgr;
	}

	/**
	 * Initializes and displays configuration manager window.
	 */
	public void init() {
		setLayout(new BorderLayout());
		Container pane = getContentPane();

		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 10);
		((JComponent) pane).setBorder(border);

		Dimension panelSize = new Dimension((int) (X_SIZE * H_SPLIT), Y_SIZE);
		displayPanel = new GUIConfigurationDisplay(mgr);
		keypadPanel = new GUIConfigurationKeypad(mgr);

		//displayPanel.setPreferredSize(panelSize);
		//displayPanel.setMinimumSize(panelSize);

		displayPanel.init();
		keypadPanel.init();

		pane.add(displayPanel, BorderLayout.NORTH);
		pane.add(keypadPanel, BorderLayout.CENTER);

		setVisible(true);
		setResizable(false);
	}

	/**
	 * Returns the configuration display panel
	 * @return the display panel object
	 */
	public GUIConfigurationDisplay getDisplayPanel() {
		return displayPanel;
	}
}
