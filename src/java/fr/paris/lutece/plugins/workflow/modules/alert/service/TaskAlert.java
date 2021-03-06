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

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.modules.alert.business.Alert;
import fr.paris.lutece.plugins.workflow.modules.alert.business.TaskAlertConfig;
import fr.paris.lutece.plugins.workflow.modules.alert.util.constants.AlertConstants;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.state.IStateService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * TaskAlert
 *
 */
public class TaskAlert extends SimpleTask
{
    // TEMPLATES
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    @Named( AlertConstants.BEAN_ALERT_CONFIG_SERVICE )
    private ITaskConfigService _taskAlertConfigService;
    @Inject
    private IAlertService _alertService;
    @Inject
    private IStateService _stateService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        TaskAlertConfig config = _taskAlertConfigService.findByPrimaryKey( getId(  ) );

        if ( ( config != null ) && ( resourceHistory != null ) &&
                Record.WORKFLOW_RESOURCE_TYPE.equals( resourceHistory.getResourceType(  ) ) )
        {
            Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

            // Record
            Record record = RecordHome.findByPrimaryKey( resourceHistory.getIdResource(  ), pluginDirectory );

            if ( record != null )
            {
                Directory directory = DirectoryHome.findByPrimaryKey( record.getDirectory(  ).getIdDirectory(  ),
                        pluginDirectory );

                if ( directory != null )
                {
                    Alert alert = _alertService.find( nIdResourceHistory, getId(  ) );

                    if ( alert == null )
                    {
                        Long lDate = config.getDate( record );
                        alert = new Alert(  );
                        alert.setIdResourceHistory( nIdResourceHistory );
                        alert.setIdTask( getId(  ) );
                        alert.setDateReference( new Timestamp( lDate ) );
                        _alertService.create( alert );
                    }
                }
            }
        }
    }

    // GET

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        String strTitle = StringUtils.EMPTY;
        TaskAlertConfig config = _taskAlertConfigService.findByPrimaryKey( getId(  ) );

        if ( ( config != null ) && ( config.getIdStateAfterDeadline(  ) != DirectoryUtils.CONSTANT_ID_NULL ) )
        {
            State state = _stateService.findByPrimaryKey( config.getIdStateAfterDeadline(  ) );

            if ( state != null )
            {
                strTitle = state.getName(  );
            }
        }

        return strTitle;
    }

    // DO

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveConfig(  )
    {
        _taskAlertConfigService.remove( getId(  ) );
    }
}
