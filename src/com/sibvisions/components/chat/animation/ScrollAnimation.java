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
package com.sibvisions.components.chat.animation;

import java.awt.Component;

import javax.swing.JScrollPane;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

/**
 * The <code>ScrollAnimation</code> class scrolls animated.
 * 
 * @author René Jahn
 */
public class ScrollAnimation 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class mebers
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

	/** the animator. */
	private final Animator animator;
    
	/** the timing target. */
    private TimingTarget target;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

    /**
     * Creates a new instance of <code>ScrollAnimation</code>.
     * 
     * @param pComponent the connected component
     */
    public ScrollAnimation(Component pComponent) 
    {
        animator = new Animator(350);
        animator.setResolution(0);
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
        
        animator.addTarget(new TimingTargetAdapter() 
        {
            @Override
            public void begin() 
            {
                pComponent.revalidate();
            }
        });
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

    /**
     * Scrolls horizontal.
     * 
     * @param pScroll the scroll pane
     * @param pValue the scrollbar position
     */
    public void scrollHorizontal(JScrollPane pScroll, int pValue) 
    {
        stop();
        
        animator.removeTarget(target);
        target = new PropertySetter(pScroll.getHorizontalScrollBar(), "value", pScroll.getHorizontalScrollBar().getValue(), pValue);
        animator.addTarget(target);
        animator.start();
    }

    /**
     * Scrolls vertical.
     * 
     * @param pScroll the scroll pane
     * @param pValue the scrollbar position
     */
    public void scrollVertical(JScrollPane pScroll, int pValue) 
    {
        stop();
        
        animator.removeTarget(target);
        
        target = new PropertySetter(pScroll.getVerticalScrollBar(), "value", pScroll.getVerticalScrollBar().getValue(), pValue);
        
        animator.addTarget(target);
        animator.start();
    }

    /**
     * Stops animation if it's running.
     */
    public void stop() 
    {
        if (animator.isRunning()) 
        {
            animator.stop();
        }
    }

    /**
     * Gets whether the animation is running.
     * 
     * @return <code>true</code> if animation is running
     */
    public boolean isRunning() 
    {
        return animator.isRunning();
    }
    
}	// ScrollButton
