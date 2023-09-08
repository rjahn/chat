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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.border.Border;

/**
 * The <code>ArcPanel</code> is a panel with round corners.
 * 
 * @author René Jahn
 */
public class ArcPanel extends BasePanel 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the arc. */
    private int arc;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>ArcPanel</code>.
     */
    public ArcPanel() 
    {
    }
    
    /**
     * Creates a new instance of <code>ArcPanel</code> with a custom layout manager.
     * 
     * @param pLayout the layout manager
     */
    public ArcPanel(LayoutManager pLayout)
    {
    	super(pLayout);
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    protected void paintComponent(Graphics pGraphics) 
    {
    	if (arc >= 0)
    	{
	        Graphics2D g2 = (Graphics2D)pGraphics.create();
	        
	        try
	        {
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        
	        	g2.setColor(getBackground());
	        
		        Border border = getBorder();
		        
		        Insets ins;
		        
		        if (border != null)
		        {
		        	ins = border.getBorderInsets(this);
		        }
		        else
		        {
		        	ins = INSETS_0;
		        }
		        
	        	g2.fillRoundRect(0 + ins.left, 0 + ins.top, getWidth() - ins.left - ins.right, getHeight() - ins.top - ins.bottom, arc, arc);
	        }
	        finally
	        {
	        	g2.dispose();
	        }
    	}
        
        super.paintComponent(pGraphics);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined Methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    
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
    
}	// ArcPanel
