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
package fr.paris.lutece.plugins.workflow.modules.alert.business;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.business.ResourceHistory;
import fr.paris.lutece.plugins.workflow.business.ResourceHistoryHome;
import fr.paris.lutece.plugins.workflow.business.StateHome;
import fr.paris.lutece.plugins.workflow.business.task.Task;
import fr.paris.lutece.plugins.workflow.modules.alert.service.AlertService;
import fr.paris.lutece.plugins.workflow.modules.alert.service.TaskAlertConfigService;
import fr.paris.lutece.plugins.workflow.modules.alert.util.constants.AlertConstants;
import fr.paris.lutece.plugins.workflow.service.WorkflowPlugin;
import fr.paris.lutece.portal.business.workflow.Action;
import fr.paris.lutece.portal.business.workflow.State;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * TaskAlert
 *
 */
public class TaskAlert extends Task
{
    // TEMPLATES
    private static final String TEMPLATE_TASK_ALERT_CONFIG = "admin/plugins/workflow/modules/alert/task_alert_config.html";

    /**
     * {@inheritDoc}
     */
    public void init(  )
    {
    }

    /**
     * {@inheritDoc}
     */
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        ResourceHistory resourceHistory = ResourceHistoryHome.findByPrimaryKey( nIdResourceHistory, plugin );
        TaskAlertConfig config = TaskAlertConfigService.getService(  ).findByPrimaryKey( getId(  ) );

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
                    Alert alert = AlertService.getService(  ).find( nIdResourceHistory, getId(  ) );

                    if ( alert == null )
                    {
                        alert = new Alert(  );
                        alert.setIdResourceHistory( nIdResourceHistory );
                        alert.setIdTask( getId(  ) );
                        AlertService.getService(  ).create( alert );
                    }
                }
            }
        }
    }

    // GET

    /**
     * {@inheritDoc}
     */
    public String getDisplayConfigForm( HttpServletRequest request, Plugin plugin, Locale locale )
    {
        AlertService alertService = AlertService.getService(  );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( AlertConstants.MARK_CONFIG, TaskAlertConfigService.getService(  ).findByPrimaryKey( getId(  ) ) );
        model.put( AlertConstants.MARK_LIST_STATES, alertService.getListStates( getAction(  ).getId(  ) ) );
        model.put( AlertConstants.MARK_LIST_DIRECTORIES, alertService.getListDirectories(  ) );
        model.put( AlertConstants.MARK_LIST_ENTRIES_DATE,
            alertService.getListEntriesDate( getId(  ), request.getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_ALERT_CONFIG, locale, model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request,
        Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ReferenceList getTaskFormEntries( Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getTitle( Plugin plugin, Locale locale )
    {
        String strTitle = StringUtils.EMPTY;
        TaskAlertConfig config = TaskAlertConfigService.getService(  ).findByPrimaryKey( getId(  ) );

        if ( ( config != null ) && ( config.getIdStateAfterDeadline(  ) != DirectoryUtils.CONSTANT_ID_NULL ) )
        {
            Plugin pluginWorkflow = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );
            State state = StateHome.findByPrimaryKey( config.getIdStateAfterDeadline(  ), pluginWorkflow );

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
    public void doRemoveConfig( Plugin plugin )
    {
        TaskAlertConfigService.getService(  ).remove( getId(  ) );
    }

    /**
     * {@inheritDoc}
     */
    public void doRemoveTaskInformation( int nIdHistory, Plugin plugin )
    {
    }

    /**
     * {@inheritDoc}
     */
    public String doSaveConfig( HttpServletRequest request, Locale locale, Plugin plugin )
    {
        TaskAlertConfigService configService = TaskAlertConfigService.getService(  );
        String strError = checkAlertConfigParameter( request, locale );

        if ( StringUtils.isBlank( strError ) )
        {
            // Fetch parameters
            String strIdDirectory = request.getParameter( AlertConstants.PARAMETER_ID_DIRECTORY );
            String strPositionEntryDirectoryDate = request.getParameter( AlertConstants.PARAMETER_POSITION_ENTRY_DIRECTORY_DATE );
            String strIdStateAfterDeadline = request.getParameter( AlertConstants.PARAMETER_ID_STATE_AFTER_DEADLINE );
            String strNbDaysToDate = request.getParameter( AlertConstants.PARAMETER_NB_DAYS_TO_DATE );
            String strUseCreationDate = request.getParameter( AlertConstants.PARAMETER_USE_CREATION_DATE );
            boolean bUseCreationDate = StringUtils.isNotBlank( strUseCreationDate );

            int nIdDirectory = DirectoryUtils.convertStringToInt( strIdDirectory );
            int nPositionEntryDirectoryDate = DirectoryUtils.convertStringToInt( strPositionEntryDirectoryDate );
            int nIdStateAfterDeadline = DirectoryUtils.convertStringToInt( strIdStateAfterDeadline );
            int nNbDaysToDate = Integer.parseInt( strNbDaysToDate );

            // In case there are no errors, then the config is created/updated
            boolean bCreate = false;
            TaskAlertConfig config = configService.findByPrimaryKey( getId(  ) );

            if ( config == null )
            {
                config = new TaskAlertConfig(  );
                config.setIdTask( getId(  ) );
                bCreate = true;
            }

            config.setIdDirectory( nIdDirectory );
            config.setPositionEntryDirectoryDate( nPositionEntryDirectoryDate );
            config.setIdStateAfterDeadline( nIdStateAfterDeadline );
            config.setNbDaysToDate( nNbDaysToDate );
            config.setUseCreationDate( bUseCreationDate );

            if ( bCreate )
            {
                configService.create( config );
            }
            else
            {
                configService.update( config );
            }
        }

        return strError;
    }

    /**
     * {@inheritDoc}
     */
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale,
        Plugin plugin )
    {
        return null;
    }

    // CHECK

    /**
     * {@inheritDoc}
     */
    public boolean isConfigRequire(  )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isFormTaskRequire(  )
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTaskForActionAutomatic(  )
    {
        return true;
    }

    /**
     * Check if the config is well configured
     * @param request the HTTP request
     * @param locale the Locale
     * @return null if it is well configured, the label of the field that is not well configured otherwise
     */
    private String checkAlertConfigParameter( HttpServletRequest request, Locale locale )
    {
        String strError = null;

        // Fetch parameters
        String strIdDirectory = request.getParameter( AlertConstants.PARAMETER_ID_DIRECTORY );
        String strPositionEntryDirectoryDate = request.getParameter( AlertConstants.PARAMETER_POSITION_ENTRY_DIRECTORY_DATE );
        String strIdStateAfterDeadline = request.getParameter( AlertConstants.PARAMETER_ID_STATE_AFTER_DEADLINE );
        String strNbDaysToDate = request.getParameter( AlertConstants.PARAMETER_NB_DAYS_TO_DATE );
        String strApply = request.getParameter( AlertConstants.PARAMETER_APPLY );
        String strUseCreationDate = request.getParameter( AlertConstants.PARAMETER_USE_CREATION_DATE );
        boolean bUseCreationDate = StringUtils.isNotBlank( strUseCreationDate );

        int nIdDirectory = DirectoryUtils.convertStringToInt( strIdDirectory );
        int nPositionEntryDirectoryDate = DirectoryUtils.convertStringToInt( strPositionEntryDirectoryDate );
        int nIdStateAfterDeadline = DirectoryUtils.convertStringToInt( strIdStateAfterDeadline );

        // Check if the AdminUser clicked on "Apply" or on "Save"
        if ( StringUtils.isEmpty( strApply ) )
        {
            // Check the required fields
            String strRequiredField = StringUtils.EMPTY;

            if ( nIdDirectory == DirectoryUtils.CONSTANT_ID_NULL )
            {
                strRequiredField = AlertConstants.PROPERTY_LABEL_DIRECTORY;
            }
            else if ( !bUseCreationDate && ( nPositionEntryDirectoryDate == DirectoryUtils.CONSTANT_ID_NULL ) )
            {
                strRequiredField = AlertConstants.PROPERTY_LABEL_POSITION_ENTRY_DIRECTORY_DATE;
            }
            else if ( nIdStateAfterDeadline == DirectoryUtils.CONSTANT_ID_NULL )
            {
                strRequiredField = AlertConstants.PROPERTY_LABEL_ID_STATE_AFTER_DEADLINE;
            }
            else if ( StringUtils.isBlank( strNbDaysToDate ) )
            {
                strRequiredField = AlertConstants.PROPERTY_LABEL_NB_DAYS_TO_DATE;
            }

            if ( StringUtils.isNotBlank( strRequiredField ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( strRequiredField, locale ) };
                strError = AdminMessageService.getMessageUrl( request, AlertConstants.MESSAGE_MANDATORY_FIELD,
                        tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            if ( StringUtils.isBlank( strError ) )
            {
                Action action = AlertService.getService(  ).getAction( getAction(  ).getId(  ) );

                if ( ( action != null ) && ( action.getStateBefore(  ) != null ) &&
                        ( action.getStateBefore(  ).getId(  ) == nIdStateAfterDeadline ) )
                {
                    strError = AdminMessageService.getMessageUrl( request,
                            AlertConstants.MESSAGE_STATE_AFTER_DEADLINE_SAME_STATE_BEFORE, AdminMessage.TYPE_STOP );
                }
            }
        }

        return strError;
    }
}
