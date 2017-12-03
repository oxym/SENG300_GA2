package ca.ucalgary.seng300.a2;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class GUIConfigurationDisplay extends GUIPanel {

	private static final long serialVersionUID = 1541606205308730945L;
	private JTextField display;
	private VendingManager mgr;



	public GUIConfigurationDisplay(VendingManager manager){
		mgr = manager;
	}
	
	@Override
	void init() {		
		String dispMsg = mgr.getConfigPanelHandler().getDisplayMessage();
		display = new JTextField(dispMsg);


		/**
		 * create display for config panel
		 */
		// display.setFont(new Font("MS Gothic", Font.PLAIN, 18));
		display.setFont(new Font("Monospaced", Font.PLAIN, 18));
		display.setBackground(COLOR_BLACK);
		display.setForeground(COLOR_DISPLAYTEXT);
		display.setColumns(40);
		display.setEditable(false);

		Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 7);

		display.setBorder(border);


		// add components and layout panel
		add(display);

		setVisible(true);
	}


	public GUIConfigurationDisplay getDisplayPanel() {
		return getDisplayPanel();
	}

	public void updateMessage(String message) {
		display.setText(message);
	}


}
