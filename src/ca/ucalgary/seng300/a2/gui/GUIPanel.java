package ca.ucalgary.seng300.a2.gui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * The parent class for the JPanels
 * This holds color information, that is common on all of
 * the JPanels
 */
public abstract class GUIPanel extends JPanel {

	private static final long serialVersionUID = 3205991961181700449L;

	//Color information for the JPanels,
	//add new colors here
	public static final Color COLOR_BACKGROUND = new Color(224,0,224);
	public static final Color COLOR1 = new Color(230, 230, 193);
	public static final Color COLOR2 = new Color(212, 212, 144);
	public static final Color COLOR3 = new Color(193, 221, 230);
	public static final Color COLOR_FONT = new Color(255, 255, 255);
	public static final Color COLOR_BLACK = new Color(0,0,0);


	/**
	 *
	 * update - repaints the panel after an event
	 *
	 *
	 */
	public void update() {
		revalidate();
		repaint();
	}

    /** Returns an ImageIcon, or null if the path was invalid. */
	/*
	 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
	 *
	 * Redistribution and use in source and binary forms, with or without
	 * modification, are permitted provided that the following conditions
	 * are met:
	 *
	 *   - Redistributions of source code must retain the above copyright
	 *     notice, this list of conditions and the following disclaimer.
	 *
	 *   - Redistributions in binary form must reproduce the above copyright
	 *     notice, this list of conditions and the following disclaimer in the
	 *     documentation and/or other materials provided with the distribution.
	 *
	 *   - Neither the name of Oracle or the names of its
	 *     contributors may be used to endorse or promote products derived
	 *     from this software without specific prior written permission.
	 *
	 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
	 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
	 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
	 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
	 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
	 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
	 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
	 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
	 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
	 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
	 */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = GUIPanel.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

	/**
	 * Lays out and displays the components of the JPanel
	 */
	abstract void init();

}
