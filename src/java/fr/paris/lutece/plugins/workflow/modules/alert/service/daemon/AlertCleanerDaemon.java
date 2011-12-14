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
package fr.paris.lutece.plugins.workflow.modules.alert.service.daemon;

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.workflow.modules.alert.business.Alert;
import fr.paris.lutece.plugins.workflow.modules.alert.business.TaskAlertConfig;
import fr.paris.lutece.plugins.workflow.modules.alert.service.AlertService;
import fr.paris.lutece.plugins.workflow.modules.alert.service.TaskAlertConfigService;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.i18n.I18nService;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;


/**
 *
 * Daemon AlertCleanerDaemon
 *
 */
public class AlertCleanerDaemon extends Daemon
{
    /**
     * Daemon's treatment method
     */
    public void run(  )
    {
        StringBuilder sbLog = new StringBuilder(  );
        TaskAlertConfigService configService = TaskAlertConfigService.getService(  );
        AlertService alertService = AlertService.getService(  );

        for ( Alert alert : alertService.findAll(  ) )
        {
            Record record = alertService.getRecord( alert );
            TaskAlertConfig config = configService.findByPrimaryKey( alert.getIdTask(  ) );

            Locale locale = I18nService.getDefaultLocale(  );

            if ( ( record != null ) && ( config != null ) &&
                    !alertService.isRecordStateValid( config, record, locale ) )
            {
                sbLog.append( "\n- Cleaning alert (ID record : " + record.getIdRecord(  ) + ", ID history : " +
                    alert.getIdResourceHistory(  ) + ", ID task : " + alert.getIdTask(  ) + ")" );
                // Remove the Alert
                alertService.removeByHistory( alert.getIdResourceHistory(  ), alert.getIdTask(  ) );
            }
        }

        if ( StringUtils.isBlank( sbLog.toString(  ) ) )
        {
            sbLog.append( "\nNo alert to clean." );
        }

        setLastRunLogs( sbLog.toString(  ) );
    }
}
