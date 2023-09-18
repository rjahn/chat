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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;
import javax.swing.UIManager;

import com.sibvisions.components.chat.component.Avatar;
import com.sibvisions.components.chat.component.BasePanel;
import com.sibvisions.components.chat.component.TextPane;
import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;

/**
 * The <code>Message</code> is a painted message with a bubble around the message.
 * 
 * @author René Jahn
 */
public class Message extends BasePanel 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class mebers
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

	/** the message type. */
	public enum Type
	{
		/** left message. */
		Left,
		/** right message. */
		Right
	};
	
	/** the chat. */
	private Chat chat;

	/** the message layout. */
	private JVxFormLayout flThis = new JVxFormLayout();

	/** the message bubble. */
	private Bubble bubble;
	
	/** the avatar. */
	private Avatar avatar;
	
	/** the component resize listener. */
	private ComponentAdapter listener;

	/** the message type. */
	private Type type;

	/** the text. */
	private String text;
	
	/** the background color. */
	private Color colBackground;
	
	/** the foreground color. */
	private Color colForeground;
	
	/** whether the message is a typing message. */
	private boolean isTyping;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
	
	/**
	 * Creates a new instance of <code>Message</code>.
	 * 
	 * @param pChat the chat
	 * @param pText the text to show
	 */
	public Message(Chat pChat, String pText)
	{
		this(pChat, pText, Type.Left);
	}
	
	/**
	 * Creates a new instance of <code>Message</code>.
	 * 
	 * @param pChat the chat
	 * @param pText the text to show
	 * @param pType the message type
	 */
	public Message(Chat pChat, String pText, Type pType)
	{
		chat = pChat;
		text = pText;
		type = pType;
		
		flThis.setMargins(new Insets(5, 0, 5, 0));
		
		bubble = new Bubble(this);
		avatar = new Avatar();
		
		setOpaque(false);

		setLayout(flThis);
		
		if (pType == Type.Left)
		{
			add(avatar, flThis.createConstraint(0, 0));
			add(bubble, flThis.createConstraint(1, 0));
			
			Image img = chat.getAvatarLeft();
			
			if (img != null)
			{
				avatar.setImage(img);
			}
			else
			{
				avatar.setVisible(false);
			}
		}
		else
		{
			add(avatar, flThis.createConstraint(-1, 0));
			add(bubble, flThis.createConstraint(-2, 0));
			
			Image img = chat.getAvatarRight();
			
			if (img != null)
			{
				avatar.setImage(img);
			}
			else
			{
				avatar.setVisible(false);
			}
		}
		
		listener = new ComponentAdapter() 
		{
			public void componentResized(ComponentEvent pEvent)
			{
				updateBubbleMaxSize(pEvent.getComponent());
				
				bubble.revalidate();
			}
		};
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

	@Override
	public void addNotify()
	{
		super.addNotify();

		Component comp = getParent().getParent();

		//the initial size should be correct
		updateBubbleMaxSize(comp);
		
		comp.addComponentListener(listener);
	}
	
	@Override
	public void removeNotify()
	{
		super.removeNotify();
		
		getParent().getParent().removeComponentListener(listener);
	}
	
	@Override
   	public void setForeground(Color pColor)
	{
		if (bubble != null)
		{
			colForeground = pColor;
	
			bubble.update();
		}

		super.setForeground(pColor);
	}

	@Override
	public Color getForeground()
	{
		return colForeground;
	}
	
	@Override
	public void setBackground(Color pColor)
	{
		if (bubble != null)
		{
			colBackground = pColor;
		
			bubble.update();
		}
		
		super.setBackground(pColor);
	}

	@Override
	public Color getBackground()
	{
		return colBackground;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Type getType()
	{
		return type;
	}
	
	/**
	 * Sets the text.
	 * 
	 * @param pText the text
	 */
	public void setText(String pText)
	{
		text = pText;
		
		bubble.update();
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * Sets whether the message is a typing message.
	 * 
	 * @param pTyping <code>true</code> if typing, <code>false</code> otherwise
	 */
	public void setTyping(boolean pTyping)
	{
		isTyping = pTyping;
		
		bubble.update();
	}
	
	/**
	 * Gets whether the message is a typing message.
	 * 
	 * @return <code>true</code> if typing, <code>false</code> otherwise
	 */
	public boolean isTyping()
	{
		return isTyping;
	}
	
	/**
	 * Updates the maximum size of the bubble.
	 * 
	 * @param pComponent the parent component
	 */
	private void updateBubbleMaxSize(Component pComponent)
	{
		//don't grow out of the message area
		bubble.setMaximumSize(new Dimension(pComponent.getWidth() - (avatar.isVisible() ? avatar.getWidth() + flThis.getHorizontalGap() : 0), 
				                     		Integer.MAX_VALUE));
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************

	/**
	 * The <code>Bubble</code> shows a message in a bubble.
	 * 
	 * @author René Jahn
	 */
	public static class Bubble extends BasePanel
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

		/** the message. */
		private Message message;
		
		/** the text pane. */
		private TextPane textPane;
		
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
		
		/**
		 * Creates a new instance of <code>Bubble</code>.
		 * 
		 * @param pMessage the message
		 */
		public Bubble(Message pMessage)
		{
			message = pMessage;

			Object oMin = UIManager.get("Component.minimumWidth");
			
			//important for some LaFs (like Flat)
			UIManager.put("Component.minimumWidth", Integer.valueOf(20));
			
			try
			{
				textPane = new TextPane(true);
				textPane.setHighlighter(null);
				textPane.setMinimumSize(new Dimension(20, 20));
			}
			finally
			{
				UIManager.put("Component.minimumWidth", oMin);
			}
			
//			JScrollPane scpText = new JScrollPane();
//			scpText.setOpaque(false);
//	        scpText.setBorder(null);
//	        scpText.setViewportBorder(null);
//	        scpText.setViewportView(textPane);
//	        scpText.getViewport().setOpaque(false);
//	        scpText.getVerticalScrollBar().setOpaque(false);
//	        scpText.getVerticalScrollBar().setPreferredSize(new Dimension(1, 0));
//	        scpText.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

			update();
			
			JVxBorderLayout blThis = new JVxBorderLayout();
			
			if (message.type == Type.Left)
			{
				blThis.setMargins(new Insets(0, 8, 0, 0));
			}
			else
			{
				blThis.setMargins(new Insets(0, 0, 0, 8));
			}
			
			setLayout(blThis);
			
			add(textPane, JVxBorderLayout.CENTER);
		}
		
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Overwritten methods
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

		@Override
		protected void paintComponent(Graphics g) 
		{
		    final Graphics2D graphics2D = (Graphics2D)g;
		    
		    RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		    
		    graphics2D.setRenderingHints(qualityHints);
		    
		    int width = getWidth();
		    int height = getHeight();
		    
		    GeneralPath path = new GeneralPath();
		    path.moveTo(5, 10);
		    path.curveTo(5, 10, 7, 5, 0, 0);
		    path.curveTo(0, 0, 12, 0, 12, 5);
		    path.curveTo(12, 5, 12, 0, 20, 0);
		    path.lineTo(width - 10, 0);
		    path.curveTo(width - 10, 0, width, 0, width, 10);
		    path.lineTo(width, height - 10);
		    path.curveTo(width, height - 10, width, height, width - 10, height);
		    path.lineTo(15, height);
		    path.curveTo(15, height, 5, height, 5, height - 10);
		    path.lineTo(5, 15);

		    Color colBackground = message.getBackground();

		    if (message.type == Type.Left)
		    {
		    	graphics2D.setPaint(colBackground != null ? colBackground : message.chat.getDefaultMessageBackgroundLeft());
		    }
		    else
		    {
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-width, 0);

			    path.transform(tx);
			    
			    graphics2D.setPaint(colBackground != null ? colBackground : message.chat.getDefaultMessageBackgroundRight());
		    }
		    
		    path.closePath();

			graphics2D.fill(path);
		    
		    super.paintComponent(g);
		}		
		
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // User-defined methods
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
		
		/**
		 * Updates text and colors.
		 */
		protected void update()
		{
			textPane.setText(message.chat.translate(message.text));
			
			Color colForeground = message.getForeground();
			
			if (message.type == Type.Left)
			{
				//left: icon after text
				if (message.isTyping)
				{
					Icon ico = message.chat.getTypingIconLeft();
					
					if (ico != null)
					{
						textPane.insertIcon(ico);
					}
				}
				
				textPane.setForeground(colForeground != null ? colForeground : message.chat.getDefaultMessageForegroundLeft());
			}
			else
			{
				//right: icon before text
				if (message.isTyping)
				{
					Icon ico = message.chat.getTypingIconRight();
					
					if (ico != null)
					{
						textPane.setCaretPosition(0);
						textPane.insertIcon(ico);
					}
				}

				textPane.setForeground(colForeground != null ? colForeground : message.chat.getDefaultMessageForegroundRight());
			}

			if (message.isShowing())
			{
				repaint();
			}
		}
		
	}	// Bubble

}	// Message
