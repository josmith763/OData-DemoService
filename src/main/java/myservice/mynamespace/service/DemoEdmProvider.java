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
package myservice.mynamespace.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;

public class DemoEdmProvider extends CsdlAbstractEdmProvider {

    // Service Namespace
    public static final String NAMESPACE = "OData.PetStore";

    // EDM Container
    public static final String CONTAINER_NAME = "Container";
    public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    // Entity Types Names
    public static final String ET_PET_NAME = "Pet";
    public static final FullQualifiedName ET_PET_FQN = new FullQualifiedName(NAMESPACE, ET_PET_NAME);

    public static final String ET_ORDER_NAME = "Order";
    public static final FullQualifiedName ET_ORDER_FQN = new FullQualifiedName(NAMESPACE, ET_ORDER_NAME);

    public static final String ET_USER_NAME = "User";
    public static final FullQualifiedName ET_USER_FQN = new FullQualifiedName(NAMESPACE, ET_USER_NAME);

    // Complex types
    public static final String CT_CATEGORY_NAME = "category";
    public static final FullQualifiedName CT_CATEGORY_FQN = new FullQualifiedName(NAMESPACE, CT_CATEGORY_NAME);

    public static final String CT_TAG_NAME = "tags";
    public static final FullQualifiedName CT_TAG_FQN = new FullQualifiedName(NAMESPACE, CT_TAG_NAME);

    // Entity Set Names
    public static final String ES_PETS_NAME = "Pets";
    public static final String ES_ORDERS_NAME = "Orders";
    public static final String ES_USERS_NAME = "Users";

    // Enum type
    public static final FullQualifiedName ENT_ORDER_STATUS_FQN = new FullQualifiedName(NAMESPACE, "orderStatus");
    public static final FullQualifiedName ENT_PET_STATUS_FQN = new FullQualifiedName(NAMESPACE, "petStatus");

    // See: https://stackoverflow.com/a/36058012
    public CsdlEnumType getEnumType(FullQualifiedName enumTypeName){
        if (ENT_ORDER_STATUS_FQN.equals(enumTypeName)) {
            return new CsdlEnumType()
                    .setName(ENT_ORDER_STATUS_FQN.getName())
                    .setMembers(Arrays.asList(
                            new CsdlEnumMember().setName("placed").setValue("0"),
                            new CsdlEnumMember().setName("approved").setValue("1"),
                            new CsdlEnumMember().setName("delivered").setValue("2")
                    ))
                    .setUnderlyingType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        } else if (ENT_PET_STATUS_FQN.equals(enumTypeName))
        {
            return new CsdlEnumType()
                    .setName(ENT_PET_STATUS_FQN.getName())
                    .setMembers(Arrays.asList(
                            new CsdlEnumMember().setName("Available").setValue("0"),
                            new CsdlEnumMember().setName("Pending").setValue("1"),
                            new CsdlEnumMember().setName("Sold").setValue("2")
                    ))
                    .setUnderlyingType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        }
        return null;
    }


    // entity set is plural of feach entity type

    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {

        // this method is called for each EntityType that are configured in the Schema
        CsdlEntityType entityType = null;

        if (entityTypeName.equals(ET_PET_FQN)) {
            // create EntityType properties
            CsdlProperty id = new CsdlProperty().setName("id")
                    .setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty name = new CsdlProperty().setName("name")
                    .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            // if a complex type (such as an object), add here as property, and add as getComplexType
            CsdlProperty category = new CsdlProperty().setName(CT_CATEGORY_NAME)
                    .setType(CT_CATEGORY_FQN);
            CsdlProperty photoUrls = new CsdlProperty().setName("photoUrls")
                    .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName())
                    .setCollection(true);
            CsdlProperty tags = new CsdlProperty().setName(CT_TAG_NAME)
                    .setType(CT_TAG_FQN)
                    .setCollection(true);
            CsdlProperty status = new CsdlProperty().setName("status")
                    .setType(ENT_PET_STATUS_FQN);


            // create PropertyRef for Key element
            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            // configure EntityType
            entityType = new CsdlEntityType();
            entityType.setName(ET_PET_NAME);
            entityType.setProperties(Arrays.asList(id, name, category, photoUrls, tags, status));
            entityType.setKey(Arrays.asList(propertyRef));

        } else if (entityTypeName.equals(ET_ORDER_FQN)) {
            CsdlProperty id = new CsdlProperty().setName("id")
                    .setType(EdmPrimitiveTypeKind.Int64.getFullQualifiedName());;
            CsdlProperty petId = new CsdlProperty().setName("petId")
                    .setType(EdmPrimitiveTypeKind.Int64.getFullQualifiedName());
            CsdlProperty quantity = new CsdlProperty().setName("quantity")
                    .setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
            CsdlProperty shipDate = new CsdlProperty().setName("shipDate")
                    .setType(EdmPrimitiveTypeKind.DateTimeOffset.getFullQualifiedName());
            CsdlProperty status = new CsdlProperty().setName("status")
                    .setType(ENT_ORDER_STATUS_FQN);
            CsdlProperty complete = new CsdlProperty().setName("complete")
                    .setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName());

            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            entityType = new CsdlEntityType();
            entityType.setName(ET_ORDER_NAME);
            entityType.setProperties(Arrays.asList(id, petId, quantity, shipDate, status, complete));
            entityType.setKey(Arrays.asList(propertyRef));
        } else if (entityTypeName.equals(ET_USER_FQN)) {
            CsdlProperty id = new CsdlProperty().setName("id")
                    .setType(EdmPrimitiveTypeKind.Int64.getFullQualifiedName());
            CsdlProperty username = new CsdlProperty().setName("username")
                    .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty firstName = new CsdlProperty().setName("firstName")
                    .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty lastName = new CsdlProperty().setName("lastName")
                    .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty email = new CsdlProperty().setName("email")
                    .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
            CsdlProperty password = new CsdlProperty().setName("password")
                    .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

            CsdlPropertyRef propertyRef = new CsdlPropertyRef();
            propertyRef.setName("id");

            entityType = new CsdlEntityType();
            entityType.setName(ET_USER_NAME);
            entityType.setProperties(Arrays.asList(id, username, firstName, lastName, email, password));
            entityType.setKey(Arrays.asList(propertyRef));
        }

        return entityType;

    }

    @Override
    public CsdlComplexType getComplexType(final FullQualifiedName complexTypeName) {
        CsdlComplexType complexType = null;
        if (complexTypeName.equals(CT_CATEGORY_FQN)) {
            complexType = new CsdlComplexType().setName(CT_CATEGORY_NAME)
                    .setProperties(Arrays.asList(
                            new CsdlProperty()
                                    .setName("id")
                                    .setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName()),
                            new CsdlProperty()
                                    .setName("name")
                                    .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName())));
        } else if (complexTypeName.equals(CT_TAG_FQN)) {
            // this doesn't really need to be repeated, but just keeping it simple for now to understand why
            complexType = new CsdlComplexType().setName(CT_TAG_NAME)
                    .setProperties(Arrays.asList(
                            new CsdlProperty()
                                    .setName("id")
                                    .setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName()),
                            new CsdlProperty()
                                    .setName("name")
                                    .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName())));
        }
        return complexType;
    }


    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {

        CsdlEntitySet entitySet = null;

        if (entityContainer.equals(CONTAINER)) {

            if (entitySetName.equals(ES_PETS_NAME)) {

                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_PETS_NAME);
                entitySet.setType(ET_PET_FQN);

                // navigation
               /* CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
                navPropBinding.setTarget("Categories"); // the target entity set, where the navigation property points to
                navPropBinding.setPath("Category"); // the path from entity type to navigation property
                List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<CsdlNavigationPropertyBinding>();
                navPropBindingList.add(navPropBinding);
                entitySet.setNavigationPropertyBindings(navPropBindingList);*/

            } else if (entitySetName.equals(ES_ORDERS_NAME)) {
                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_ORDERS_NAME);
                entitySet.setType(ET_ORDER_FQN);
            } else if (entitySetName.equals(ES_USERS_NAME)) {
                entitySet = new CsdlEntitySet();
                entitySet.setName(ES_USERS_NAME);
                entitySet.setType(ET_USER_FQN);
            }
        }

        return entitySet;
    }

    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {

        // This method is invoked when displaying the service document at
        // e.g. http://localhost:8080/DemoService/DemoService.svc
        if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
            CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
            entityContainerInfo.setContainerName(CONTAINER);
            return entityContainerInfo;
        }

        return null;
    }

    @Override
    public List<CsdlSchema> getSchemas() {
        // create Schema
        CsdlSchema schema = new CsdlSchema();
        schema.setNamespace(NAMESPACE);

        // add EntityTypes
        List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
        entityTypes.add(getEntityType(ET_PET_FQN));
        entityTypes.add(getEntityType(ET_ORDER_FQN));
        entityTypes.add(getEntityType(ET_USER_FQN));
        schema.setEntityTypes(entityTypes);

        // add enum types
        List<CsdlEnumType> enumTypes = new ArrayList<CsdlEnumType>();
        enumTypes.add(getEnumType(ENT_ORDER_STATUS_FQN));
        enumTypes.add(getEnumType(ENT_PET_STATUS_FQN));
        schema.setEnumTypes(enumTypes);

        // add Complex Types
        List<CsdlComplexType> complexTypes = new ArrayList<CsdlComplexType>();
        // add the complex type fqns
        complexTypes.add(getComplexType(CT_CATEGORY_FQN));
        complexTypes.add(getComplexType(CT_TAG_FQN));
        schema.setComplexTypes(complexTypes);

        // add EntityContainer
        schema.setEntityContainer(getEntityContainer());

        // finally
        List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
        schemas.add(schema);

        return schemas;
    }

    @Override
    public CsdlEntityContainer getEntityContainer() {

        // create EntitySets
        List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
        entitySets.add(getEntitySet(CONTAINER, ES_PETS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_ORDERS_NAME));
        entitySets.add(getEntitySet(CONTAINER, ES_USERS_NAME));

        // create EntityContainer
        CsdlEntityContainer entityContainer = new CsdlEntityContainer();
        entityContainer.setName(CONTAINER_NAME);
        entityContainer.setEntitySets(entitySets);

        return entityContainer;
    }
}
