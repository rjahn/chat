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

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sibvisions.components.chat.Chat.ButtonPosition;
import com.sibvisions.components.chat.event.ChatEvent;
import com.sibvisions.components.chat.event.ChatListener;
import com.sibvisions.components.chat.event.MessageEvent;
import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;

/**
 * Tests {@link Chat} functionality.
 * 
 * @author René Jahn
 */
public class TestChat extends JFrame
                      implements ChatListener
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
//		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JVxBorderLayout layout = new JVxBorderLayout();
		
		setLayout(layout);
		
		Chat chat = new Chat();
		chat.setAvatarRight(((ImageIcon)IconFontSwing.buildIcon(GoogleMaterialDesignIcons.VERIFIED_USER, 18f, new Color(200, 200, 200))).getImage());
		chat.setTitle("Chat with me");
		chat.setCloseVisible(true);
		chat.addChatListener(this);
		
		chat.addButton(GoogleMaterialDesignIcons.CHECK, "Create", null, ButtonPosition.Right);
		chat.addButton(GoogleMaterialDesignIcons.HOURGLASS_FULL, "OK", null, ButtonPosition.Right);
		
		chat.addButton(GoogleMaterialDesignIcons.SPEAKER, null, null, ButtonPosition.Left);
		
		Message msg = new Message("Hello and welcome!", Message.Type.Right);
		
		chat.addMessage(msg);
		
		add(chat, JVxBorderLayout.CENTER);
		
        pack();
        setLocationRelativeTo(null);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
		/**
	 * {@inheritDoc}
	 */
	public void chatNotification(ChatEvent pEvent)
	{
		switch (pEvent.getType())
		{
			case ChatEvent.EVENT_CLOSE:
				dispose();
				break;
			case ChatEvent.EVENT_MESSAGE:
				pEvent.getChat().addMessage(new Message(((MessageEvent)pEvent).getMessage().trim(), pEvent.getChat().getMessages().length %2 == 1 ? Message.Type.Left : Message.Type.Right));
				break;
			default:
				//ignore
		}
	}
	
}	// TestChat
