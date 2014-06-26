/*
 * Copyright (C) 2005 - 2014 Jaspersoft Corporation. All rights  reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License  as
 * published by the Free Software Foundation, either version 3 of  the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero  General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public  License
 * along with this program.&nbsp; If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhocview.filters;

import com.jaspersoft.jasperserver.dto.adhocview.metadata.ClientFilterMetadata;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.AbstractAdapter;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhocview.AdHocViewParameter;
import com.jaspersoft.jasperserver.jaxrs.client.core.JerseyRequest;
import com.jaspersoft.jasperserver.jaxrs.client.core.MimeTypeUtil;
import com.jaspersoft.jasperserver.jaxrs.client.core.SessionStorage;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.GenericEntityOperationResult;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.NullEntityOperationResult;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public class BatchAdHocViewFilterMetadataApiAdapter extends AbstractAdapter {

    private final MultivaluedMap<String, String> params;
    private final String adHocViewUri;

    public BatchAdHocViewFilterMetadataApiAdapter(SessionStorage sessionStorage, String adHocViewUri) {
        super(sessionStorage);
        this.adHocViewUri = adHocViewUri;
        params = new MultivaluedHashMap<String, String>();
    }

    public BatchAdHocViewFilterMetadataApiAdapter parameter(AdHocViewParameter parameter, String value){
        params.add(parameter.getParamName(), value);
        return this;
    }

    public OperationResult<List<ClientFilterMetadata>> get() {
        JerseyRequest<List> request = JerseyRequest.buildRequest(sessionStorage,
                List.class,
                new String[]{"/resources", adHocViewUri, "/filters"});

        String acceptMime = MimeTypeUtil.toCorrectAcceptMime(sessionStorage.getConfiguration(), "application/adhocDataView.filter+{mime}");
        request.setAccept(acceptMime);
        request.addParams(params);

        OperationResult<List> operationResult = request.get();
        OperationResult<List<ClientFilterMetadata>> result;
        if (!(operationResult instanceof NullEntityOperationResult))
            result = new GenericEntityOperationResult<List<ClientFilterMetadata>>(operationResult.getResponse(), new GenericType<List<ClientFilterMetadata>>(){});
        else
            result = new NullEntityOperationResult<List<ClientFilterMetadata>>(operationResult.getResponse());

        return result;
    }

}
