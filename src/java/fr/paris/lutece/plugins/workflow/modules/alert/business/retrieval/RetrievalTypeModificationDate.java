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
import fr.paris.lutece.plugins.workflow.modules.alert.business.TaskAlertConfig;

import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * RetrievalTypeModificationDate
 *
 */
public class RetrievalTypeModificationDate extends AbstractRetrievalType
{
    /**
     * {@inheritDoc}
     */
    public Long getDate( TaskAlertConfig config, Record record )
    {
        if ( record != null )
        {
            /*
             * The modification date of the record is updated only after all
             * tasks are executed. In other words, this task would retrieve
             * the old modification date before the execution of the action.
             * Ex : We have a record with
             *  - modification date : 14/12/2012
             *  - execution date of the task : 15/12/2012
             * The reference date should not be 14/12/2012 but 15/12/2012.
             */
            return GregorianCalendar.getInstance(  ).getTimeInMillis(  );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String checkConfigData( HttpServletRequest request )
    {
        return null;
    }
}