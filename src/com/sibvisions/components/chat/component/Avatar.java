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

import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.sibvisions.util.type.ImageUtil;

/**
 * The <code>Avatar</code> shows a round area with a resized image in this area. It doesn't show the
 * image with round clipping like other Avatar implementations.
 */
public class Avatar extends JComponent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the image. */
	private Image image;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Avatar</code>.
	 */
	public Avatar()
    {
		setSize(28, 28);
		setMinimumSize(new Dimension(24, 24));
		setMaximumSize(new Dimension(24, 24));
		
		setBorder(null);
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    protected void paintComponent(Graphics grphcs) 
    {
        Graphics2D g2 = (Graphics2D)grphcs.create();
        
        try
        {
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        
	        if (image != null) 
	        {
	            int width = getWidth();
	            int height = getHeight();
	            
	            int diameter = Math.min(width, height);
	            
	            BufferedImage img = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
	            Graphics2D g2Img = img.createGraphics();
	            
	            g2Img.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            g2Img.setColor(new Color(180, 180, 180, 90));
	            g2Img.fillOval(0, 0, diameter, diameter);
	            
	            Composite composite = g2Img.getComposite();
	            
	            try
	            {
	            	Image imgScaled = ImageUtil.getScaledImage(image, diameter - 10, diameter - 10, true);
	            	
		            g2Img.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		            g2Img.drawImage(imgScaled, diameter - imgScaled.getWidth(this) - 5, diameter - imgScaled.getHeight(this) - 5, this);
	            }
	            finally
	            {
		            g2Img.setComposite(composite);
		            g2Img.dispose();
	            }
	            
	            g2.drawImage(img, 0, 0, this);
	        }
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
     * Sets the image.
     * 
     * @param pImage the image
     */
    public void setImage(Image pImage) 
    {
        image = pImage;
        
        repaint();
    }

    /**
     * Gets the image.
     * 
     * @return the image
     */
    public Image getImage() 
    {
        return image;
    }

}	// Avatar
