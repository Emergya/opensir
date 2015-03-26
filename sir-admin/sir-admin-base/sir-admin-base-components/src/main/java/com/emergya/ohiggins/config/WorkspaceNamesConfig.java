/*
 * Copyright (C) 2013 Emergya
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.emergya.ohiggins.config;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class is used to contain configuration values for GeoServer workspace
 * names used by  the application.
 *  
 * @author lroman
 */
@Component
public class WorkspaceNamesConfig {
    @Value("#{webProperties['workspaceNames.publicWorkspace']}")
    private String publicWorkspaceName;
    @Value("#{webProperties['workspaceNames.requestsWorkspace']}")
    private String requestsWorspace;
    @Value("#{webProperties['workspaceNames.tmpWorkspace']}")
    private String tmpWorkspace;

    /**
     * @return the publicWorkspaceName
     */
    public String getPublicWorkspaceName() {
        if(Strings.isNullOrEmpty(publicWorkspaceName)) {
            throw new IllegalStateException("publicWorkspaceName should have been set. Check your application.properties file!");
        }
        return publicWorkspaceName;
    }

    /**
     * @return the requestsWorspace
     */
    public String getRequestsWorspace() {
        if(Strings.isNullOrEmpty(requestsWorspace)) {
            throw new IllegalStateException("requestsWorspace should have been set. Check your application.properties file!");
        }
        return requestsWorspace;
    }

    /**
     * @return the tmpWorkspace
     */
    public String getTmpWorkspace() {
        if(Strings.isNullOrEmpty(tmpWorkspace)) {
            throw new IllegalStateException("tmpWorkspace should have been set. Check your application.properties file!");
        }
        return tmpWorkspace;
    }
    
      
}
