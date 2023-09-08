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

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout.Constraint;

/**
 * The <code>FloatingComponentAnimation</code> adds an animation to a floating component.
 * 
 * @author René Jahn
 */
public class FloatingComponentAnimation
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class mebers
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

	/** the animator. */
	private final Animator animator;
    
	/** whether the animation is visible. */
    private boolean bVisible;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

    /**
     * Creates a new instance of <code>FloatingComponentAnimation</code>.
     * 
     * @param pComponent the component
     */
    public FloatingComponentAnimation(Component pComponent) 
    {
    	JVxFormLayout layout = (JVxFormLayout)pComponent.getParent().getLayout();
    	
        animator = new Animator(300, new TimingTargetAdapter() 
        {
            @Override
            public void timingEvent(float pFraction) 
            {
                int v;
                
                if (bVisible) 
                {
                    v = (int)((1f - pFraction) * 50);
                } 
                else 
                {
                    v = (int)(pFraction * 50);
                }
                
                Constraint cons = layout.getConstraint(pComponent);
                cons.getBottomAnchor().setPosition(v);
                
                pComponent.revalidate();
            }
        });
        animator.setResolution(1);
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

    /**
     * Sets the animation visibility.
     * 
     * @param pVisible <code>true</code> to set the animation visible
     */
    public void setVisible(boolean pVisible) 
    {
    	if (pVisible == isVisible())
    	{
    		return;
    	}
    	
    	stop();
    	
    	bVisible = pVisible;
    	
    	animator.start();
    }
    
    /**
     * Gets whether the animation is visible.
     *  
     * @return <code>true</code> if visible
     */
    public boolean isVisible()
    {
    	return bVisible;
    }

    /**
     * Stops the animation.
     */
    public void stop() 
    {
        if (isRunning()) 
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

    /**
     * Gets whether the animation is running.
     * 
     * @return <code>true</code> if animation is running
     */
    public boolean isRunning() 
    {
        return animator.isRunning();
    }
    
}	// FloatingComponentAnimation
