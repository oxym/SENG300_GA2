package ca.ucalgary.seng300.a2;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JButton;


/**
 * Listens to the Configuration Panel actions
 *
 */
public class GUIConfigurationListener implements ActionListener {
	
	private VendingManager mgr;
	
	public GUIConfigurationListener(VendingManager mgr) {
		this.mgr = mgr;
	}

	
	@Override
	public void actionPerformed(ActionEvent event) {

		ConfigPanelHandler cfghand = mgr.getConfigPanelHandler();
		String keyName = event.getActionCommand();
		if (mgr.isDebug()) System.out.println(keyName);
		
		//If the button is shift, we should toggle its background to indicate shift state
		if (keyName.equals("Shift")){
			JButton button = ((JButton) event.getSource());
			Color downColor = Color.LIGHT_GRAY;
			if (button.getBackground() == downColor){				
				button.setBackground(null); //Default color
			} else{
				button.setBackground(downColor);
			}
		}
		
		//Relay the message to the configuration panel handler
		int kIndex = cfghand.getKeyIndex(keyName);
		if (kIndex >= 0){ // If it exists
			cfghand.pressKey(kIndex);
		}
	}
}
