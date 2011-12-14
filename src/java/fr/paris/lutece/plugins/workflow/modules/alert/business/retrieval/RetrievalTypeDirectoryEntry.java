/*
 * Copyright (c) 2002-2011, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.alert.business.retrieval;

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.modules.alert.business.TaskAlertConfig;
import fr.paris.lutece.plugins.workflow.modules.alert.service.AlertService;
import fr.paris.lutece.plugins.workflow.modules.alert.util.constants.AlertConstants;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * RetrievalTypeDirectoryEntry
 *
 */
public class RetrievalTypeDirectoryEntry extends AbstractRetrievalType
{
    /**
     * {@inheritDoc}
     */
    public Long getDate( TaskAlertConfig config, Record record )
    {
        if ( ( config != null ) && ( record != null ) && ( record.getDirectory(  ) != null ) )
        {
            String strDate = AlertService.getService(  )
                                         .getRecordFieldValue( config.getPositionEntryDirectoryDate(  ),
                    record.getIdRecord(  ), record.getDirectory(  ).getIdDirectory(  ) );

            if ( StringUtils.isNotBlank( strDate ) )
            {
                return Long.parseLong( strDate );
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String checkConfigData( HttpServletRequest request )
    {
        String strPositionEntryDirectoryDate = request.getParameter( AlertConstants.PARAMETER_POSITION_ENTRY_DIRECTORY_DATE );
        int nPositionEntryDirectoryDate = DirectoryUtils.convertStringToInt( strPositionEntryDirectoryDate );

        if ( nPositionEntryDirectoryDate == DirectoryUtils.CONSTANT_ID_NULL )
        {
            return AlertConstants.PROPERTY_LABEL_POSITION_ENTRY_DIRECTORY_DATE;
        }

        return null;
    }
}