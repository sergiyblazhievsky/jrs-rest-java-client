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

package com.jaspersoft.jasperserver.jaxrs.client.core.operationresult;

import com.jaspersoft.jasperserver.dto.adhocview.ClientAdHocViewMetadata;
import com.jaspersoft.jasperserver.dto.adhocview.ClientAdHocViewTableMetadata;
import com.jaspersoft.jasperserver.dto.adhocview.ClientClientAdHocViewChartMetadata;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.jasperserver.jaxrs.client.apiadapters.resources.ResourcesTypeResolverUtil;
import com.jaspersoft.jasperserver.jaxrs.client.core.ResponseStatus;
import com.jaspersoft.jasperserver.jaxrs.client.core.exceptions.JSClientException;
import org.codehaus.jackson.map.util.JSONWrappedObject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.core.Response;


public class OperationResultFactoryImpl implements OperationResultFactory {

    @Override
    public <T> OperationResult<T> getOperationResult(Response response, Class<T> responseClass) {
        if (isClientResource(responseClass))
            responseClass = (Class<T>) getSpecificResourceType(response);
        if (isAdHocViewMetadata(responseClass))
            responseClass = (Class<T>) getSpecificAdHocViewMetadataType(response);

        return getAppropriateOperationResultInstance(response, responseClass);
    }

    private <T> OperationResult<T> getAppropriateOperationResultInstance(Response response, Class<T> responseClass){
        OperationResult<T> result;
        //response.hasEntity() has wrong behaviour for some reason
        if (response.bufferEntity() && response.getStatus() != ResponseStatus.NO_CONTENT && !response.readEntity(String.class).equals(""))
            result = new WithEntityOperationResult<T>(response, responseClass);
        else
            result = new NullEntityOperationResult(response);
        return result;
    }

    private boolean isClientResource(Class<?> clazz){
        return clazz != Object.class && clazz.isAssignableFrom(ClientResource.class);
    }

    private boolean isAdHocViewMetadata(Class<?> clazz){
        return clazz != Object.class && clazz.isAssignableFrom(ClientAdHocViewMetadata.class);
    }

    private Class<? extends ClientResource> getSpecificResourceType(Response response){
        return ResourcesTypeResolverUtil.getClassForMime(response.getHeaderString("Content-Type"));
    }

    private Class<? extends ClientAdHocViewMetadata> getSpecificAdHocViewMetadataType(Response response){
        if (response.bufferEntity()) {
            JSONObject jsonObject = response.readEntity(JSONObject.class);
            try {
                String type = jsonObject.getString("type");
                if (type.equals("table"))
                    return ClientAdHocViewTableMetadata.class;
                else
                    return ClientClientAdHocViewChartMetadata.class;
            } catch (JSONException e) {
                throw new JSClientException("Unable to resolve AdHocViewMetadata type");
            }
        }
        return ClientAdHocViewMetadata.class;
    }

}
