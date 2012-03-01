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
package fr.paris.lutece.plugins.workflow.modules.alert.business.retrieval;

import fr.paris.lutece.plugins.workflow.modules.alert.service.AlertPlugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * RetrievalTypeFactory
 *
 */
public final class RetrievalTypeFactory
{
    private static final String BEAN_RETRIEVAL_TYPE_FACTORY = "workflow-alert.retrievalTypeFactory";
    private Map<String, IRetrievalType> _mapRetrievalTypes;

    /**
     * Private constructor
     */
    private RetrievalTypeFactory(  )
    {
    }

    /**
     * Get the factory
     * @return the factory
     */
    public static RetrievalTypeFactory getFactory(  )
    {
        return (RetrievalTypeFactory) SpringContextService.getPluginBean( AlertPlugin.PLUGIN_NAME,
            BEAN_RETRIEVAL_TYPE_FACTORY );
    }

    /**
     * Set the retrieval types
     * @param mapRetrievalTypes the retrieval types
     */
    public void setRetrievalTypes( Map<String, IRetrievalType> mapRetrievalTypes )
    {
        _mapRetrievalTypes = mapRetrievalTypes;
    }

    /**
     * Get the retrieval type
     * @return a map of (id_retrieval_type, {@link IRetrievalType}
     */
    public Map<String, IRetrievalType> getRetrievalTypes(  )
    {
        if ( _mapRetrievalTypes == null )
        {
            init(  );
        }

        return _mapRetrievalTypes;
    }

    /**
     * Get the retrieval type
     * @param nIdRetrievalType the id retrieval type
     * @return a {@link RetrievalType}
     */
    public IRetrievalType getRetrievalType( int nIdRetrievalType )
    {
        if ( _mapRetrievalTypes == null )
        {
            init(  );
        }

        return _mapRetrievalTypes.get( Integer.toString( nIdRetrievalType ) );
    }

    /**
     * Init in case the map is null
     */
    private void init(  )
    {
        _mapRetrievalTypes = new HashMap<String, IRetrievalType>(  );

        for ( IRetrievalType retrievalType : SpringContextService.getBeansOfType( IRetrievalType.class ) )
        {
            _mapRetrievalTypes.put( Integer.toString( retrievalType.getIdType(  ) ), retrievalType );
        }
    }
}
