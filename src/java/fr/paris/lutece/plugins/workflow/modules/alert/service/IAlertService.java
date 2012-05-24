/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.modules.alert.service;

import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.workflow.modules.alert.business.Alert;
import fr.paris.lutece.plugins.workflow.modules.alert.business.TaskAlertConfig;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;
import java.util.Locale;


/**
 *
 * IAlertService
 *
 */
public interface IAlertService
{
    /**
    * Create a new alert
    * @param alert the alert
    */
    void create( Alert alert );

    /**
     * Remove an alert by id history
     * @param nIdResourceHistory the id history
     * @param nIdTask the id task
     */
    void removeByHistory( int nIdResourceHistory, int nIdTask );

    /**
     * Remove an alert by id task
     * @param nIdTask the id task
     */
    void removeByTask( int nIdTask );

    /**
     * Find an alert
     * @param nIdResourceHistory the id history
     * @param nIdTask the id task
     * @return an {@link Alert}
     */
    Alert find( int nIdResourceHistory, int nIdTask );

    /**
     * Find all alerts
     * @return a list of {@link Alert}
     */
    List<Alert> findAll(  );

    // CHECKS

    /**
     * Check if the given entry type id is accepted for the date
     * @param nIdEntryType the id entry type
     * @return true if it is accepted, false otherwise
     */
    boolean isEntryTypeDateAccepted( int nIdEntryType );

    /**
     * Check if the record has the same state before executing the action
     * @param config the config
     * @param record the record
     * @param locale the locale
     * @return true if the record has a valid state, false otherwise
     */
    boolean isRecordStateValid( TaskAlertConfig config, Record record, Locale locale );

    // GET

    /**
     * Get the list of entries from a given id task
     * @param nIdTask the id task
     * @return a list of IEntry
     */
    List<IEntry> getListEntries( int nIdTask );

    /**
     * Get the list of entries that have the accepted type (which are defined in <b>workflow-alert.properties</b>)
     * @param nIdTask the id task
     * @param locale the Locale
     * @return a ReferenceList
     */
    ReferenceList getListEntriesDate( int nIdTask, Locale locale );

    /**
     * Get the list of directories
     * @return a ReferenceList
     */
    ReferenceList getListDirectories(  );

    /**
     * Get the list of states
     * @param nIdAction the id action
     * @return a ReferenceList
     */
    ReferenceList getListStates( int nIdAction );

    /**
     * Get the record from a given Alert
     * @param alert the alert
     * @return a {@link Record}
     */
    Record getRecord( Alert alert );

    /**
     * Get the date
     * @param config the config
     * @param nIdRecord the id record
     * @param nIdDirectory the id directory
     * @return the date
     */
    long getDate( TaskAlertConfig config, int nIdRecord, int nIdDirectory );

    /**
     * Get the record field value
     * @param nPosition the position of the entry
     * @param nIdRecord the id record
     * @param nIdDirectory the id directory
     * @return the record field value
     */
    String getRecordFieldValue( int nPosition, int nIdRecord, int nIdDirectory );

    // ACTIONS

    /**
     * Do change the record state
     * @param config the config
     * @param nIdRecord the id record
     * @param alert the alert
     */
    void doChangeRecordState( TaskAlertConfig config, int nIdRecord, Alert alert );
}
