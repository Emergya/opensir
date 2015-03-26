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
package com.emergya.ohiggins.web.helper;

import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.LayerPublishRequestDto;
import org.springframework.validation.BindingResult;

/**
 * Offers helper methods for handling publication requests so controllers can 
 * delegate common tasks.
 * 
 * @author lroman
 */
public interface PublicationRequestHelper {
    
    /**
     * Possible results for the saving method.
     */
     public enum SaveRequestResult {
	/**
	 * Validation errors found.
	 */
	FAILURE_VALIDATION_ERRORS(false),
	/**
	 * The layer that the user wants to publish no longer exists.
	 */
	FAILURE_SOURCE_LAYER_MISSING(false),
	/**
	 * There is already another publication request for the selected source layer.
	 */
	FAILURE_EXISTING_REQUEST(false),
	/**
	 * An error happened in a publication attemp in geoserver.
	 */
	FAILURE_GEOSERVER_ERROR(false),
	/**
	 * The creation of a new publication request was successful.
	 */
	SUCCESS_REQUEST_CREATION(true),
	/**
	 * The update of an existing publication request with new data was successful.
	 */
	SUCCESS_REQUEST_MODIFICATION(true);
	
	private boolean success;

	private SaveRequestResult(boolean success) {
	    this.success = success;
	}
	
	public boolean isSuccess() {
	    return success;
	}
     }
     
    /**
     * Attemps to create a publication request in the database, and duplicates
     * the source layer in geoserver.
     * @param requestDto
     * @return 
     */
    public SaveRequestResult savePublicationRequest(LayerPublishRequestDto requestDto, boolean validateMetadata, BindingResult errors);
    
    
    /**
     * Enumerate type defining the possible result status of the layer rejection
     * method.
     */
    public enum RejectRequestResult {
	/**
	 * There was an error rejecting the layer publication request.
	 */
	FAILURE(false),
	/**
	 * Publication request rejection was succesful but for some reason 
	 * we couldn't send the requesting user a mail thelling him that.
	 */
	SUCCESS_NO_MAIL(false),
	/**
	 * Publication requeste rejection was succesful, incluing sending mail to 
	 * the requesting user.
	 */
	SUCCESS(true);
	
	private boolean success;

	private RejectRequestResult(boolean success) {
	    this.success = success;
	}
	
	public boolean isSuccess() {
	    return success;
	}
    }
    
    
    /**
     * Rejects a layer's publication request, removing geoserver data.
     * @param publicationRequestId
     * @param comment
     * @return 
     */
    public RejectRequestResult rejectPublicationRequest(Long publicationRequestId, String comment);
    
   
    /**
     * Wrapper for the results of the publication process.
     */
    public class ConfirmRequestResult {
        private  ConfirmRequestResultStatus status;        
        private final  LayerDto publishedLayer; 

        public ConfirmRequestResult(ConfirmRequestResultStatus status, LayerDto publishedLayer) {
            this.status = status;
            this.publishedLayer = publishedLayer;
        }

        public ConfirmRequestResult(ConfirmRequestResultStatus status) {
            this.status = status;
            this.publishedLayer = null;
        }
        
        public boolean isSuccess() {
            return getStatus() !=null && getStatus().isSuccess();
        }

        /**
         * @return the status
         */
        public ConfirmRequestResultStatus getStatus() {
            return status;
        }

        /**
         * @param status the status to set
         */
        public void setStatus(ConfirmRequestResultStatus status) {
            this.status = status;
        }

        /**
         * @return the publishedLayer
         */
        public LayerDto getPublishedLayer() {
            return publishedLayer;
        }
    }
     /**
     * Result codes for the <c>confirmPublicationRequest</c> method.
     */
    public enum ConfirmRequestResultStatus  {
	       /**
         * No existing publication request was found.
         */
        FAILURE_NO_REQUEST(false),
        /**
         * Couldn't publish a new layer in geoserver.
         */
        FAILURE_GEOSERVER_PUBLISH(false),
        /**
         * The layer to be updated was not found.
         */
        FAILURE_MISSING_UPDATED_LAYER(false),
        /**
         * Couldn't update the layer in geoserver.
         */
        FAILURE_GEOSERVER_LAYER_UPDATE(false),
        /**
         * Publication was successful.
         */
        SUCCESS(true),
        /**
         * Publication was successful but couldn't delete request data from
         * Geoserver.
         */
        SUCCESS_COULDNT_CLEAN_GEOSERVER(true);

        boolean success;

        private ConfirmRequestResultStatus(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }
    }
    
    public ConfirmRequestResult  confirmPublicationRequest(LayerPublishRequestDto requestDto);
}
