/*
 * This file is part of muCommander, http://www.mucommander.com
 * Copyright (C) 2002-2008 Maxence Bernard
 *
 * muCommander is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * muCommander is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mucommander.ui.autocomplete.completers;

import java.util.Vector;

import com.mucommander.ui.autocomplete.AutocompleterTextComponent;

/**
 * FileCompleter is a Completer based on root folders and file paths. 
 * 
 * @author Arik Hadas, based on the code of Santhosh Kumar: http://www.jroller.com/santhosh/entry/file_path_autocompletion
 */
 
public class PathCompleter extends Completer { 
    
	public PathCompleter(){  
		registerService(ServiceFactory.getRootFoldersService());
		registerService(ServiceFactory.getAllFilesService());
    }
 
	protected Vector getUpdatedSuggestions(AutocompleterTextComponent component) {
    	return getPossibleCompletionsFromServices(component.getText());
    } 
 
    public void updateTextComponent(final String selected, AutocompleterTextComponent comp){ 
        if(selected==null) 
            return;
                                 
        String location = tryToCompleteFromServices(selected);        
        if (comp.isEnabled() && location != null)
        	comp.setText(location);
    }
}