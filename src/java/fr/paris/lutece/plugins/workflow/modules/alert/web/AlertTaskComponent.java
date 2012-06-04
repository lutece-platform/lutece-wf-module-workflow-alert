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
package fr.paris.lutece.plugins.workflow.modules.alert.web;

import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.modules.alert.business.TaskAlertConfig;
import fr.paris.lutece.plugins.workflow.modules.alert.business.retrieval.IRetrievalType;
import fr.paris.lutece.plugins.workflow.modules.alert.business.retrieval.RetrievalTypeFactory;
import fr.paris.lutece.plugins.workflow.modules.alert.service.IAlertService;
import fr.paris.lutece.plugins.workflow.modules.alert.service.ITaskAlertConfigService;
import fr.paris.lutece.plugins.workflow.modules.alert.util.constants.AlertConstants;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.service.action.IActionService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.web.task.NoFormTaskComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * AlertTaskComponent
 *
 */
public class AlertTaskComponent extends NoFormTaskComponent
{
    private static final String TEMPLATE_TASK_ALERT_CONFIG = "admin/plugins/workflow/modules/alert/task_alert_config.html";
    @Inject
    private ITaskAlertConfigService _taskAlertConfigService;
    @Inject
    private IActionService _actionService;
    @Inject
    private IAlertService _alertService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strError = checkAlertConfigParameter( request, locale, task );

        if ( StringUtils.isBlank( strError ) )
        {
            // Fetch parameters
            String strIdDirectory = request.getParameter( AlertConstants.PARAMETER_ID_DIRECTORY );
            String strPositionEntryDirectoryDate = request.getParameter( AlertConstants.PARAMETER_POSITION_ENTRY_DIRECTORY_DATE );
            String strIdStateAfterDeadline = request.getParameter( AlertConstants.PARAMETER_ID_STATE_AFTER_DEADLINE );
            String strNbDaysToDate = request.getParameter( AlertConstants.PARAMETER_NB_DAYS_TO_DATE );
            String strIdRetrievalType = request.getParameter( AlertConstants.PARAMETER_RETRIEVAL_TYPE );

            int nIdDirectory = DirectoryUtils.convertStringToInt( strIdDirectory );
            int nPositionEntryDirectoryDate = DirectoryUtils.convertStringToInt( strPositionEntryDirectoryDate );
            int nIdStateAfterDeadline = DirectoryUtils.convertStringToInt( strIdStateAfterDeadline );
            int nNbDaysToDate = Integer.parseInt( strNbDaysToDate );
            int nIdRetrievalType = DirectoryUtils.convertStringToInt( strIdRetrievalType );

            // In case there are no errors, then the config is created/updated
            boolean bCreate = false;
            TaskAlertConfig config = _taskAlertConfigService.findByPrimaryKey( task.getId(  ) );

            if ( config == null )
            {
                config = new TaskAlertConfig(  );
                config.setIdTask( task.getId(  ) );
                bCreate = true;
            }

            config.setIdDirectory( nIdDirectory );
            config.setPositionEntryDirectoryDate( nPositionEntryDirectoryDate );
            config.setIdStateAfterDeadline( nIdStateAfterDeadline );
            config.setNbDaysToDate( nNbDaysToDate );
            config.setIdRetrievalType( nIdRetrievalType );

            if ( bCreate )
            {
                _taskAlertConfigService.create( config );
            }
            else
            {
                _taskAlertConfigService.update( config );
            }
        }

        return strError;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( AlertConstants.MARK_CONFIG, _taskAlertConfigService.findByPrimaryKey( task.getId(  ) ) );
        model.put( AlertConstants.MARK_LIST_STATES, _alertService.getListStates( task.getAction(  ).getId(  ) ) );
        model.put( AlertConstants.MARK_LIST_DIRECTORIES, _alertService.getListDirectories(  ) );
        model.put( AlertConstants.MARK_LIST_ENTRIES_DATE,
            _alertService.getListEntriesDate( task.getId(  ), request.getLocale(  ) ) );
        model.put( AlertConstants.MARK_RETRIEVAL_TYPES, RetrievalTypeFactory.getRetrievalTypes(  ) );
        model.put( AlertConstants.MARK_LOCALE, locale );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_ALERT_CONFIG, locale, model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    /**
    * Check if the config is well configured
    * @param request the HTTP request
    * @param locale the Locale
    * @param task the task
    * @return null if it is well configured, the label of the field that is not well configured otherwise
    */
    private String checkAlertConfigParameter( HttpServletRequest request, Locale locale, ITask task )
    {
        String strError = null;

        // Fetch parameters
        String strIdDirectory = request.getParameter( AlertConstants.PARAMETER_ID_DIRECTORY );
        String strIdStateAfterDeadline = request.getParameter( AlertConstants.PARAMETER_ID_STATE_AFTER_DEADLINE );
        String strNbDaysToDate = request.getParameter( AlertConstants.PARAMETER_NB_DAYS_TO_DATE );
        String strApply = request.getParameter( AlertConstants.PARAMETER_APPLY );
        String strIdRetrievalType = request.getParameter( AlertConstants.PARAMETER_RETRIEVAL_TYPE );

        int nIdDirectory = DirectoryUtils.convertStringToInt( strIdDirectory );
        int nIdStateAfterDeadline = DirectoryUtils.convertStringToInt( strIdStateAfterDeadline );
        int nIdRetrievalType = DirectoryUtils.convertStringToInt( strIdRetrievalType );

        // Check if the AdminUser clicked on "Apply" or on "Save"
        if ( StringUtils.isEmpty( strApply ) )
        {
            // Check the required fields
            String strRequiredField = StringUtils.EMPTY;

            if ( nIdDirectory == DirectoryUtils.CONSTANT_ID_NULL )
            {
                strRequiredField = AlertConstants.PROPERTY_LABEL_DIRECTORY;
            }
            else if ( StringUtils.isBlank( strIdRetrievalType ) || !StringUtils.isNumeric( strIdRetrievalType ) )
            {
                strRequiredField = AlertConstants.PROPERTY_LABEL_RETRIEVAL_TYPE;
            }
            else if ( nIdStateAfterDeadline == DirectoryUtils.CONSTANT_ID_NULL )
            {
                strRequiredField = AlertConstants.PROPERTY_LABEL_ID_STATE_AFTER_DEADLINE;
            }
            else if ( StringUtils.isBlank( strNbDaysToDate ) )
            {
                strRequiredField = AlertConstants.PROPERTY_LABEL_NB_DAYS_TO_DATE;
            }

            try
            {
                Integer.parseInt( strNbDaysToDate );
            }
            catch ( NumberFormatException e )
            {
                strRequiredField = AlertConstants.PROPERTY_LABEL_NB_DAYS_TO_DATE;

                Object[] tabRequiredFields = { I18nService.getLocalizedString( strRequiredField, locale ) };

                return AdminMessageService.getMessageUrl( request, AlertConstants.MESSAGE_ERROR_INVALID_NUMBER,
                    tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            if ( StringUtils.isBlank( strRequiredField ) )
            {
                IRetrievalType retrievalType = RetrievalTypeFactory.getRetrievalType( nIdRetrievalType );

                if ( retrievalType == null )
                {
                    strRequiredField = AlertConstants.PROPERTY_LABEL_RETRIEVAL_TYPE;
                }
                else
                {
                    strRequiredField = retrievalType.checkConfigData( request );
                }
            }

            if ( StringUtils.isNotBlank( strRequiredField ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( strRequiredField, locale ) };
                strError = AdminMessageService.getMessageUrl( request, AlertConstants.MESSAGE_MANDATORY_FIELD,
                        tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            if ( StringUtils.isBlank( strError ) )
            {
                Action action = _actionService.findByPrimaryKey( task.getAction(  ).getId(  ) );

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
