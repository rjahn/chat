/*
 * Copyright 2023 René Jahn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sibvisions.components.chat;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;

/**
 * Tests {@link Chat} functionality.
 * 
 * @author René Jahn
 */
public class TestChat extends JFrame
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Main
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Start the chat.
	 * 
	 * @param pArgs the arguments
	 */
	public static void main(String[] pArgs)
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
            public void run() 
            {
                TestChat chat = new TestChat();
                chat.setVisible(true);
            }
        });
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests {@link Chat}.
	 */
	public TestChat()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JVxBorderLayout layout = new JVxBorderLayout();
		
		setLayout(layout);
		
		Chat chat = new Chat();
		
		add(chat, JVxBorderLayout.CENTER);
		
        pack();
        setLocationRelativeTo(null);
	}
	
}	// TestChat