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
package com.sibvisions.components.chat.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import jiconfont.IconCode;
import jiconfont.swing.IconFontSwing;

/**
 * The <code>GradientIconFontSwing</code> is like {@link IconFontSwing} but with support for gradient 
 * text color.
 * 
 * @author René Jahn
 */
public final class GradientIconFontSwing
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the gradient direction. */
	public enum GradientDirection
	{
		/** no gradient. */
		None,
		/** top to bottom. */
		TopToBottom, 
		/** left to right. */
		LeftToRight, 
		/** top left to bottom right. */
		TopLeftToBottomRight,
		/** bottom left to top right. */
		BottomLeftToTopRight
	};
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>GradientIconFontSwing</code> is a utility class.
	 */
	private GradientIconFontSwing()
	{
		// No instance needed.
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Builds an icon.
     *
     * @param pCode the icon code
     * @param pSize the size
     * @param pStart the start color
     * @param pEnd the end color
     * @param pDirection the gradient direction
     * @return the icon
     */
    public static Icon buildIcon(IconCode pCode, float pSize, Color pStart, Color pEnd, GradientDirection pDirection)
	{
		if (pDirection == null || pDirection == GradientDirection.None)
		{
			return IconFontSwing.buildIcon(pCode, pSize, pStart);
		}
    	
		return new ImageIcon(buildImage(pCode, pSize, pStart, pEnd, pDirection));
	}
    
    /**
     * Builds an image.
     *
     * @param pCode the icon code
     * @param pSize the size
     * @param pStart the start color
     * @param pEnd the end color
     * @param pDirection the gradient direction
     * @return the image
     */
    private static Image buildImage(IconCode pCode, float pSize, Color pStart, Color pEnd, GradientDirection pDirection) 
    {
        return buildImage(Character.toString(pCode.getUnicode()), 
        		          IconFontSwing.buildFont(pCode.getFontFamily()).deriveFont(pSize), 
        		          pSize, pStart, pEnd, pDirection);
    }
	
    /**
     * Builds an image.
     *
     * @param pText the icon text/character
     * @param pFont the icon font
     * @param pSize the size
     * @param pStart the start color
     * @param pEnd the end color
     * @param pDirection the gradient direction
     * @return the image
     */
	private static BufferedImage buildImage(String pText, Font pFont, float pSize, Color pStart, Color pEnd, GradientDirection pDirection) 
	{
		//original code https://github.com/jIconFont/jiconfont-swing/blob/master/src/main/java/jiconfont/swing/IconFontSwing.java
		//
		//modified for gradient paint
		
        JLabel label = new JLabel(pText);
        label.setForeground(pStart);
        label.setFont(pFont);
        
        Dimension dim = label.getPreferredSize();

        int width = dim.width + 1;
        int height = dim.height + 1;
        
        label.setSize(width, height);
        
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = bimg.createGraphics();
        
        try
        {
	        g2d.setFont(pFont);
	        
	        FontMetrics ftmetrics = g2d.getFontMetrics();
	        Rectangle2D rTextBounds = ftmetrics.getStringBounds(pText, g2d);
	        
	        double x = (width - rTextBounds.getWidth()) / 2;
	        double y = (height - rTextBounds.getHeight()) / 2;
	        
	        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	        
	        int x1, x2, y1, y2;
	        
	        switch (pDirection)
	        {
	        	case TopToBottom:
		            x1 = 0;
		            y1 = 0;
		            x2 = 0;
		            y2 = height;
		            
		            break;
		            
	        	case BottomLeftToTopRight:
		            x1 = 0;
		            y1 = height;
		            x2 = width;
		            y2 = 0;
		            
		            break;
		            
	        	case TopLeftToBottomRight:
		            x1 = 0;
		            y1 = 0;
		            x2 = width;
		            y2 = height;
		            
		            break;
		            
	        	case LeftToRight:
	        	default:
		            x1 = 0;
		            y1 = 0;
		            x2 = width;
		            y2 = 0;
	        }
	        
	        GradientPaint gra = new GradientPaint(x1, y1, pStart, x2, y2, pEnd);
	        
	        g2d.setPaint(gra);
	        g2d.drawString(pText, (int) x, (int) (y + ftmetrics.getAscent()));
        }
        finally
        {
        	g2d.dispose();
        }
        
        return bimg;		
	}
    
}	// GradientIconFontSwing
