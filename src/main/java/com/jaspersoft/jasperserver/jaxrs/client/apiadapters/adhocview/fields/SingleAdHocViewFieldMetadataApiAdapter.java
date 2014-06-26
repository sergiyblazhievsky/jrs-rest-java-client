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

package com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhocview.fields;

import com.jaspersoft.jasperserver.dto.adhocview.metadata.ClientFieldMetadata;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.AbstractAdapter;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.adhocview.AdHocViewParameter;
import com.jaspersoft.jasperserver.jaxrs.client.core.JerseyRequest;
import com.jaspersoft.jasperserver.jaxrs.client.core.MimeTypeUtil;
import com.jaspersoft.jasperserver.jaxrs.client.core.SessionStorage;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SingleAdHocViewFieldMetadataApiAdapter extends AbstractAdapter {

    private final MultivaluedMap<String, String> params;
    private final String adHocViewUri;
    private final String id;

    public SingleAdHocViewFieldMetadataApiAdapter(SessionStorage sessionStorage, String adHocViewUri, String id) {
        super(sessionStorage);
        this.adHocViewUri = adHocViewUri;
        this.id = id;
        params = new MultivaluedHashMap<String, String>();
    }

    public SingleAdHocViewFieldMetadataApiAdapter parameter(AdHocViewParameter parameter, String value){
        params.add(parameter.getParamName(), value);
        return this;
    }

    public OperationResult<ClientFieldMetadata> get() {
        JerseyRequest<ClientFieldMetadata> request = JerseyRequest.buildRequest(sessionStorage,
                ClientFieldMetadata.class,
                new String[]{"/resources", adHocViewUri, "/fields", id});

        String acceptMime = MimeTypeUtil.toCorrectAcceptMime(sessionStorage.getConfiguration(), "application/adhocDataView.field+{mime}");
        request.setAccept(acceptMime);
        request.addParams(params);

        return request.get();
    }

}
