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
package com.sibvisions.components.chat.event;

import com.sibvisions.components.chat.Chat;

/**
 * The <code>ChatEvent</code> is the base class for all events of {@link Chat}.
 * 
 * @author René Jahn
 */
public abstract class ChatEvent 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the close event. */
	public static final int EVENT_CLOSE = 0;
	
	/** the message event. */
	public static final int EVENT_MESSAGE = 1;
	
	/** the chat. */
	private Chat chat;
	
	/** the event type. */
	private int type;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ChatEvent</code>.
	 * 
	 * @param pChat the chat
	 * @param pType the event type
	 */
	protected ChatEvent(Chat pChat, int pType)
	{
		chat = pChat;
		type = pType;
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the chat.
	 * 
	 * @return the chat
	 */
	public Chat getChat() 
	{
		return chat;
	}
	
	/**
	 * Gets the event type.
	 * 
	 * @return the type
	 */
	public int getType()
	{
		return type;
	}
	
}	// ChatEvent
