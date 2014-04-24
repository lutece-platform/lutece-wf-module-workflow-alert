/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
import fr.paris.lutece.plugins.workflow.modules.alert.business.Alert;
import fr.paris.lutece.plugins.workflow.modules.alert.business.IAlertDAO;
import fr.paris.lutece.plugins.workflow.modules.alert.business.TaskAlertConfig;
import fr.paris.lutece.plugins.workflow.modules.alert.util.constants.AlertConstants;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceWorkflow;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.business.state.StateFilter;
import fr.paris.lutece.plugins.workflowcore.service.action.IActionService;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceWorkflowService;
import fr.paris.lutece.plugins.workflowcore.service.state.IStateService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.ITaskService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * AlertService
 * 
 */
public final class AlertService implements IAlertService
{
    public static final String BEAN_SERVICE = "workflow-alert.alertService";
    private List<Integer> _listAcceptedEntryTypesDate;
    @Inject
    private ITaskService _taskService;
    @Inject
    private IStateService _stateService;
    @Inject
    private IResourceWorkflowService _resourceWorkflowService;
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    private IActionService _actionService;
    @Inject
    @Named( AlertConstants.BEAN_ALERT_CONFIG_SERVICE )
    private ITaskConfigService _taskAlertConfigService;
    @Inject
    private IAlertDAO _alertDAO;

    /**
     * Private constructor
     */
    private AlertService( )
    {
        // Init list accepted entry types for date
        _listAcceptedEntryTypesDate = fillListEntryTypes( AlertConstants.PROPERTY_ACCEPTED_DIRECTORY_ENTRY_TYPES_DATE );
    }

    // CRUD

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( AlertPlugin.BEAN_TRANSACTION_MANAGER )
    public void create( Alert alert )
    {
        if ( alert != null )
        {
            _alertDAO.insert( alert, PluginService.getPlugin( AlertPlugin.PLUGIN_NAME ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( AlertPlugin.BEAN_TRANSACTION_MANAGER )
    public void removeByHistory( int nIdResourceHistory, int nIdTask )
    {
        _alertDAO.deleteByHistory( nIdResourceHistory, nIdTask, PluginService.getPlugin( AlertPlugin.PLUGIN_NAME ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( AlertPlugin.BEAN_TRANSACTION_MANAGER )
    public void removeByTask( int nIdTask )
    {
        _alertDAO.deleteByTask( nIdTask, PluginService.getPlugin( AlertPlugin.PLUGIN_NAME ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Alert find( int nIdResourceHistory, int nIdTask )
    {
        return _alertDAO.load( nIdResourceHistory, nIdTask, PluginService.getPlugin( AlertPlugin.PLUGIN_NAME ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Alert> findAll( )
    {
        return _alertDAO.selectAll( PluginService.getPlugin( AlertPlugin.PLUGIN_NAME ) );
    }

    // CHECKS

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEntryTypeDateAccepted( int nIdEntryType )
    {
        boolean bIsAccepted = false;

        if ( ( _listAcceptedEntryTypesDate != null ) && !_listAcceptedEntryTypesDate.isEmpty( ) )
        {
            bIsAccepted = _listAcceptedEntryTypesDate.contains( nIdEntryType );
        }

        return bIsAccepted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRecordStateValid( TaskAlertConfig config, Record record, Locale locale )
    {
        boolean bIsValid = false;

        ITask task = _taskService.findByPrimaryKey( config.getIdTask( ), locale );

        if ( task != null )
        {
            Action action = _actionService.findByPrimaryKey( task.getAction( ).getId( ) );

            if ( ( action != null ) && ( action.getStateAfter( ) != null ) )
            {
                ResourceWorkflow resourceWorkflow = _resourceWorkflowService.findByPrimaryKey( record.getIdRecord( ),
                        Record.WORKFLOW_RESOURCE_TYPE, action.getWorkflow( ).getId( ) );

                if ( ( resourceWorkflow != null ) && ( resourceWorkflow.getState( ) != null )
                        && ( resourceWorkflow.getState( ).getId( ) == action.getStateAfter( ).getId( ) ) )
                {
                    bIsValid = true;
                }
            }
        }

        return bIsValid;
    }

    // GET

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IEntry> getListEntries( int nIdTask )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        TaskAlertConfig config = _taskAlertConfigService.findByPrimaryKey( nIdTask );

        List<IEntry> listEntries = new ArrayList<IEntry>( );

        if ( config != null )
        {
            EntryFilter entryFilter = new EntryFilter( );
            entryFilter.setIdDirectory( config.getIdDirectory( ) );

            listEntries = EntryHome.getEntryList( entryFilter, pluginDirectory );
        }

        return listEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getListEntriesDate( int nIdTask, Locale locale )
    {
        ReferenceList refenreceListEntries = new ReferenceList( );
        refenreceListEntries.addItem( DirectoryUtils.CONSTANT_ID_NULL, DirectoryUtils.EMPTY_STRING );

        for ( IEntry entry : getListEntries( nIdTask ) )
        {
            int nIdEntryType = entry.getEntryType( ).getIdType( );

            if ( isEntryTypeDateAccepted( nIdEntryType ) )
            {
                refenreceListEntries.addItem( entry.getPosition( ), buildReferenceEntryToString( entry, locale ) );
            }
        }

        return refenreceListEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getListDirectories( )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        ReferenceList listDirectories = DirectoryHome.getDirectoryList( pluginDirectory );
        ReferenceList refenreceListDirectories = new ReferenceList( );
        refenreceListDirectories.addItem( DirectoryUtils.CONSTANT_ID_NULL, StringUtils.EMPTY );

        if ( listDirectories != null )
        {
            refenreceListDirectories.addAll( listDirectories );
        }

        return refenreceListDirectories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getListStates( int nIdAction )
    {
        ReferenceList referenceListStates = new ReferenceList( );
        Action action = _actionService.findByPrimaryKey( nIdAction );

        if ( ( action != null ) && ( action.getWorkflow( ) != null ) )
        {
            StateFilter stateFilter = new StateFilter( );
            stateFilter.setIdWorkflow( action.getWorkflow( ).getId( ) );

            List<State> listStates = _stateService.getListStateByFilter( stateFilter );

            referenceListStates.addItem( DirectoryUtils.CONSTANT_ID_NULL, StringUtils.EMPTY );
            referenceListStates.addAll( ReferenceList
                    .convert( listStates, AlertConstants.ID, AlertConstants.NAME, true ) );
        }

        return referenceListStates;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Record getRecord( Alert alert )
    {
        Record record = null;

        if ( alert != null )
        {
            Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
            ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( alert.getIdResourceHistory( ) );

            if ( ( resourceHistory != null )
                    && Record.WORKFLOW_RESOURCE_TYPE.equals( resourceHistory.getResourceType( ) ) )
            {
                record = RecordHome.findByPrimaryKey( resourceHistory.getIdResource( ), pluginDirectory );
            }
        }

        return record;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getDate( TaskAlertConfig config, int nIdRecord, int nIdDirectory )
    {
        long lDate = 0;

        if ( config != null )
        {
            String strDate = getRecordFieldValue( config.getPositionEntryDirectoryDate( ), nIdRecord, nIdDirectory );

            if ( StringUtils.isNotBlank( strDate ) )
            {
                lDate = Long.parseLong( strDate );
            }
        }

        return lDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordFieldValue( int nPosition, int nIdRecord, int nIdDirectory )
    {
        String strRecordFieldValue = StringUtils.EMPTY;
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        // RecordField
        EntryFilter entryFilter = new EntryFilter( );
        entryFilter.setPosition( nPosition );
        entryFilter.setIdDirectory( nIdDirectory );

        List<IEntry> listEntries = EntryHome.getEntryList( entryFilter, pluginDirectory );

        if ( ( listEntries != null ) && !listEntries.isEmpty( ) )
        {
            IEntry entry = listEntries.get( 0 );
            RecordFieldFilter recordFieldFilterEmail = new RecordFieldFilter( );
            recordFieldFilterEmail.setIdDirectory( nIdDirectory );
            recordFieldFilterEmail.setIdEntry( entry.getIdEntry( ) );
            recordFieldFilterEmail.setIdRecord( nIdRecord );

            List<RecordField> listRecordFields = RecordFieldHome.getRecordFieldList( recordFieldFilterEmail,
                    pluginDirectory );

            if ( ( listRecordFields != null ) && !listRecordFields.isEmpty( ) && ( listRecordFields.get( 0 ) != null ) )
            {
                RecordField recordFieldIdDemand = listRecordFields.get( 0 );
                strRecordFieldValue = recordFieldIdDemand.getValue( );

                if ( recordFieldIdDemand.getField( ) != null )
                {
                    strRecordFieldValue = recordFieldIdDemand.getField( ).getTitle( );
                }
            }
        }

        return strRecordFieldValue;
    }

    // ACTIONS

    /**
     * {@inheritDoc}
     */
    @Override
    public void doChangeRecordState( TaskAlertConfig config, int nIdRecord, Alert alert )
    {
        // The locale is not important. It is just used to fetch the task action id
        Locale locale = I18nService.getDefaultLocale( );
        ITask task = _taskService.findByPrimaryKey( config.getIdTask( ), locale );

        if ( task != null )
        {
            State state = _stateService.findByPrimaryKey( config.getIdStateAfterDeadline( ) );
            Action action = _actionService.findByPrimaryKey( task.getAction( ).getId( ) );

            if ( ( state != null ) && ( action != null ) )
            {
                // Create Resource History
                ResourceHistory resourceHistory = new ResourceHistory( );
                resourceHistory.setIdResource( nIdRecord );
                resourceHistory.setResourceType( Record.WORKFLOW_RESOURCE_TYPE );
                resourceHistory.setAction( action );
                resourceHistory.setWorkFlow( action.getWorkflow( ) );
                resourceHistory.setCreationDate( WorkflowUtils.getCurrentTimestamp( ) );
                resourceHistory.setUserAccessCode( AlertConstants.USER_AUTO );
                _resourceHistoryService.create( resourceHistory );

                // Update Resource
                ResourceWorkflow resourceWorkflow = _resourceWorkflowService.findByPrimaryKey( nIdRecord,
                        Record.WORKFLOW_RESOURCE_TYPE, action.getWorkflow( ).getId( ) );
                resourceWorkflow.setState( state );
                _resourceWorkflowService.update( resourceWorkflow );

                // If the new state has automatic reflexive actions
                WorkflowService.getInstance( ).doProcessAutomaticReflexiveActions( nIdRecord,
                        Record.WORKFLOW_RESOURCE_TYPE, state.getId( ), resourceWorkflow.getExternalParentId( ), locale );

                // if new state has action automatic
                WorkflowService.getInstance( ).executeActionAutomatic( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE,
                        action.getWorkflow( ).getId( ), resourceWorkflow.getExternalParentId( ) );

                // Remove the Alert
                removeByHistory( alert.getIdResourceHistory( ), alert.getIdTask( ) );
            }
        }
    }

    // PRIVATE METHODS

    /**
     * Build the reference entry into String
     * @param entry the entry
     * @param locale the Locale
     * @return the reference entry
     */
    private String buildReferenceEntryToString( IEntry entry, Locale locale )
    {
        StringBuilder sbReferenceEntry = new StringBuilder( );
        sbReferenceEntry.append( entry.getPosition( ) );
        sbReferenceEntry.append( AlertConstants.SPACE + AlertConstants.OPEN_BRACKET );
        sbReferenceEntry.append( entry.getTitle( ) );
        sbReferenceEntry.append( AlertConstants.SPACE + AlertConstants.HYPHEN + AlertConstants.SPACE );
        sbReferenceEntry.append( I18nService.getLocalizedString( entry.getEntryType( ).getTitleI18nKey( ), locale ) );
        sbReferenceEntry.append( AlertConstants.CLOSED_BRACKET );

        return sbReferenceEntry.toString( );
    }

    /**
     * Fill the list of entry types
     * @param strPropertyEntryTypes the property containing the entry types
     * @return a list of integer
     */
    private static List<Integer> fillListEntryTypes( String strPropertyEntryTypes )
    {
        List<Integer> listEntryTypes = new ArrayList<Integer>( );
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
