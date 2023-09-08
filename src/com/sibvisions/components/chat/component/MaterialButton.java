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
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 * The <code>MaterialButton</code> is a button with pressed effect like a material design button.
 * 
 * @author René Jahn
 */
public class MaterialButton extends JButton 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the animator. */
    private Animator animator;

    /** the point where lieft mouse was pressed. */
    private Point ptPressed;
    
    /** the color for pressed animation. */
    private Color colPressed = new Color(173, 173, 173);

    /** the arc. */
    private int arc;
    
    /** the target size for the pressed animation. */
    private int targetSize;

    /** the current pressed animation size. */
    private float animatSize;
    
    /** alpha composite value. */
    private float alpha;
    
    /** whether to paint the background. */
    private boolean bBackgroundPainted;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>MaterialButton</code>.
     */
    public MaterialButton() 
    {
        init();
    }

    /**
     * Initializes the button.
     */
    private void init() 
    {
        super.setContentAreaFilled(false);
        
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mousePressed(MouseEvent me) 
            {
                if (SwingUtilities.isLeftMouseButton(me)) 
                {
                    ptPressed = me.getPoint();

                    targetSize = Math.max(getWidth(), getHeight()) * 2;
                    animatSize = 0;
                    alpha = 0.5f;
                    
                    if (animator.isRunning()) 
                    {
                        animator.stop();
                    }
                    
                    animator.start();
                }
            }
        });
        
        TimingTarget target = new TimingTargetAdapter() 
        {
            @Override
            public void timingEvent(float fraction) 
            {
                if (fraction > 0.5f) 
                {
                    alpha = 1 - fraction;
                }
                
                animatSize = fraction * targetSize;
                repaint();
            }
        };
        
        animator = new Animator(400, target);
        animator.setResolution(0);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public void setContentAreaFilled(boolean pFilled) 
    {
    	//can't be changed
    }
    
    @Override
    protected void paintComponent(Graphics pGraphics) 
    {
        int width = getWidth();
        int height = getHeight();
        
        Graphics2D g2 = (Graphics2D)pGraphics.create();
        
        try
        {
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        
	        if (bBackgroundPainted) 
	        {
	            g2.setColor(getBackground());
	            g2.fillRoundRect(0, 0, width, height, arc, arc);
	        }
	        
	        if (ptPressed != null) 
	        {
	            g2.setColor(colPressed);
	            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
	            
	            Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, arc, arc));
	            
	            area.intersect(new Area(new Ellipse2D.Double((ptPressed.x - animatSize / 2), (ptPressed.y - animatSize / 2), animatSize, animatSize)));
	            g2.fill(area);
	        }
        }
        finally
        {
        	g2.dispose();
        }
        
        super.paintComponent(pGraphics);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Sets whether the background should be painted.
     * 
     * @param pPaint <code>true</code> to paint the background, <code>false</code> otherwise
     */
    public void setBackgroundPainted(boolean pPaint) 
    {
        bBackgroundPainted = pPaint;
        
        repaint();
    }

    /**
     * Gets whether the background should be painted.
     * 
     * @return <code>true</code> to paint the background, <code>false</code> otherwise
     */
    public boolean isBackgroundPainted() 
    {
        return bBackgroundPainted;
    }

    /**
     * Sets the arc.
     * 
     * @param pArc the arc
     */
    public void setArc(int pArc) 
    {
    	arc = pArc;
        
        repaint();
    }

    /**
     * Gets the arc.
     * 
     * @return the arc
     */
    public int getArc() 
    {
        return arc;
    }

    /**
     * Sets the color for pressed state.
     * 
     * @param pColor the color
     */
    public void setPressedColor(Color pColor) 
    {
        colPressed = pColor;
        
        repaint();
    }

    /**
     * Gets the color for pressed state.
     * 
     * @return the color
     */
    public Color getPressedColor() 
    {
        return colPressed;
    }
    
}	// MaterialButton
