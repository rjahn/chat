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

import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * The <code>BasePanel</code> is a non opaque base panel.
 * 
 * @author René Jahn
 */
public abstract class BasePanel extends JPanel 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** zero insets. */
	protected static final Insets INSETS_0 = new Insets(0, 0, 0, 0);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>BasePanel</code>.
     */
    public BasePanel() 
    {
        init();
    }
    
    /**
     * Creates a new instance of <code>BasePanel</code> with a custom layout manager.
     * 
     * @param pLayout the layout manager
     */
    public BasePanel(LayoutManager pLayout)
    {
    	super(pLayout);
    	
        init();
    }

    /**
     * Initializes the panel defaults.
     */
    private void init()
    {
    	super.setOpaque(false);    	
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public void setOpaque(boolean pOpaque)
    {
    	//can't be changed
    }
    
}	// BasePanel
