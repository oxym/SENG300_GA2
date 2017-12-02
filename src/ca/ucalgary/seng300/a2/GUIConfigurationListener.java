package ca.ucalgary.seng300.a2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		System.out.println(keyName);
		
		int kIndex = cfghand.getKeyIndex(keyName);
		if (kIndex >= 0){ // If it exists
			cfghand.pressKey(kIndex);
		}
	}
}
