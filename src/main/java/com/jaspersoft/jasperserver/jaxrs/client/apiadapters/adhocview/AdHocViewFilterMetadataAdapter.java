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

package com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhocview;

import com.jaspersoft.jasperserver.dto.adhocview.metadata.ClientFilterMetadata;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.AbstractAdapter;
import com.jaspersoft.jasperserver.jaxrs.client.core.JerseyRequest;
import com.jaspersoft.jasperserver.jaxrs.client.core.MimeTypeUtil;
import com.jaspersoft.jasperserver.jaxrs.client.core.SessionStorage;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.GenericEntityOperationResult;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.NullEntityOperationResult;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;

import javax.ws.rs.core.GenericType;
import java.util.List;

public class AdHocViewFilterMetadataAdapter extends AbstractAdapter {

    private final String id;
    private final String adHocViewUri;

    public AdHocViewFilterMetadataAdapter(SessionStorage sessionStorage, String adHocViewUri, String id) {
        super(sessionStorage);
        this.adHocViewUri = adHocViewUri;
        this.id = id;
    }

    public OperationResult<ClientFilterMetadata> get(){
        JerseyRequest<ClientFilterMetadata> request = JerseyRequest.buildRequest(sessionStorage,
                ClientFilterMetadata.class,
                new String[]{"/resources", adHocViewUri, "/filters", id});

        String acceptMime = MimeTypeUtil.toCorrectAcceptMime(sessionStorage.getConfiguration(), "application/adhocDataView.filter+{mime}");
        request.setAccept(acceptMime);

        return request.get();
    }

    public OperationResult<List<String>> values(){
        JerseyRequest<List> request = JerseyRequest.buildRequest(sessionStorage,
                List.class,
                new String[]{"/resources", adHocViewUri, "/filters", id, "/values"});

        OperationResult<List> operationResult = request.get();
        OperationResult<List<String>> result;
        if (!(operationResult instanceof NullEntityOperationResult))
            result = new GenericEntityOperationResult<List<String>>(operationResult.getResponse(), new GenericType<List<String>>(){});
        else
            result = new NullEntityOperationResult<List<String>>(operationResult.getResponse());

        return result;
    }

}
