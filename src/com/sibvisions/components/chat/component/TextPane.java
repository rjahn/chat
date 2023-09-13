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
package com.sibvisions.components.chat.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>TextPane</code> shows styled text.
 * 
 * @author René Jahn
 */
public class TextPane extends JTextPane
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the placeholder. */
    private String sPlaceholder;
    
    /** the animator. */
    private Animator animator;
    
    /** the animate value. */
    private float animate;
    
    /** whether the animation should be shown. */
    private boolean bShowAnimation = true;    

    /** whether to ignore the animation. */
    private boolean bIgnoreAnimation = false;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>TextPane</code>.
     */
    public TextPane()
    {
    	this(false);
    }
    
    /**
     * Creates a new instance of <code>TextPane</code>.
     * 
     * @param pReadOnly <code>true</code> to disable edit features
     */
    public TextPane(boolean pReadOnly)
	{
    	setOpaque(false);
        setForeground(new Color(255, 255, 255));
        setSelectionColor(new Color(200, 200, 200, 100));
        setCaretColor(new Color(200, 200, 200, 100));
        setEditorKit(new WrapStyledEditorKit());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        if (pReadOnly)
        {
        	setEditable(false);
        }
        
        if (!pReadOnly)
        {
	        animator = new Animator(350, new TimingTargetAdapter() 
	        {
	            @Override
	            public void timingEvent(float fraction) 
	            {
	                if (bShowAnimation) 
	                {
	                    animate = fraction;
	                } 
	                else 
	                {
	                    animate = 1f - fraction;
	                }
	                
	                repaint();
	            }
	
	            @Override
	            public void end() 
	            {
	                bShowAnimation = !bShowAnimation;
	                
	                repaint();
	            }
	
	        });
	        animator.setResolution(0);
	        animator.setAcceleration(.5f);
	        animator.setDeceleration(.5f);
	        
	        getDocument().addDocumentListener(new DocumentListener() 
	        {
	            @Override
	            public void insertUpdate(DocumentEvent e) 
	            {
	                if (!StringUtil.isEmpty(getText())) 
	                {
	                	if (bShowAnimation) 
	                	{
	                        if (!animator.isRunning()) 
	                        {
	                            startAnimation();
	                        }
	                    } 
	                	else if (animator.isRunning()) 
	                	{
	                        startAnimation();
	                    }
	                }
	            }
	
	            @Override
	            public void removeUpdate(DocumentEvent e) 
	            {
	                if (StringUtil.isEmpty(getText())) 
	                {
	                    startAnimation();
	                }
	            }
	
	            @Override
	            public void changedUpdate(DocumentEvent e) 
	            {
	            }
	        });
        }
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void paint(Graphics pGraphics) 
    {
        if (sPlaceholder != null && !sPlaceholder.equals("")) 
        {
            int h = getHeight();

            Graphics2D g2 = (Graphics2D)pGraphics.create();
            
            try
            {
	            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
	            Insets ins = getInsets();
	            FontMetrics fm = pGraphics.getFontMetrics();
	            
	            g2.setColor(new Color(170, 170, 170));
	            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - animate));
	            g2.drawString(sPlaceholder, ins.left + (animate * 30), h / 2 + fm.getAscent() / 2 - 1);
            }
            finally
            {
            	g2.dispose();
            }
        }
        
        super.paint(pGraphics);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets the placeholder text.
     * 
     * @param pPlaceholder the placeholder text
     */
    public void setPlaceholder(String pPlaceholder) 
    {
        sPlaceholder = pPlaceholder;
        
        repaint();
    }

    /**
     * Gets the placeholder text.
     * 
     * @return the placeholder text
     */
    public String getPlaceholder() 
    {
        return sPlaceholder;
    }
    
    /**
     * Starts the animation for the placeholder.
     */
    private void startAnimation() 
    {
    	if (bIgnoreAnimation)
    	{
	        animator.stop();
    		animator.setStartFraction(0f);

    		return;
    	}
    	
    	if (animator != null)
    	{
	        if (animator.isRunning()) 
	        {
	            float f = animator.getTimingFraction();
	            
	            animator.stop();
	            animator.setStartFraction(1f - f);
	        } 
	        else 
	        {
	            animator.setStartFraction(0f);
	        }
    	}
	        
        animator.start();
    }
    
    /**
     * Sets text without animation.
     * 
     * @param pText the text
     */
    public void setTextNoAnimation(String pText)
    {
    	animate = 0;
    	
    	bIgnoreAnimation = true;
    	
    	try
    	{
    		super.setText(pText);
    	}
    	finally
    	{
    		bIgnoreAnimation = false;
    	}
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************
    
    /**
     * The <code>WrapStyledEditorKit</code> is a {@link StyledEditorKit} with wrap support.
     * 
     * @author René Jahn
     */
    private static class WrapStyledEditorKit extends StyledEditorKit
    {
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Overwritten methods
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	@Override
	    public ViewFactory getViewFactory() 
	    {
	        return new WrapViewFactory();
	    }
	    
    }	// WrapStyledEditorKit
    
    /**
     * The <code>WrapViewFactory</code> is a {@link ViewFactory} with wrap support.
     * 
     * @author René Jahn
     */
    private static class WrapViewFactory implements ViewFactory 
    {
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Overwritten methods
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	@Override
        public View create(Element pElement) 
        {
            String sName = pElement.getName();
            
            if (sName != null) 
            {
                if (sName.equals(AbstractDocument.ContentElementName)) 
                {
                    return new WrapLabelView(pElement);
                } 
                else if (sName.equals(AbstractDocument.ParagraphElementName)) 
                {
                    return new ParagraphView(pElement);
                } 
                else if (sName.equals(AbstractDocument.SectionElementName)) 
                {
                    return new BoxView(pElement, View.Y_AXIS);
                } 
                else if (sName.equals(StyleConstants.ComponentElementName)) 
                {
                    return new ComponentView(pElement);
                } 
                else if (sName.equals(StyleConstants.IconElementName)) 
                {
                    return new IconView(pElement);
                }
            }
            
            return new LabelView(pElement);
        }
        
    }	// WarpViewFactory

    /**
     * The <code>WrapLabelView</code> is a {@link LabelView} with wrap support.
     * 
     * @author Reé Jahn
     */
    private static class WrapLabelView extends LabelView 
    {
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * Creates a new instance of <code>WrapLabelView</code>.
    	 * 
    	 * @param pElement the element
    	 */
    	public WrapLabelView(Element pElement) 
        {
            super(pElement);
        }

	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Overwritten methods
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        @Override
        public float getMinimumSpan(int pAxis) 
        {
            switch (pAxis) 
            {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(pAxis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + pAxis);
            }
        }
        
    }	// WarpLabelView

}	// TextPane
