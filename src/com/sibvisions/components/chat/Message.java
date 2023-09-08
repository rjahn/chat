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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import javax.swing.JScrollPane;

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
	
	/** the message layout. */
	private JVxFormLayout flThis = new JVxFormLayout();

	/** the message bubble. */
	private Bubble bubble;
	
	/** the component resize listener. */
	private ComponentAdapter listener;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
	
	/**
	 * Creates a new instance of <code>Message</code>.
	 * 
	 * @param pText the text to show
	 */
	public Message(String pText)
	{
		this(pText, Type.Left);
	}
	
	/**
	 * Creates a new instance of <code>Message</code>.
	 * 
	 * @param pText the text to show
	 * @param pType the message type
	 */
	public Message(String pText, Type pType)
	{
		flThis.setMargins(new Insets(5, 0, 5, 0));
		
		bubble = new Bubble(pText, pType);
		
		setOpaque(false);

		setLayout(flThis);
		
		if (pType == Type.Left)
		{
			add(bubble, flThis.createConstraint(0, 0));
		}
		else
		{
			add(bubble, flThis.createConstraint(-1, 0));
		}
		
		listener = new ComponentAdapter() 
		{
			public void componentResized(ComponentEvent pEvent)
			{
				//don't grow out of the message area
				bubble.setMaximumSize(new Dimension(pEvent.getComponent().getWidth(), Integer.MAX_VALUE));
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
		
		getParent().getParent().addComponentListener(listener);
	}
	
	@Override
	public void removeNotify()
	{
		super.removeNotify();
		
		getParent().getParent().removeComponentListener(listener);
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

		/** the text. */
		private String text;
		
		/** the message type. */
		private Type type;
		
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
		
		/**
		 * Creates a new instance of <code>Bubble</code>.
		 * 
		 * @param pText the text to show
		 * @param pType the message type
		 */
		public Bubble(String pText, Type pType)
		{
			text = pText;
			type = pType;
			
			TextPane textPane = new TextPane(true);
			textPane.setText(text);
			textPane.setHighlighter(null);
			textPane.setMinimumSize(new Dimension(50, 20));
			
			JScrollPane scpText = new JScrollPane();
			scpText.setOpaque(false);
	        scpText.setBorder(null);
	        scpText.setViewportBorder(null);
	        scpText.setViewportView(textPane);
	        scpText.getViewport().setOpaque(false);
	        scpText.getVerticalScrollBar().setOpaque(false);
	        scpText.getVerticalScrollBar().setPreferredSize(new Dimension(1, 0));
	        scpText.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
	
			JVxBorderLayout blThis = new JVxBorderLayout();
			
			if (pType == Type.Left)
			{
				blThis.setMargins(new Insets(0, 8, 0, 0));
			}
			else
			{
				textPane.setForeground(new Color(30, 30, 30));
				
				blThis.setMargins(new Insets(0, 0, 0, 8));
			}
			
			setLayout(blThis);
			
			add(textPane, JVxBorderLayout.CENTER);
		}
		
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

		    if (type == Type.Right)
		    {
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-width, 0);

			    path.transform(tx);
			    
			    graphics2D.setPaint(new Color(255, 191, 0, 210));
		    }
		    else
		    {
		    	graphics2D.setPaint(new Color(250, 250, 250, 65));
		    }
		    
		    path.closePath();

			graphics2D.fill(path);
		    
		    super.paintComponent(g);
		}		
		
	}	// Bubble

}	// Message
