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
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.rad.model.ui.ITranslatable;
import javax.rad.util.ITranslator;
import javax.rad.util.TranslationMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.sibvisions.components.chat.animation.FloatingComponentAnimation;
import com.sibvisions.components.chat.animation.ScrollAnimation;
import com.sibvisions.components.chat.component.ArcPanel;
import com.sibvisions.components.chat.component.BasePanel;
import com.sibvisions.components.chat.component.MaterialButton;
import com.sibvisions.components.chat.component.TextPane;
import com.sibvisions.components.chat.event.ChatListener;
import com.sibvisions.components.chat.event.CloseEvent;
import com.sibvisions.components.chat.event.MessageEvent;
import com.sibvisions.components.chat.util.GradientIconFontSwing;
import com.sibvisions.components.chat.util.GradientIconFontSwing.GradientDirection;
import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout.Constraint;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.StringUtil;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;

/**
 * The <code>Chat</code> class is a simple chat component.
 */
public class Chat extends BasePanel
                  implements ITranslatable,
                             ITranslator
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

	/** the close button. */
	private MaterialButton butClose = new MaterialButton();

	/** the scroll messages down animation. */
	private ScrollAnimation ascMessages;

	/** the scroll down button floating animation. */
    private FloatingComponentAnimation fbaScrollDown;
    
    /** the message panel. */
    private ArcPanel panMessages = new ArcPanel();

    /** the title bar panel. */
    private JPanel panTitleBar = new JPanel();

    /** the title panel. */
    private ArcPanel panTitle = new ArcPanel();
    
    /** the messages layout. */
    private JVxFormLayout flMessages = new JVxFormLayout();
    
	/** The translation mapping. */
	private TranslationMap translation = null;
    
	/** the left message color. */
	private Color colMessageLeft = new Color(250, 250, 250, 65);

	/** the right message color. */
	private Color colMessageRight = new Color(255, 191, 0, 210);
	
	/** the left avatar. */
	private Image imgAvatarLeft;

	/** the right avatar. */
	private Image imgAvatarRight;
	
	/** the title. */
	private JLabel lblTitle = new JLabel();
	
	/** the untranslated title. */
	private String sTitle;
	
	/** the messages. */
	private ArrayUtil<Message> liMessages = new ArrayUtil<Message>();

	/** the chat listeners. */
	private ArrayUtil<ChatListener> liListeners = new ArrayUtil<ChatListener>();
    
    /** whether the translation is enabled. */
    private boolean bTranslationEnabled = true;
	
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
		
		blMain.setMargins(new Insets(5, 5, 5, 5));
		
		setLayout(blMain);
		
		Color colWhite = new Color(255, 255, 255, 20);

        //------------------------------------------------------------
		// Title
        //------------------------------------------------------------
		
        butClose.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CLOSE, 16f, new Color(200, 200, 200)));
        butClose.setVerticalAlignment(SwingConstants.CENTER);
        butClose.setHorizontalAlignment(SwingConstants.CENTER);
        butClose.setArc(28);
        butClose.setPreferredSize(new Dimension(28, 28));
        butClose.setBackgroundPainted(true);
        butClose.setBackground(new Color(140, 140, 140, 100));
        butClose.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	fireClose(e);
            }
        });        
		
		JVxFormLayout flTitle = new JVxFormLayout();
		flTitle.setMargins(new Insets(5, 0, 5, 0));

		JVxFormLayout flTitleBar = new JVxFormLayout();
		flTitleBar.setMargins(new Insets(0, 0, 8, 0));
		
		panTitleBar.setLayout(flTitleBar);
		panTitleBar.setOpaque(false);
		
		panTitle = new ArcPanel(flTitle);
        panTitle.setBackground(colWhite);

        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 13f));
        lblTitle.setBorder(new EmptyBorder(2, 10, 2, 2));
        lblTitle.setForeground(new Color(220, 220, 220));

        panTitle.add(lblTitle, flTitle.createConstraint(0, 0, -1, 0));
        
        panTitleBar.add(panTitle, flTitleBar.createConstraint(0, 0, -2, -1));
        panTitleBar.add(butClose, flTitleBar.createConstraint(-1, 0));

        panTitle.setVisible(false);
        butClose.setVisible(false);
        
        panTitleBar.setVisible(false);
        
        //------------------------------------------------------------
		// Center
        //------------------------------------------------------------

		butSend.setIcon(GradientIconFontSwing.buildIcon(GoogleMaterialDesignIcons.SEND, 20f, colMessageRight, 
				                                        new Color(255, 143, 0), GradientDirection.LeftToRight));
		
        butSend.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	String sText = text.getText();
            	
            	if (!StringUtil.isEmpty(sText))
            	{
            		int iCount = liMessages.size();
            		
	            	fireMessage(text.getText());
	            	
	            	boolean bScroll = iCount != liMessages.size();
	            	
	            	scpMessages.repaint();
	            	
	            	if (bScroll)
	            	{
	            		scrollToBottom();
	            	}
	            	
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
		panBottom.setBackground(colWhite);

		panBottom.add(scpText, flBottom.createConstraint(0, 0, -2, -1));
		panBottom.add(butSend, flBottom.createConstraint(-1, -1));
		
		add(panTitleBar, JVxBorderLayout.NORTH);
		add(lpCenter, JVxBorderLayout.CENTER);
		add(panBottom, JVxBorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(300, 380));
		
		ascMessages = new ScrollAnimation(panMessages);
        fbaScrollDown = new FloatingComponentAnimation(butScrollDown);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public void setTranslation(TranslationMap pTranslation)
    {
    	if (translation != pTranslation)
    	{
	    	translation = pTranslation;
	    	
	    	updateTranslation();
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public TranslationMap getTranslation()
    {
    	return translation;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        bTranslationEnabled = pEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return bTranslationEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public String translate(String pText)
    {
    	if (translation != null)
    	{
    		return translation.translate(pText);
    	}
    	else
    	{
    		return pText;
    	}
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
	
	@Override
	public void requestFocus()
	{
		text.requestFocus();
	}
	
	@Override
	public void setEnabled(boolean pEnabled)
	{
		super.setEnabled(pEnabled);
		
		text.setEditable(pEnabled);
		butSend.setEnabled(pEnabled);
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
	
	/**
	 * Updates translation.
	 */
	private void updateTranslation()
	{
		text.setPlaceholder(translate("Enter your message"));
		
		setTitle(sTitle);
	}

	/**
	 * Sets the color of left messages.
	 * 
	 * @param pColor the color
	 */
	public void setMessageColorLeft(Color pColor)
	{
		colMessageLeft = pColor;
	}

	/**
	 * Gets the color of right messages.
	 * 
	 * @return the color
	 */
	public Color getMessageColorLeft()
	{
		return colMessageLeft;
	}
	
	/**
	 * Sets the color of right messages.
	 * 
	 * @param pColor the color
	 */
	public void setMessageColorRight(Color pColor)
	{
		colMessageRight = pColor;
	}

	/**
	 * Gets the color of right messages.
	 * 
	 * @return the color
	 */
	public Color getMessageColorRight()
	{
		return colMessageRight;
	}
	
	/**
	 * Gets the left avatar.
	 * 
	 * @return the left avatar
	 */
	public Image getAvatarLeft()
	{
		return imgAvatarLeft;
	}

	/**
	 * Sets the left avatar.
	 * 
	 * @param pImage the left avatar
	 */
	public void setAvatarLeft(Image pImage)
	{
		imgAvatarLeft = pImage;
	}
	
	/**
	 * Gets the right avatar.
	 * 
	 * @return the right avatar
	 */
	public Image getAvatarRight()
	{
		return imgAvatarRight;
	}
	
	/**
	 * Sets the right avatar.
	 * 
	 * @param pImage the right avatar
	 */
	public void setAvatarRight(Image pImage)
	{
		imgAvatarRight = pImage;
	}

	/**
	 * Sets the gradient color of send button.
	 * 
	 * @param pStart the start color
	 * @param pEnd the end color
	 */
	public void setSendButtonColor(Color pStart, Color pEnd)
	{
		butSend.setIcon(GradientIconFontSwing.buildIcon(GoogleMaterialDesignIcons.SEND, 20f, pStart, pEnd, GradientDirection.LeftToRight));
	}
	
	/**
	 * Sets the title. If given title is <code>null</code> no title bar will be shown.
	 * 
	 * @param pTitle the title
	 */
	public void setTitle(String pTitle)
	{
		sTitle = pTitle;
		
		lblTitle.setText(translate(sTitle));
		
		panTitle.setVisible(sTitle != null);
		
		panTitleBar.setVisible(panTitle.isVisible() || butClose.isVisible());
	}
	
	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle()
	{
		return sTitle;
	}
	
	/**
	 * Sets whether close should be visible.
	 * 
	 * @param pVisible <code>true</code> to show close, <code>false</code> otherwise
	 */
	public void setCloseVisible(boolean pVisible)
	{
		butClose.setVisible(pVisible);
		
		panTitleBar.setVisible(panTitle.isVisible() || butClose.isVisible());
	}
	
	/**
	 * Gets whether close is visible.
	 * 
	 * @return <code>true</code> if visible, <code>false</code> otherwise
	 */
	public boolean isCloseVisible()
	{
		return butClose.isVisible();
	}

	/**
	 * Adds a message to the chat.
	 * 
	 * @param pMessage the message
	 */
	public void addMessage(Message pMessage)
	{
		liMessages.add(pMessage);
		
		int iPos = liMessages.size() - 1;
		
    	panMessages.add(pMessage, flMessages.createConstraint(0, iPos, -1, iPos));
    	
    	scrollToBottom();
	}
	
	/**
	 * Gets all available messages.
	 * 
	 * @return the messages
	 */
	public Message[] getMessages()
	{
		return liMessages.toArray(new Message[liMessages.size()]);
	}
	
	/**
	 * Removes a message from the chat.
	 * 
	 * @param pMessage the message
	 */
	public void removeMessage(Message pMessage)
	{
		liMessages.remove(pMessage);
		
		panMessages.remove(pMessage);
	}
	
	/**
	 * Adds a chat listener if not already added.
	 * 
	 * @param pListener the listener
	 */
	public void addChatListener(ChatListener pListener)
	{
		synchronized (liListeners)
		{
			if (!liListeners.contains(pListener))
			{
				liListeners.add(pListener);
			}
		}
	}
	
	/**
	 * Removes a chat listener.
	 * 
	 * @param pListener the listener
	 */
	public void removeChatListener(ChatListener pListener)
	{
		synchronized (liListeners)
		{
			liListeners.remove(pListener);
		}
	}
	
	/**
	 * Gets all available chat listeners.
	 * 
	 * @return the listeners
	 */
	public ChatListener[] getChatListeners()
	{
		synchronized (liListeners)
		{
			return liListeners.toArray(new ChatListener[liListeners.size()]);
		}
	}
	
	/**
	 * Fires close event.
	 * 
	 * @param pEvent the close button event
	 */
	protected void fireClose(ActionEvent pEvent)
	{
		ArrayUtil<ChatListener> listeners;
	
		synchronized (liListeners)
		{
			listeners = new ArrayUtil<ChatListener>(liListeners);
		}
		
		for (ChatListener lis : listeners)
		{
			lis.chatNotification(new CloseEvent(this));
		}
	}
	
	/**
	 * Fires (add) message event.
	 * 
	 * @param pMessage the message
	 */
	protected void fireMessage(String pMessage)
	{
		ArrayUtil<ChatListener> listeners;
	
		synchronized (liListeners)
		{
			listeners = new ArrayUtil<ChatListener>(liListeners);
		}
		
		for (ChatListener lis : listeners)
		{
			lis.chatNotification(new MessageEvent(this, pMessage));
		}
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
