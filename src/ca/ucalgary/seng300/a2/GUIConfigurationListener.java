package ca.ucalgary.seng300.a2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listens to the Configuration Panel actions
 *
 */
public class GUIConfigurationListener implements ActionListener {



	@Override
	public void actionPerformed(ActionEvent event) {

		String command = event.getActionCommand();
		System.out.println(command);

		switch (command) {


		case "enter":

			break;

		case "shift":

			break;

			default:
				//TODO: just press the associated button

		}//end switch

	}

}
