/*
 * Copyright (c) 2002-2013, Mairie de Paris
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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * AlertDAO
 *
 */
public class AlertDAO implements IAlertDAO
{
    private static final String SQL_QUERY_SELECT_BY_PRIMARY_KEY = " SELECT id_history, id_task, reference_date " +
        " FROM task_alert WHERE id_history = ? AND id_task = ?";
    private static final String SQL_QUERY_INSERT = " INSERT INTO task_alert ( id_history, id_task, reference_date ) VALUES ( ?,?,? ) ";
    private static final String SQL_QUERY_DELETE_BY_HISTORY = " DELETE FROM task_alert WHERE id_history = ? AND id_task = ? ";
    private static final String SQL_QUERY_DELETE_BY_TASK = " DELETE FROM task_alert WHERE id_task = ? ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_history, id_task, reference_date FROM task_alert ";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( Alert alertValue, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, alertValue.getIdResourceHistory(  ) );
        daoUtil.setInt( nIndex++, alertValue.getIdTask(  ) );
        daoUtil.setTimestamp( nIndex++, alertValue.getDateReference(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Alert load( int nIdResourceHistory, int nIdTask, Plugin plugin )
    {
        Alert alert = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PRIMARY_KEY, plugin );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, nIdResourceHistory );
        daoUtil.setInt( nIndex++, nIdTask );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nIndex = 1;
            alert = new Alert(  );
            alert.setIdResourceHistory( daoUtil.getInt( nIndex++ ) );
            alert.setIdTask( daoUtil.getInt( nIndex++ ) );
            alert.setDateReference( daoUtil.getTimestamp( nIndex++ ) );
        }

        daoUtil.free(  );

        return alert;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByHistory( int nIdResourceHistory, int nIdTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_HISTORY, plugin );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, nIdResourceHistory );
        daoUtil.setInt( nIndex++, nIdTask );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByTask( int nIdTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_TASK, plugin );
        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Alert> selectAll( Plugin plugin )
    {
        List<Alert> listAlerts = new ArrayList<Alert>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, plugin );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            Alert alert = new Alert(  );
            alert.setIdResourceHistory( daoUtil.getInt( nIndex++ ) );
            alert.setIdTask( daoUtil.getInt( nIndex++ ) );
            alert.setDateReference( daoUtil.getTimestamp( nIndex++ ) );
            listAlerts.add( alert );
        }

        daoUtil.free(  );

        return listAlerts;
    }
}
