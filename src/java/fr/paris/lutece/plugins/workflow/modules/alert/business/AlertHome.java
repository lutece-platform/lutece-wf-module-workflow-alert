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
package fr.paris.lutece.plugins.workflow.modules.alert.business;

import fr.paris.lutece.plugins.workflow.modules.alert.service.AlertPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * AlertHome
 *
 */
public final class AlertHome
{
    private static final String BEAN_ALERT_DAO = "workflow-alert.alertDAO";
    private static Plugin _plugin = PluginService.getPlugin( AlertPlugin.PLUGIN_NAME );
    private static IAlertDAO _dao = (IAlertDAO) SpringContextService.getPluginBean( AlertPlugin.PLUGIN_NAME,
            BEAN_ALERT_DAO );

    /**
     * Private constructor - this class need not be instantiated
     */
    private AlertHome(  )
    {
    }

    /**
     * Creation of an instance of alert
     * @param alert The instance of alert which contains the informations to store
     */
    public static void create( Alert alert )
    {
        _dao.insert( alert, _plugin );
    }

    /**
     * Remove the alert by history
     * @param nIdResourceHistory the history key
     * @param nIdTask The task key
     */
    public static void removeByHistory( int nIdResourceHistory, int nIdTask )
    {
        _dao.deleteByHistory( nIdResourceHistory, nIdTask, _plugin );
    }

    /**
     * Remove alert by task
     * @param nIdTask The task key
     */
    public static void removeByTask( int nIdTask )
    {
        _dao.deleteByTask( nIdTask, _plugin );
    }

    /**
     * Load the Alert Object
     * @param nIdResourceHistory the history id
     * @param nIdTask the task id
     * @return the Config Object
     */
    public static Alert find( int nIdResourceHistory, int nIdTask )
    {
        return _dao.load( nIdResourceHistory, nIdTask, _plugin );
    }

    /**
     * Find all alerts
     * @return a list of Alert
     */
    public static List<Alert> findAll(  )
    {
        return _dao.selectAll( _plugin );
    }
}
