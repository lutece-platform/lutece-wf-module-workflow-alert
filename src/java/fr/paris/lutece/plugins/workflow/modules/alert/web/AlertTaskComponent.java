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

import fr.paris.lutece.plugins.workflow.modules.alert.business.retrieval.RetrievalTypeFactory;
import fr.paris.lutece.plugins.workflow.modules.alert.service.IAlertService;
import fr.paris.lutece.plugins.workflow.modules.alert.util.constants.AlertConstants;
import fr.paris.lutece.plugins.workflow.web.task.NoFormTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

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
    @Named( AlertConstants.BEAN_ALERT_CONFIG_SERVICE )
    private ITaskConfigService _taskAlertConfigService;
    @Inject
    private IAlertService _alertService;

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
}
