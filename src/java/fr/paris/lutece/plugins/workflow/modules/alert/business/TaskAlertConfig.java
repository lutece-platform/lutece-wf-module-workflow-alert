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


/**
 *
 * TaskAlertConfig
 *
 */
public class TaskAlertConfig
{
    private int _nIdTask;
    private int _nIdDirectory;
    private int _nIdStateAfterDeadline;
    private int _nPositionEntryDirectoryDate;
    private int _nNbDaysToDate;
    private boolean _bUseCreationDate;

    /**
     * Get the ID task
     * @return id Task
     */
    public int getIdTask(  )
    {
        return _nIdTask;
    }

    /**
     * Set id Task
     * @param nIdTask id task
     */
    public void setIdTask( int nIdTask )
    {
        _nIdTask = nIdTask;
    }

    /**
     * Set the id directory
     * @param nIdDirectory the id directory
     */
    public void setIdDirectory( int nIdDirectory )
    {
        _nIdDirectory = nIdDirectory;
    }

    /**
     * Get the id directory
     * @return the id directory
     */
    public int getIdDirectory(  )
    {
        return _nIdDirectory;
    }

    /**
     * Set the id state after deadline
     * @param nIdStateAfterDeadline the id state after deadline
     */
    public void setIdStateAfterDeadline( int nIdStateAfterDeadline )
    {
        _nIdStateAfterDeadline = nIdStateAfterDeadline;
    }

    /**
     * Get the id state after deadline
     * @return the id state after deadline
     */
    public int getIdStateAfterDeadline(  )
    {
        return _nIdStateAfterDeadline;
    }

    /**
     * Set the position of the entry directory for the date
     * @param nPositionEntryDirectoryDate the position
     */
    public void setPositionEntryDirectoryDate( int nPositionEntryDirectoryDate )
    {
        _nPositionEntryDirectoryDate = nPositionEntryDirectoryDate;
    }

    /**
     * Get the position of the entry directory for the date
     * @return the position
     */
    public int getPositionEntryDirectoryDate(  )
    {
        return _nPositionEntryDirectoryDate;
    }

    /**
     * Set the number of days to date
     * @param nNbDaysToDate the number of days
     */
    public void setNbDaysToDate( int nNbDaysToDate )
    {
        _nNbDaysToDate = nNbDaysToDate;
    }

    /**
     * Get the number of days
     * @return the number of days
     */
    public int getNbDaysToDate(  )
    {
        return _nNbDaysToDate;
    }

    /**
     * Set true if the date is the creation date
     * @param bUseCreationDate true if the date is the creation date, false otherwise
     */
	public void setUseCreationDate( boolean bUseCreationDate )
	{
		_bUseCreationDate = bUseCreationDate;
	}

	/**
	 * True if the date is the creation date, false otherwise
	 * @return true if the date is the creation date, false otherwise
	 */
	public boolean isUseCreationDate()
	{
		return _bUseCreationDate;
	}
}
