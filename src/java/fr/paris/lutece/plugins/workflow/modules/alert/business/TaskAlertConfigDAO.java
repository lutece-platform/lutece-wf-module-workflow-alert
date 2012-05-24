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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * TaskAlertConfigDAO
 *
 */
public class TaskAlertConfigDAO implements ITaskAlertConfigDAO
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = " SELECT id_task, id_directory, id_state_after_deadline, position_directory_entry_date, nb_days_to_date, id_retrieval_type " +
        " FROM task_alert_cf  WHERE id_task = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO task_alert_cf( id_task, id_directory, id_state_after_deadline, position_directory_entry_date, nb_days_to_date, id_retrieval_type )" +
        " VALUES ( ?,?,?,?,?,? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE task_alert_cf SET id_directory = ?, id_state_after_deadline = ?, position_directory_entry_date = ?, nb_days_to_date = ?, id_retrieval_type = ? " +
        " WHERE id_task = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM task_alert_cf WHERE id_task = ? ";
    private static final String SQL_QUERY_FIND_ALL = " SELECT id_task, id_directory, id_state_after_deadline, position_directory_entry_date, nb_days_to_date, use_creation_date, use_creation_date " +
        " FROM task_alert_cf ";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( TaskAlertConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, config.getIdTask(  ) );
        daoUtil.setInt( nIndex++, config.getIdDirectory(  ) );
        daoUtil.setInt( nIndex++, config.getIdStateAfterDeadline(  ) );
        daoUtil.setInt( nIndex++, config.getPositionEntryDirectoryDate(  ) );
        daoUtil.setInt( nIndex++, config.getNbDaysToDate(  ) );
        daoUtil.setInt( nIndex++, config.getIdRetrievalType(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( TaskAlertConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, config.getIdDirectory(  ) );
        daoUtil.setInt( nIndex++, config.getIdStateAfterDeadline(  ) );
        daoUtil.setInt( nIndex++, config.getPositionEntryDirectoryDate(  ) );
        daoUtil.setInt( nIndex++, config.getNbDaysToDate(  ) );
        daoUtil.setInt( nIndex++, config.getIdRetrievalType(  ) );

        daoUtil.setInt( nIndex++, config.getIdTask(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskAlertConfig load( int nIdTask, Plugin plugin )
    {
        TaskAlertConfig config = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );

        daoUtil.setInt( 1, nIdTask );

        daoUtil.executeQuery(  );

        int nIndex = 1;

        if ( daoUtil.next(  ) )
        {
            config = new TaskAlertConfig(  );
            config.setIdTask( daoUtil.getInt( nIndex++ ) );
            config.setIdDirectory( daoUtil.getInt( nIndex++ ) );
            config.setIdStateAfterDeadline( daoUtil.getInt( nIndex++ ) );
            config.setPositionEntryDirectoryDate( daoUtil.getInt( nIndex++ ) );
            config.setNbDaysToDate( daoUtil.getInt( nIndex++ ) );
            config.setIdRetrievalType( daoUtil.getInt( nIndex++ ) );
        }

        daoUtil.free(  );

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public List<TaskAlertConfig> loadAll( Plugin plugin )
    {
        List<TaskAlertConfig> configList = new ArrayList<TaskAlertConfig>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL, plugin );

        daoUtil.executeQuery(  );

        int nIndex = 1;

        if ( daoUtil.next(  ) )
        {
            TaskAlertConfig config = new TaskAlertConfig(  );
            config.setIdTask( daoUtil.getInt( nIndex++ ) );
            config.setIdDirectory( daoUtil.getInt( nIndex++ ) );
            config.setIdStateAfterDeadline( daoUtil.getInt( nIndex++ ) );
            config.setPositionEntryDirectoryDate( daoUtil.getInt( nIndex++ ) );
            config.setNbDaysToDate( daoUtil.getInt( nIndex++ ) );
            config.setIdRetrievalType( daoUtil.getInt( nIndex++ ) );
            configList.add( config );
        }

        daoUtil.free(  );

        return configList;
    }
}
