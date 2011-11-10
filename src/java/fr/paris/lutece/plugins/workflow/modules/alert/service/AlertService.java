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
package fr.paris.lutece.plugins.workflow.modules.alert.service;

import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.business.RecordFieldHome;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.business.ActionHome;
import fr.paris.lutece.plugins.workflow.business.ResourceHistory;
import fr.paris.lutece.plugins.workflow.business.ResourceHistoryHome;
import fr.paris.lutece.plugins.workflow.business.ResourceWorkflow;
import fr.paris.lutece.plugins.workflow.business.ResourceWorkflowHome;
import fr.paris.lutece.plugins.workflow.business.StateFilter;
import fr.paris.lutece.plugins.workflow.business.StateHome;
import fr.paris.lutece.plugins.workflow.business.task.ITask;
import fr.paris.lutece.plugins.workflow.business.task.TaskHome;
import fr.paris.lutece.plugins.workflow.modules.alert.business.Alert;
import fr.paris.lutece.plugins.workflow.modules.alert.business.AlertHome;
import fr.paris.lutece.plugins.workflow.modules.alert.business.TaskAlertConfig;
import fr.paris.lutece.plugins.workflow.modules.alert.util.constants.AlertConstants;
import fr.paris.lutece.plugins.workflow.service.WorkflowPlugin;
import fr.paris.lutece.plugins.workflow.service.WorkflowService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.portal.business.workflow.Action;
import fr.paris.lutece.portal.business.workflow.State;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 *
 * AlertService
 *
 */
public final class AlertService
{
    private static final String BEAN_ALERT_SERVICE = "workflow-alert.alertService";
    private List<Integer> _listAcceptedEntryTypesDate;

    /**
     * Private constructor
     */
    private AlertService(  )
    {
        // Init list accepted entry types for date
        _listAcceptedEntryTypesDate = fillListEntryTypes( AlertConstants.PROPERTY_ACCEPTED_DIRECTORY_ENTRY_TYPES_DATE );
    }

    /**
     * Get the service
     * @return the instance of the service
     */
    public static AlertService getService(  )
    {
        return (AlertService) SpringContextService.getPluginBean( AlertPlugin.PLUGIN_NAME, BEAN_ALERT_SERVICE );
    }

    // CRUD

    /**
     * Create a new alert
     * @param alert the alert
     */
    public void create( Alert alert )
    {
        if ( alert != null )
        {
            AlertHome.create( alert );
        }
    }

    /**
     * Remove an alert by id history
     * @param nIdResourceHistory the id history
     * @param nIdTask the id task
     */
    public void removeByHistory( int nIdResourceHistory, int nIdTask )
    {
        AlertHome.removeByHistory( nIdResourceHistory, nIdTask );
    }

    /**
     * Remove an alert by id task
     * @param nIdTask the id task
     */
    public void removeByTask( int nIdTask )
    {
        AlertHome.removeByTask( nIdTask );
    }

    /**
     * Find an alert
     * @param nIdResourceHistory the id history
     * @param nIdTask the id task
     * @return an {@link Alert}
     */
    public Alert find( int nIdResourceHistory, int nIdTask )
    {
        return AlertHome.find( nIdResourceHistory, nIdTask );
    }

    /**
     * Find all alerts
     * @return a list of {@link Alert}
     */
    public List<Alert> findAll(  )
    {
        return AlertHome.findAll(  );
    }

    // CHECKS

    /**
     * Check if the given entry type id is accepted for the date
     * @param nIdEntryType the id entry type
     * @return true if it is accepted, false otherwise
     */
    public boolean isEntryTypeDateAccepted( int nIdEntryType )
    {
        boolean bIsAccepted = false;

        if ( ( _listAcceptedEntryTypesDate != null ) && !_listAcceptedEntryTypesDate.isEmpty(  ) )
        {
            bIsAccepted = _listAcceptedEntryTypesDate.contains( nIdEntryType );
        }

        return bIsAccepted;
    }

    // GET

    /**
     * Get the list of entries from a given id task
     * @param nIdTask the id task
     * @return a list of IEntry
     */
    public List<IEntry> getListEntries( int nIdTask )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        TaskAlertConfig config = TaskAlertConfigService.getService(  ).findByPrimaryKey( nIdTask );

        List<IEntry> listEntries = new ArrayList<IEntry>(  );

        if ( config != null )
        {
            EntryFilter entryFilter = new EntryFilter(  );
            entryFilter.setIdDirectory( config.getIdDirectory(  ) );

            listEntries = EntryHome.getEntryList( entryFilter, pluginDirectory );
        }

        return listEntries;
    }

    /**
     * Get the list of entries that have the accepted type (which are defined in <b>workflow-alert.properties</b>)
     * @param nIdTask the id task
     * @param locale the Locale
     * @return a ReferenceList
     */
    public ReferenceList getListEntriesDate( int nIdTask, Locale locale )
    {
        ReferenceList refenreceListEntries = new ReferenceList(  );
        refenreceListEntries.addItem( DirectoryUtils.CONSTANT_ID_NULL, DirectoryUtils.EMPTY_STRING );

        for ( IEntry entry : getListEntries( nIdTask ) )
        {
            int nIdEntryType = entry.getEntryType(  ).getIdType(  );

            if ( isEntryTypeDateAccepted( nIdEntryType ) )
            {
                refenreceListEntries.addItem( entry.getPosition(  ), buildReferenceEntryToString( entry, locale ) );
            }
        }

        return refenreceListEntries;
    }

    /**
     * Get the list of directories
     * @return a ReferenceList
     */
    public ReferenceList getListDirectories(  )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        ReferenceList listDirectories = DirectoryHome.getDirectoryList( pluginDirectory );
        ReferenceList refenreceListDirectories = new ReferenceList(  );
        refenreceListDirectories.addItem( DirectoryUtils.CONSTANT_ID_NULL, StringUtils.EMPTY );

        if ( listDirectories != null )
        {
            refenreceListDirectories.addAll( listDirectories );
        }

        return refenreceListDirectories;
    }

    /**
     * Get the list of states
     * @param nIdAction the id action
     * @return a ReferenceList
     */
    public ReferenceList getListStates( int nIdAction )
    {
        Plugin pluginWorkflow = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );
        ReferenceList referenceListStates = new ReferenceList(  );
        Action action = ActionHome.findByPrimaryKey( nIdAction, pluginWorkflow );

        if ( ( action != null ) && ( action.getWorkflow(  ) != null ) )
        {
            StateFilter stateFilter = new StateFilter(  );
            stateFilter.setIdWorkflow( action.getWorkflow(  ).getId(  ) );

            List<State> listStates = StateHome.getListStateByFilter( stateFilter, pluginWorkflow );

            referenceListStates.addItem( DirectoryUtils.CONSTANT_ID_NULL, StringUtils.EMPTY );
            referenceListStates.addAll( ReferenceList.convert( listStates, AlertConstants.ID, AlertConstants.NAME, true ) );
        }

        return referenceListStates;
    }

    /**
     * Get the record from a given Alert
     * @param alert the alert
     * @return a {@link Record}
     */
    public Record getRecord( Alert alert )
    {
        Record record = null;

        if ( alert != null )
        {
            Plugin pluginWorkflow = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );
            Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
            ResourceHistory resourceHistory = ResourceHistoryHome.findByPrimaryKey( alert.getIdResourceHistory(  ),
                    pluginWorkflow );

            if ( ( resourceHistory != null ) &&
                    Record.WORKFLOW_RESOURCE_TYPE.equals( resourceHistory.getResourceType(  ) ) )
            {
                record = RecordHome.findByPrimaryKey( resourceHistory.getIdResource(  ), pluginDirectory );
            }
        }

        return record;
    }

    /**
     * Get the date
     * @param config the config
     * @param nIdRecord the id record
     * @param nIdDirectory the id directory
     * @return the date
     */
    public long getDate( TaskAlertConfig config, int nIdRecord, int nIdDirectory )
    {
        long lDate = 0;

        if ( config != null )
        {
            String strDate = getRecordFieldValue( config.getPositionEntryDirectoryDate(  ), nIdRecord, nIdDirectory );

            if ( StringUtils.isNotBlank( strDate ) )
            {
                lDate = Long.parseLong( strDate );
            }
        }

        return lDate;
    }

    /**
     * Get the action
     * @param nIdAction the id action
     * @return the action
     */
    public Action getAction( int nIdAction )
    {
        Plugin pluginWorkflow = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );

        return ActionHome.findByPrimaryKey( nIdAction, pluginWorkflow );
    }

    // ACTIONS

    /**
     * Do change the record state
     * @param config the config
     * @param nIdRecord the id record
     * @param alert the alert
     */
    public void doChangeRecordState( TaskAlertConfig config, int nIdRecord, Alert alert )
    {
        Plugin pluginWorkflow = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );

        // The locale is not important. It is just used to fetch the task action id
        Locale locale = I18nService.getDefaultLocale(  );
        ITask task = TaskHome.findByPrimaryKey( config.getIdTask(  ), pluginWorkflow, locale );

        if ( task != null )
        {
            State state = StateHome.findByPrimaryKey( config.getIdStateAfterDeadline(  ), pluginWorkflow );
            Action action = ActionHome.findByPrimaryKey( task.getAction(  ).getId(  ), pluginWorkflow );

            if ( ( state != null ) && ( action != null ) )
            {
                // Create Resource History
                ResourceHistory resourceHistory = new ResourceHistory(  );
                resourceHistory.setIdResource( nIdRecord );
                resourceHistory.setResourceType( Record.WORKFLOW_RESOURCE_TYPE );
                resourceHistory.setAction( action );
                resourceHistory.setWorkFlow( action.getWorkflow(  ) );
                resourceHistory.setCreationDate( WorkflowUtils.getCurrentTimestamp(  ) );
                resourceHistory.setUserAccessCode( AlertConstants.USER_AUTO );
                ResourceHistoryHome.create( resourceHistory, pluginWorkflow );

                // Update Resource
                ResourceWorkflow resourceWorkflow = ResourceWorkflowHome.findByPrimaryKey( nIdRecord,
                        Record.WORKFLOW_RESOURCE_TYPE, action.getWorkflow(  ).getId(  ), pluginWorkflow );
                resourceWorkflow.setState( state );
                ResourceWorkflowHome.update( resourceWorkflow, pluginWorkflow );

                // if new state have action automatic
                WorkflowService.getInstance(  )
                               .executeActionAutomatic( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE,
                    action.getWorkflow(  ).getId(  ), resourceWorkflow.getExternalParentId(  ) );

                // Remove the Alert
                removeByHistory( alert.getIdResourceHistory(  ), alert.getIdTask(  ) );
            }
        }
    }

    // PRIVATE METHODS

    /**
     * Get the record field value
     * @param nPosition the position of the entry
     * @param nIdRecord the id record
     * @param nIdDirectory the id directory
     * @return the record field value
     */
    private String getRecordFieldValue( int nPosition, int nIdRecord, int nIdDirectory )
    {
        String strRecordFieldValue = StringUtils.EMPTY;
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        // RecordField
        EntryFilter entryFilter = new EntryFilter(  );
        entryFilter.setPosition( nPosition );
        entryFilter.setIdDirectory( nIdDirectory );

        List<IEntry> listEntries = EntryHome.getEntryList( entryFilter, pluginDirectory );

        if ( ( listEntries != null ) && !listEntries.isEmpty(  ) )
        {
            IEntry entry = listEntries.get( 0 );
            RecordFieldFilter recordFieldFilterEmail = new RecordFieldFilter(  );
            recordFieldFilterEmail.setIdDirectory( nIdDirectory );
            recordFieldFilterEmail.setIdEntry( entry.getIdEntry(  ) );
            recordFieldFilterEmail.setIdRecord( nIdRecord );

            List<RecordField> listRecordFields = RecordFieldHome.getRecordFieldList( recordFieldFilterEmail,
                    pluginDirectory );

            if ( ( listRecordFields != null ) && !listRecordFields.isEmpty(  ) && ( listRecordFields.get( 0 ) != null ) )
            {
                RecordField recordFieldIdDemand = listRecordFields.get( 0 );
                strRecordFieldValue = recordFieldIdDemand.getValue(  );

                if ( recordFieldIdDemand.getField(  ) != null )
                {
                    strRecordFieldValue = recordFieldIdDemand.getField(  ).getTitle(  );
                }
            }
        }

        return strRecordFieldValue;
    }

    /**
     * Build the reference entry into String
     * @param entry the entry
     * @param locale the Locale
     * @return the reference entry
     */
    private String buildReferenceEntryToString( IEntry entry, Locale locale )
    {
        StringBuilder sbReferenceEntry = new StringBuilder(  );
        sbReferenceEntry.append( entry.getPosition(  ) );
        sbReferenceEntry.append( AlertConstants.SPACE + AlertConstants.OPEN_BRACKET );
        sbReferenceEntry.append( entry.getTitle(  ) );
        sbReferenceEntry.append( AlertConstants.SPACE + AlertConstants.HYPHEN + AlertConstants.SPACE );
        sbReferenceEntry.append( I18nService.getLocalizedString( entry.getEntryType(  ).getTitleI18nKey(  ), locale ) );
        sbReferenceEntry.append( AlertConstants.CLOSED_BRACKET );

        return sbReferenceEntry.toString(  );
    }

    /**
     * Fill the list of entry types
     * @param strPropertyEntryTypes the property containing the entry types
     * @return a list of integer
     */
    private static List<Integer> fillListEntryTypes( String strPropertyEntryTypes )
    {
        List<Integer> listEntryTypes = new ArrayList<Integer>(  );
        String strEntryTypes = AppPropertiesService.getProperty( strPropertyEntryTypes );

        if ( StringUtils.isNotBlank( strEntryTypes ) )
        {
            String[] listAcceptEntryTypesForIdDemand = strEntryTypes.split( AlertConstants.COMMA );

            for ( String strAcceptEntryType : listAcceptEntryTypesForIdDemand )
            {
                if ( StringUtils.isNotBlank( strAcceptEntryType ) && StringUtils.isNumeric( strAcceptEntryType ) )
                {
                    int nAcceptedEntryType = Integer.parseInt( strAcceptEntryType );
                    listEntryTypes.add( nAcceptedEntryType );
                }
            }
        }

        return listEntryTypes;
    }
}
