/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package myservice.mynamespace.data;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmKeyPropertyRef;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;

import myservice.mynamespace.service.DemoEdmProvider;
import myservice.mynamespace.util.Util;

public class Storage {

    // represent our database
    private List<Entity> petList;

    public Storage() {

        petList = new ArrayList<Entity>();

        // creating some sample data
        initPetSampleData();
    }

    /* PUBLIC FACADE */
    public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet) {
        EntityCollection entitySet = null;

        if (edmEntitySet.getName().equals(DemoEdmProvider.ES_PETS_NAME)) {
            entitySet = getPets();
        }
        return entitySet;
    }

    public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException {
        Entity entity = null;

        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        if (edmEntityType.getName().equals(DemoEdmProvider.ET_PET_NAME)) {
            entity = getPet(edmEntityType, keyParams);
        }
        return entity;
    }

    public Entity createEntityData(EdmEntitySet edmEntitySet, Entity entityToCreate) {

        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        // actually, this is only required if we have more than one Entity Type
        if (edmEntityType.getName().equals(DemoEdmProvider.ET_PET_NAME)) {
            return createPet(edmEntityType, entityToCreate);
        }

        return null;
    }

    /**
     * This method is invoked for PATCH or PUT requests
     * */
    public void updateEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams, Entity updateEntity,
                                 HttpMethod httpMethod) throws ODataApplicationException {

        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        // actually, this is only required if we have more than one Entity Type
        if (edmEntityType.getName().equals(DemoEdmProvider.ET_PET_NAME)) {
            updatePet(edmEntityType, keyParams, updateEntity, httpMethod);
        }
    }

    public void deleteEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams)
            throws ODataApplicationException {

        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        // actually, this is only required if we have more than one Entity Type
        if (edmEntityType.getName().equals(DemoEdmProvider.ET_PET_NAME)) {
            deletePet(edmEntityType, keyParams);
        }
    }

    // Navigation

    public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType) {
        EntityCollection collection = getRelatedEntityCollection(entity, relatedEntityType);
        if (collection.getEntities().isEmpty()) {
            return null;
        }
        return collection.getEntities().get(0);
    }

    public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType, List<UriParameter> keyPredicates) throws ODataApplicationException {

        EntityCollection relatedEntities = getRelatedEntityCollection(entity, relatedEntityType);
        return Util.findEntity(relatedEntityType, relatedEntities, keyPredicates);
    }

   public EntityCollection getRelatedEntityCollection(Entity sourceEntity, EdmEntityType targetEntityType) {
        EntityCollection navigationTargetEntityCollection = new EntityCollection();

      /*  FullQualifiedName relatedEntityFqn = targetEntityType.getFullQualifiedName();
        String sourceEntityFqn = sourceEntity.getType();

        if (sourceEntityFqn.equals(DemoEdmProvider.ET_PRODUCT_FQN.getFullQualifiedNameAsString())
                && relatedEntityFqn.equals(DemoEdmProvider.ET_CATEGORY_FQN)) {
            // relation Products->Category (result all categories)
            int productID = (Integer) sourceEntity.getProperty("ID").getValue();
            if (productID == 1 || productID == 2) {
                navigationTargetEntityCollection.getEntities().add(categoryList.get(0));
            } else if (productID == 3 || productID == 4) {
                navigationTargetEntityCollection.getEntities().add(categoryList.get(1));
            } else if (productID == 5 || productID == 6) {
                navigationTargetEntityCollection.getEntities().add(categoryList.get(2));
            }
        } else if (sourceEntityFqn.equals(DemoEdmProvider.ET_CATEGORY_FQN.getFullQualifiedNameAsString())
                && relatedEntityFqn.equals(DemoEdmProvider.ET_PRODUCT_FQN)) {
            // relation Category->Products (result all products)
            int categoryID = (Integer) sourceEntity.getProperty("ID").getValue();
            if (categoryID == 1) {
                // the first 2 products are notebooks
                navigationTargetEntityCollection.getEntities().addAll(productList.subList(0, 2));
            } else if (categoryID == 2) {
                // the next 2 products are organizers
                navigationTargetEntityCollection.getEntities().addAll(productList.subList(2, 4));
            } else if (categoryID == 3) {
                // the first 2 products are monitors
                navigationTargetEntityCollection.getEntities().addAll(productList.subList(4, 6));
            }
        } else if (sourceEntityFqn.equals(DemoEdmProvider.ET_PRODUCT_FQN.getFullQualifiedNameAsString())
                && relatedEntityFqn.equals(DemoEdmProvider.ET_SUPPLIER_FQN)) {
            int productID = (Integer) sourceEntity.getProperty("ID").getValue();
            if (productID == 1) {
                navigationTargetEntityCollection.getEntities().add(supplierList.get(0));
                navigationTargetEntityCollection.getEntities().add(supplierList.get(1));
            } else if (productID == 2) {
                navigationTargetEntityCollection.getEntities().add(supplierList.get(2));
            } else if (productID == 3) {
                navigationTargetEntityCollection.getEntities().add(supplierList.get(3));
            } else if (productID == 4) {
                navigationTargetEntityCollection.getEntities().add(supplierList.get(4));
            } else if (productID == 5) {
                navigationTargetEntityCollection.getEntities().add(supplierList.get(5));
            } else if (productID == 6) {
                navigationTargetEntityCollection.getEntities().add(supplierList.get(6));
            }
        }

        if (navigationTargetEntityCollection.getEntities().isEmpty()) {
            return null;
        }*/

        return navigationTargetEntityCollection;
    }

    /* INTERNAL */

    private EntityCollection getPets() {
        EntityCollection retEntitySet = new EntityCollection();

        for (Entity petEntity : this.petList) {
            retEntitySet.getEntities().add(petEntity);
        }

        return retEntitySet;
    }

    private Entity getPet(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException {

        // the list of entities at runtime
        EntityCollection entityCollection = getPets();

        /* generic approach to find the requested entity */
        return Util.findEntity(edmEntityType, entityCollection, keyParams);
    }

    private Entity createPet(EdmEntityType edmEntityType, Entity entity) {

        // the ID of the newly created product entity is generated automatically
        int newId = 1;
        while (petIdExists(newId)) {
            newId++;
        }

        Property idProperty = entity.getProperty("ID");
        if (idProperty != null) {
            idProperty.setValue(ValueType.PRIMITIVE, Integer.valueOf(newId));
        } else {
            // as of OData v4 spec, the key property can be omitted from the POST request body
            entity.getProperties().add(new Property(null, "ID", ValueType.PRIMITIVE, newId));
        }
        //entity.setId(createId("Products", newId));
        this.petList.add(entity);

        return entity;

    }

    private boolean petIdExists(int id) {

        for (Entity entity : this.petList) {
            Integer existingID = (Integer) entity.getProperty("id").getValue();
            if (existingID.intValue() == id) {
                return true;
            }
        }

        return false;
    }

    private void updatePet(EdmEntityType edmEntityType, List<UriParameter> keyParams, Entity entity,
                               HttpMethod httpMethod) throws ODataApplicationException {

        Entity petEntity = getPet(edmEntityType, keyParams);
        if (petEntity == null) {
            throw new ODataApplicationException("Entity not found", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        // loop over all properties and replace the values with the values of the given payload
        // Note: ignoring ComplexType, as we don't have it in our odata model
        List<Property> existingProperties = petEntity.getProperties();
        for (Property existingProp : existingProperties) {
            String propName = existingProp.getName();

            // ignore the key properties, they aren't updateable
            if (isKey(edmEntityType, propName)) {
                continue;
            }

            Property updateProperty = entity.getProperty(propName);
            // the request payload might not consider ALL properties, so it can be null
            if (updateProperty == null) {
                // if a property has NOT been added to the request payload
                // depending on the HttpMethod, our behavior is different
                if (httpMethod.equals(HttpMethod.PATCH)) {
                    // as of the OData spec, in case of PATCH, the existing property is not touched
                    continue; // do nothing
                } else if (httpMethod.equals(HttpMethod.PUT)) {
                    // as of the OData spec, in case of PUT, the existing property is set to null (or to default value)
                    existingProp.setValue(existingProp.getValueType(), null);
                    continue;
                }
            }

            // change the value of the properties
            existingProp.setValue(existingProp.getValueType(), updateProperty.getValue());
        }
    }

    private void deletePet(EdmEntityType edmEntityType, List<UriParameter> keyParams)
            throws ODataApplicationException {

        Entity petEntity = getPet(edmEntityType, keyParams);
        if (petEntity == null) {
            throw new ODataApplicationException("Entity not found", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        this.petList.remove(petEntity);
    }

    /* HELPER */

    private boolean isKey(EdmEntityType edmEntityType, String propertyName) {
        List<EdmKeyPropertyRef> keyPropertyRefs = edmEntityType.getKeyPropertyRefs();
        for (EdmKeyPropertyRef propRef : keyPropertyRefs) {
            String keyPropertyName = propRef.getName();
            if (keyPropertyName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }

    private void initPetSampleData() {

        Entity entity = new Entity();

        entity.addProperty(new Property(null, "id", ValueType.PRIMITIVE, 1));
        entity.addProperty(new Property(null, "name", ValueType.PRIMITIVE, "Doggie"));
        //entity.addProperty(new Property(null, "category", ValueType.PRIMITIVE, "Sample cat"));
        //entity.addProperty(new Property(null, "tags", ValueType.PRIMITIVE, "Notebook Basic, 1.7GHz - 15 XGA - 1024MB DDR2 SDRAM - 40GB"));

        /*ComplexValue complexValue = new ComplexValue();
        Property p1 = new Property(null, "tags", ValueType.PRIMITIVE, "Sample Tag 1");
        Property p2 = new Property(null, "tags", ValueType.PRIMITIVE, "Sample Tag 2");
        complexValue.getValue().add(p1);
        complexValue.getValue().add(p2);

        entity.addProperty(new Property(null, "tags", ValueType.COMPLEX, complexValue));*/

        ArrayList<String> strList = new ArrayList<String>();
        strList.add("URL 1");
        strList.add("URL 2");

        entity.addProperty(new Property(null, "photoUrls", ValueType.COLLECTION_PRIMITIVE, strList));
        //entity.addProperty(new Property(null, "photoUrls", ValueType.PRIMITIVE, '1'));


        entity.addProperty(new Property(null, "status", ValueType.ENUM, "Available"));
        entity.setType(DemoEdmProvider.ET_PET_FQN.getFullQualifiedNameAsString());
        entity.setId(createId(entity, "id"));
        petList.add(entity);
    }


    private URI createId(Entity entity, String idPropertyName) {
        return createId(entity, idPropertyName, null);
    }

    private URI createId(Entity entity, String idPropertyName, String navigationName) {
        try {
            StringBuilder sb = new StringBuilder(getEntitySetName(entity)).append("(");
            final Property property = entity.getProperty(idPropertyName);
            sb.append(property.asPrimitive()).append(")");
            if(navigationName != null) {
                sb.append("/").append(navigationName);
            }
            return new URI(sb.toString());
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create (Atom) id for entity: " + entity, e);
        }
    }

    private URI createId(Entity entity, String idPropertyName, String navigationName, String sourceId) {
        try {
            StringBuilder sb = new StringBuilder(getEntitySetName(entity)).append("(");
            final Property property = entity.getProperty(idPropertyName);
            sb.append(sourceId).append(")");
            if(navigationName != null) {
                sb.append("/").append(navigationName);
                sb.append("(").append(property.asPrimitive()).append(")");
            }
            return new URI(sb.toString());
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create (Atom) id for entity: " + entity, e);
        }
    }

    private String getEntitySetName(Entity entity) {
        if(DemoEdmProvider.ET_PET_FQN.getFullQualifiedNameAsString().equals(entity.getType())) {
            return DemoEdmProvider.ES_PETS_NAME;
        }
        return entity.getType();
    }
}
