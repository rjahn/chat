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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.sibvisions.components.chat.animation.FloatingComponentAnimation;
import com.sibvisions.components.chat.animation.ScrollAnimation;
import com.sibvisions.components.chat.component.BasePanel;
import com.sibvisions.components.chat.component.MaterialButton;
import com.sibvisions.components.chat.component.ArcPanel;
import com.sibvisions.components.chat.component.TextPane;
import com.sibvisions.components.chat.util.GradientIconFontSwing;
import com.sibvisions.components.chat.util.GradientIconFontSwing.GradientDirection;
import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout.Constraint;
import com.sibvisions.util.type.StringUtil;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;

/**
 * The <code>Chat</code> class is a simple chat component.
 */
public class Chat extends BasePanel
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the text message. */
	private TextPane text = new TextPane();
	
	/** the messages scroll panel. */
    private JScrollPane scpMessages = new JScrollPane();

	/** the main layout. */
	private JVxBorderLayout blMain = new JVxBorderLayout();
	
	/** the send button. */
	private MaterialButton butSend = new MaterialButton();
	
	/** the scroll down button. */
	private MaterialButton butScrollDown = new MaterialButton();

	/** the scroll messages down animation. */
	private ScrollAnimation ascMessages;

	/** the scroll down button floating animation. */
    private FloatingComponentAnimation fbaScrollDown;
    
    /** the message panel. */
    private ArcPanel panMessages = new ArcPanel();
    
    /** the messages layout. */
    private JVxFormLayout flMessages = new JVxFormLayout();
    
    /** the number of messages. */
    private int iMessageCount = 0;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Chat</code>.
	 */
	public Chat()
	{
		super.setOpaque(false);
		
		IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
		IconFontSwing.register(FontAwesome.getIconFont());
		
		butSend.setIcon(GradientIconFontSwing.buildIcon(GoogleMaterialDesignIcons.SEND, 20f, new Color(255, 191, 0), 
				                                        new Color(255, 143, 0), GradientDirection.LeftToRight));
		
        butSend.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	String sText = text.getText();
            	
            	if (!StringUtil.isEmpty(sText))
            	{
            		Message msg = new Message(text.getText(), iMessageCount % 2 == 0 ? Message.Type.Right : Message.Type.Left);
            		
	            	panMessages.add(msg, flMessages.createConstraint(0, iMessageCount, -1, iMessageCount));
	            	
	            	iMessageCount++;
	            	
	            	scpMessages.repaint();
	            	
	            	scrollToBottom();
	            	
	            	text.setTextNoAnimation(null);
	            	text.requestFocus();
	            	
	            	revalidate();
            	}
            }
        });
		
        butScrollDown.setIcon(GradientIconFontSwing.buildIcon(FontAwesome.ANGLE_DOWN, 35f, new Color(79, 79, 79, 240), 
        		                                              new Color(248, 248, 248, 240), GradientDirection.TopToBottom));
        butScrollDown.setArc(40);
        butScrollDown.setPreferredSize(new Dimension(40, 40));
        butScrollDown.setBackgroundPainted(true);
        butScrollDown.setBackground(new Color(100, 100, 100, 100));
        butScrollDown.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	scrollToBottom();
            }
        });
		
		blMain.setMargins(new Insets(5, 5, 5, 5));
		
		setLayout(blMain);

		//Center
		
		flMessages.setMargins(new Insets(0, 0, 0, 0));
		
		panMessages = new ArcPanel();
		panMessages.setBackground(new Color(0, 0, 0, 0));
		panMessages.setMinimumSize(new Dimension(0, 0));
        panMessages.setLayout(flMessages);
        
        JScrollBar sbVertical = new JScrollBar();
        sbVertical.setUI(new ReducedScrollBarUI());
        sbVertical.setPreferredSize(new Dimension(3, 3));
        sbVertical.setOpaque(false);
        sbVertical.setUnitIncrement(20);
        
        scpMessages.setOpaque(false);
        scpMessages.setBorder(null);
        scpMessages.setViewportBorder(null);
        scpMessages.setViewportView(panMessages);
        scpMessages.setVerticalScrollBar(sbVertical);
        scpMessages.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scpMessages.getViewport().setOpaque(false);
        scpMessages.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() 
        {
            private int oldValues;

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) 
            {
                int value = scpMessages.getVerticalScrollBar().getValue();
                int extent = scpMessages.getVerticalScrollBar().getModel().getExtent();
             
                if ((value + extent) >= scpMessages.getVerticalScrollBar().getMaximum() - 80) 
                {
                    fbaScrollDown.setVisible(false);
                } 
                else if (oldValues <= e.getValue()) 
                {
                    if (!ascMessages.isRunning()) 
                    {
                        fbaScrollDown.setVisible(true);
                    }
                }
            }
        });  
        
		JVxFormLayout flCenter = new JVxFormLayout();
		flCenter.setMargins(new Insets(0, 0, 5, 0));
		
		JLayeredPane lpCenter = new JLayeredPane();
        lpCenter.setLayout(flCenter);
        
        lpCenter.setLayer(butScrollDown, JLayeredPane.POPUP_LAYER);
        
        lpCenter.add(butScrollDown, new JVxFormLayout.Constraint(null, null, 
        		                                                 new JVxFormLayout.Anchor(flCenter.getBottomMarginAnchor()), 
        		                                                 new JVxFormLayout.Anchor(flCenter.getRightAnchor())));
        lpCenter.add(scpMessages, flCenter.createConstraint(0, 0, -1, -1));        
        
        Constraint cons = flCenter.getConstraint(butScrollDown);
        
        cons.getBottomAnchor().setAutoSize(false);
        cons.getBottomAnchor().setPosition(45);
		
        //------------------------------------------------------------
		// Bottom
        //------------------------------------------------------------
        
		text.setPlaceholder("Enter your message");
        text.addKeyListener(new KeyAdapter() 
        {
            @Override
            public void keyPressed(KeyEvent pEvent) 
            {
                if (pEvent.isControlDown()
                	&& pEvent.getKeyCode() == KeyEvent.VK_ENTER)
                {
                	butSend.doClick();
                }
                else
                {
	            	//updates layout because of possible text growth (vertical)
	                revalidate();
                }
            }
        });
		
		JScrollPane scpText = new JScrollPane();
		scpText.setOpaque(false);
        scpText.setBorder(null);
        scpText.setViewportBorder(null);
        scpText.setViewportView(text);
        scpText.getViewport().setOpaque(false);
        scpText.setMaximumSize(new Dimension(100, 100));
        scpText.getVerticalScrollBar().setOpaque(false);
        scpText.getVerticalScrollBar().setPreferredSize(new Dimension(1, 0));
        scpText.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		
		JVxFormLayout flBottom = new JVxFormLayout();
		flBottom.setMargins(new Insets(0, 0, 0, 0));
		
		ArcPanel panBottom = new ArcPanel(flBottom);
		panBottom.setBackground(new Color(255, 255, 255, 20));

		panBottom.add(scpText, flBottom.createConstraint(0, 0, -2, -1));
		panBottom.add(butSend, flBottom.createConstraint(-1, -1));
		
		add(lpCenter, JVxBorderLayout.CENTER);
		add(panBottom, JVxBorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(300, 380));
		
		
		ascMessages = new ScrollAnimation(panMessages);
        fbaScrollDown = new FloatingComponentAnimation(butScrollDown);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
    protected void paintComponent(Graphics grphcs) 
    {
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = (Graphics2D) grphcs.create();

        Border border = getBorder();
        
        Insets ins;
        
        if (border != null)
        {
        	ins = getBorder().getBorderInsets(this);
        }
        else
        {
        	ins = INSETS_0;
        }
        
        try
        {
	        g2.setPaint(new GradientPaint(0, 0, new Color(58, 72, 85), width, 0, new Color(28, 38, 50)));
        	g2.fillRect(0 + ins.left, 0 + ins.top, width - ins.left - ins.right, height - ins.top - ins.bottom);
        }
        finally
        {
        	g2.dispose();
        }
        
        super.paintComponent(grphcs);
    }	

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Scrolls to bottom.
	 */
	private void scrollToBottom()
	{
    	ascMessages.scrollVertical(scpMessages, scpMessages.getVerticalScrollBar().getMaximum());
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************
	
	/**
	 * The <code>ReducedScrollBarUI</code> should look like a modern scrollbar with small scrollbars
	 * and no navigation buttons.
	 */
	public static class ReducedScrollBarUI extends BasicScrollBarUI 
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** the thumb color. */
	    private static final Color THUMB_COLOR = Color.gray;

	    /** the thumb size. */
	    private static final int THUMB_SIZE = 4;
	    
	    /** the alpha composite value. */
	    private static final int ALPHA = 50;
	    /** the rollover alpha composite value. */
	    private static final int ALPHA_ROLLOVER = 100;
	    
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /**
	     * Creates a new instance of <code>ReducedScrollBarUI</code>.
	     */
	    public ReducedScrollBarUI() 
	    {
	    }
	
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Overwritten methods
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    @Override
	    protected JButton createDecreaseButton(int orientation) 
	    {
	        return new HiddenButton();
	    }
	
	    @Override
	    protected JButton createIncreaseButton(int orientation) 
	    {
	        return new HiddenButton();
	    }
	
	    @Override
	    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) 
	    {
	    }
	
	    @Override
	    protected Dimension getMinimumThumbSize() 
	    {
	        return new Dimension(0, 75);
	    }
	
	    @Override
	    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) 
	    {
	        int alpha = isThumbRollover() ? ALPHA_ROLLOVER : ALPHA;
	        int orientation = scrollbar.getOrientation();
	        int x = thumbBounds.x;
	        int y = thumbBounds.y;
	
	        int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width;
	        width = Math.max(width, THUMB_SIZE);
	
	        int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height : THUMB_SIZE;
	        height = Math.max(height, THUMB_SIZE);
	
	        Graphics2D graphics2D = (Graphics2D) g.create();
	        
	        try
	        {
		        graphics2D.setColor(new Color(THUMB_COLOR.getRed(), THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
		        graphics2D.fillRect(x, y, width, height);
	        }
	        finally
	        {
	        	graphics2D.dispose();
	        }
	    }
	
	    //****************************************************************
	    // Subclass definition
	    //****************************************************************

	    /**
	     * The <code>HiddenButton</code> is a hidden button for a scrollbar.
	     * 
	     * @author René Jahn
	     */
	    private static final class HiddenButton extends JButton 
	    {
		    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		    // Class members
		    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    	/**
	    	 * Creates a new instance of <code>InvisibleScrollBarButton</code>.
	    	 */
	    	private HiddenButton() 
	        {
	            setOpaque(false);
	            setFocusable(false);
	            setFocusPainted(false);
	            setBorderPainted(false);
	            setBorder(BorderFactory.createEmptyBorder());
	        }
	        
	    }	// HiddenButton
	    
	}	// ModernScrollBarUI
	
}	// Chat
