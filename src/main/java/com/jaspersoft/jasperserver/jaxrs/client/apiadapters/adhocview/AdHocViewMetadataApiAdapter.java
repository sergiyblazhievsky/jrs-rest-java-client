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

import com.jaspersoft.jasperserver.dto.adhocview.ClientAdHocViewMetadata;
import com.jaspersoft.jasperserver.dto.adhocview.metadata.ClientFieldMetadata;
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

public class AdHocViewMetadataApiAdapter extends AbstractAdapter {

    private final String adHocViewUri;

    public AdHocViewMetadataApiAdapter(SessionStorage sessionStorage, String adHocViewUri) {
        super(sessionStorage);
        this.adHocViewUri = adHocViewUri;
    }

    public OperationResult<ClientAdHocViewMetadata> get() {
        JerseyRequest<ClientAdHocViewMetadata> request = JerseyRequest.buildRequest(sessionStorage,
                ClientAdHocViewMetadata.class,
                new String[]{"/resources", adHocViewUri, "/view"});

        String acceptMime = MimeTypeUtil.toCorrectAcceptMime(sessionStorage.getConfiguration(), "application/adhocDataView.view+{mime}");
        request.setAccept(acceptMime);

        return request.get();
    }

    public OperationResult<ClientAdHocViewMetadata> update(ClientAdHocViewMetadata adHocViewMetadata) {
        JerseyRequest<ClientAdHocViewMetadata> request = JerseyRequest.buildRequest(sessionStorage,
                ClientAdHocViewMetadata.class,
                new String[]{"/resources", adHocViewUri, "/view"});

        String contentMime = MimeTypeUtil.toCorrectAcceptMime(sessionStorage.getConfiguration(), "application/adhocDataView.view+{mime}");
        String acceptMime = MimeTypeUtil.toCorrectAcceptMime(sessionStorage.getConfiguration(), "application/adhocDataView.view+{mime}");

        request.setContentType(contentMime);
        request.setAccept(acceptMime);

        return request.put(adHocViewMetadata);
    }

    public OperationResult<List<ClientFieldMetadata>> fields() {
        JerseyRequest<List> request = JerseyRequest.buildRequest(sessionStorage,
                List.class,
                new String[]{"/resources", adHocViewUri, "/fields"});

        String acceptMime = MimeTypeUtil.toCorrectAcceptMime(sessionStorage.getConfiguration(), "application/adhocDataView.field+{mime}");
        request.setAccept(acceptMime);

        OperationResult<List> operationResult = request.get();
        OperationResult<List<ClientFieldMetadata>> result;
        if (!(operationResult instanceof NullEntityOperationResult))
            result = new GenericEntityOperationResult<List<ClientFieldMetadata>>(operationResult.getResponse(), new GenericType<List<ClientFieldMetadata>>(){});
        else
            result = new NullEntityOperationResult<List<ClientFieldMetadata>>(operationResult.getResponse());

        return result;
    }

    public OperationResult<ClientFieldMetadata> field(String id) {
        JerseyRequest<ClientFieldMetadata> request = JerseyRequest.buildRequest(sessionStorage,
                ClientFieldMetadata.class,
                new String[]{"/resources", adHocViewUri, "/fields", id});

        String acceptMime = MimeTypeUtil.toCorrectAcceptMime(sessionStorage.getConfiguration(), "application/adhocDataView.field+{mime}");
        request.setAccept(acceptMime);

        return request.get();
    }

    public OperationResult<List<ClientFilterMetadata>> filters() {
        JerseyRequest<List> request = JerseyRequest.buildRequest(sessionStorage,
                List.class,
                new String[]{"/resources", adHocViewUri, "/filters"});

        String acceptMime = MimeTypeUtil.toCorrectAcceptMime(sessionStorage.getConfiguration(), "application/adhocDataView.filter+{mime}");
        request.setAccept(acceptMime);

        OperationResult<List> operationResult = request.get();
        OperationResult<List<ClientFilterMetadata>> result;
        if (!(operationResult instanceof NullEntityOperationResult))
            result = new GenericEntityOperationResult<List<ClientFilterMetadata>>(operationResult.getResponse(), new GenericType<List<ClientFilterMetadata>>(){});
        else
            result = new NullEntityOperationResult<List<ClientFilterMetadata>>(operationResult.getResponse());

        return result;
    }

    public AdHocViewFilterMetadataAdapter filter(String id) {
        return new AdHocViewFilterMetadataAdapter(sessionStorage, adHocViewUri, id);
    }

}
