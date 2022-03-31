/*
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2016 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package org.broadleafcommerce.menu.admin.server.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.presentation.client.OperationType;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.broadleafcommerce.common.sandbox.SandBoxHelper;
import org.broadleafcommerce.common.util.BLCMessageUtils;
import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.domain.MenuItem;
import org.broadleafcommerce.menu.domain.MenuItemImpl;
import org.broadleafcommerce.menu.service.MenuService;
import org.broadleafcommerce.menu.type.MenuItemType;
import org.broadleafcommerce.openadmin.dto.BasicFieldMetadata;
import org.broadleafcommerce.openadmin.dto.ClassMetadata;
import org.broadleafcommerce.openadmin.dto.CriteriaTransferObject;
import org.broadleafcommerce.openadmin.dto.DynamicResultSet;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.FieldMetadata;
import org.broadleafcommerce.openadmin.dto.MergedPropertyType;
import org.broadleafcommerce.openadmin.dto.PersistencePackage;
import org.broadleafcommerce.openadmin.dto.PersistencePerspective;
import org.broadleafcommerce.openadmin.dto.Property;
import org.broadleafcommerce.openadmin.server.dao.DynamicEntityDao;
import org.broadleafcommerce.openadmin.server.service.ValidationException;
import org.broadleafcommerce.openadmin.server.service.handler.CustomPersistenceHandlerAdapter;
import org.broadleafcommerce.openadmin.server.service.persistence.module.EmptyFilterValues;
import org.broadleafcommerce.openadmin.server.service.persistence.module.InspectHelper;
import org.broadleafcommerce.openadmin.server.service.persistence.module.PersistenceModule;
import org.broadleafcommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FieldPath;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FilterMapping;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.Restriction;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.predicate.PredicateProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Component("blMenuItemCustomPersistenceHandler")
public class MenuItemCustomPersistenceHandler extends CustomPersistenceHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(MenuItemCustomPersistenceHandler.class);

    public static final String DERIVED_LABEL_FIELD_NAME = "derivedLabel";
    public static final String MENU_SEPARATOR = " -> ";
    protected static final String IMAGE_URL = "imageUrl";
    protected static final String ID_PROPERTY = "id";
    protected static final String TYPE_PROPERTY = "type";
    protected static final String LINKED_MENU_PROPERTY = "linkedMenu";
    protected static final String PARENT_MENU_PROPERTY = "parentMenu";

    @Resource(name = "blMenuService")
    protected MenuService menuService;
    @Resource(name = "blSandBoxHelper")
    protected SandBoxHelper sandBoxHelper;

    @Override
    public Boolean canHandleInspect(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            Class<?> testClass = Class.forName(ceilingEntityFullyQualifiedClassname);
            return MenuItem.class.isAssignableFrom(testClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        if (isSelectingLinkedMenu(persistencePackage)) return true;
        return canHandleInspect(persistencePackage);
    }

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return canHandleInspect(persistencePackage);
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return canHandleInspect(persistencePackage);
    }

    @Override
    public DynamicResultSet inspect(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, InspectHelper helper) throws ServiceException {
        try {
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties = new HashMap<MergedPropertyType, Map<String, FieldMetadata>>();

            //retrieve the default properties for Menu Item
            Map<String, FieldMetadata> properties = helper.getSimpleMergedProperties(MenuItem.class.getName(), persistencePerspective);

            //create a new field to hold default content item checkbox
            BasicFieldMetadata derivedLabelMetadata = new BasicFieldMetadata();
            derivedLabelMetadata.setFieldType(SupportedFieldType.STRING);
            derivedLabelMetadata.setMutable(false);
            derivedLabelMetadata.setInheritedFromType(MenuItemImpl.class.getName());
            derivedLabelMetadata.setAvailableToTypes(new String[]{MenuItemImpl.class.getName()});
            derivedLabelMetadata.setForeignKeyCollection(false);
            derivedLabelMetadata.setMergedPropertyType(MergedPropertyType.PRIMARY);
            derivedLabelMetadata.setName(DERIVED_LABEL_FIELD_NAME);
            derivedLabelMetadata.setFriendlyName("MenuItemImpl_Derived_Label");
            derivedLabelMetadata.setExplicitFieldType(SupportedFieldType.STRING);
            derivedLabelMetadata.setProminent(true);
            derivedLabelMetadata.setReadOnly(true);
            derivedLabelMetadata.setOrder(500);
            derivedLabelMetadata.setGridOrder(500);

            derivedLabelMetadata.setVisibility(VisibilityEnum.FORM_HIDDEN);
            derivedLabelMetadata.setExcluded(false);
            properties.put(DERIVED_LABEL_FIELD_NAME, derivedLabelMetadata);

            allMergedProperties.put(MergedPropertyType.PRIMARY, properties);
            Class<?>[] entityClasses = dynamicEntityDao.getAllPolymorphicEntitiesFromCeiling(MenuItem.class);
            ClassMetadata mergedMetadata = helper.buildClassMetadata(entityClasses, persistencePackage, allMergedProperties);

            return new DynamicResultSet(mergedMetadata, null, null);

        } catch (Exception e) {
            String className = persistencePackage.getCeilingEntityFullyQualifiedClassname();
            ServiceException ex = new ServiceException("Unable to retrieve inspection results for " + className, e);
            LOG.error("Unable to retrieve inspection results for " + className, ex);
            throw ex;
        }
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        if (isSelectingLinkedMenu(persistencePackage)) {
            FilterMapping defaultCategoryMapping = createFilterMappingForProperty(ID_PROPERTY, new PredicateProvider() {
                @Override
                public Predicate buildPredicate(CriteriaBuilder builder, FieldPathBuilder fieldPathBuilder, From root,
                                                String ceilingEntity, String fullPropertyName, Path explicitPath,
                                                List directValues) {
                    return builder.and(builder.notEqual(explicitPath, persistencePackage.getSectionCrumbs()[0].getSectionId()));
                }
            });
            cto.getAdditionalFilterMappings().add(defaultCategoryMapping);
        }
        OperationType fetchType = persistencePackage.getPersistencePerspective().getOperationTypes().getFetchType();
        PersistenceModule persistenceModule = helper.getCompatibleModule(fetchType);
        DynamicResultSet drs = persistenceModule.fetch(persistencePackage, cto);

        for (Entity entity : drs.getRecords()) {
            Property menuItemId = entity.findProperty("id");
            if (menuItemId != null) {
                MenuItem menuItem = menuService.findMenuItemById(Long.parseLong(menuItemId.getValue()));
                if (menuItem != null) {
                    //Call getDerivedLabel() on an actual MenuItem entity
                    // as it is optional and can be determined by MenuItemType and not by reflection.
                    Property derivedLabel = new Property();
                    derivedLabel.setName(DERIVED_LABEL_FIELD_NAME);
                    derivedLabel.setValue(menuItem.getDerivedLabel());
                    entity.addProperty(derivedLabel);
                }
            }
        }

        return drs;
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        this.cleanImageProperty(persistencePackage);
        this.validateMenuItem(persistencePackage.getEntity());
        try {
            OperationType updateType = persistencePackage.getPersistencePerspective().getOperationTypes().getUpdateType();
            return helper.getCompatibleModule(updateType).add(persistencePackage);
        } catch (Exception e) {
            LOG.error("Unable to add entity (execute persistence activity) ", e);
            throw new ServiceException("Unable to add entity", e);
        }
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        this.cleanImageProperty(persistencePackage);
        this.validateMenuItem(persistencePackage.getEntity());
        try {
            OperationType updateType = persistencePackage.getPersistencePerspective().getOperationTypes().getUpdateType();
            return helper.getCompatibleModule(updateType).update(persistencePackage);
        } catch (Exception e) {
            LOG.error("Unable to update entity (execute persistence activity) ", e);
            throw new ServiceException("Unable to update entity", e);
        }
    }

    protected void cleanImageProperty(final PersistencePackage persistencePackage) {
        final Property url = persistencePackage.getEntity().findProperty(IMAGE_URL);
        if (url != null && StringUtils.isEmpty(url.getValue())) {
            url.setValue(" ");
        }
    }

    protected boolean isSelectingLinkedMenu(PersistencePackage persistencePackage) {
        List<String> customCriteria = Arrays.asList(persistencePackage.getCustomCriteria());
        return customCriteria.contains("requestingField=linkedMenu");
    }

    protected FilterMapping createFilterMappingForProperty(String targetPropertyName, PredicateProvider predicateProvider) {
        FieldPath fieldPath = new FieldPath().withTargetProperty(targetPropertyName);
        EmptyFilterValues directFilterValues = new EmptyFilterValues();
        Restriction newRestriction = new Restriction().withPredicateProvider(predicateProvider);

        return new FilterMapping()
                .withFieldPath(fieldPath)
                .withDirectFilterValues(directFilterValues)
                .withRestriction(newRestriction);
    }

    protected void validateMenuItem(final Entity entity) throws ValidationException {
        this.validateSelfLink(entity);
        this.validateRecursiveRelationship(entity);
    }

    protected void validateSelfLink(final Entity entity) throws ValidationException {
        final Property linkedMenuProperty = entity.findProperty(LINKED_MENU_PROPERTY);
        final Property parentMenuProperty = entity.findProperty(PARENT_MENU_PROPERTY);
        if (linkedMenuProperty != null && linkedMenuProperty.getValue() != null
                && parentMenuProperty != null) {
            final String linkedMenuId = linkedMenuProperty.getValue();
            final String parentMenuId = parentMenuProperty.getValue();
            if (linkedMenuId.equals(parentMenuId)) {
                entity.addValidationError(LINKED_MENU_PROPERTY,"validateSelfLink");
                throw new ValidationException(entity);
            }
        }
    }

    protected void validateRecursiveRelationship(final Entity entity) throws ValidationException {
        final Property typeProperty = entity.findProperty(TYPE_PROPERTY);
        final Property linkedMenuProperty = entity.findProperty(LINKED_MENU_PROPERTY);
        final Property parentMenuProperty = entity.findProperty(PARENT_MENU_PROPERTY);
        if (typeProperty != null && MenuItemType.SUBMENU.getType().equals(typeProperty.getValue())) {
            if (linkedMenuProperty != null && linkedMenuProperty.getValue() != null
                    && parentMenuProperty != null && parentMenuProperty.getValue() != null) {
                final Long linkedMenuId = Long.parseLong(linkedMenuProperty.getValue());
                final Long parentMenuId = Long.parseLong(parentMenuProperty.getValue());
                final Menu linkedMenu = this.menuService.findMenuById(linkedMenuId);
                final Menu parentMenu = this.menuService.findMenuById(parentMenuId);
                final StringBuilder menuLinks = new StringBuilder();
                this.addMenuLink(menuLinks, parentMenu.getName());
                this.addMenuLink(menuLinks, linkedMenu.getName());
                this.validateLinkedMenu(entity, linkedMenu, parentMenuId, menuLinks);
            }
        }
    }

    private void validateLinkedMenu(final Entity entity, final Menu linkedMenu, final Long id,
                                    final StringBuilder menuLinks) throws ValidationException {
        if (linkedMenu != null) {
            for (MenuItem menuItem : linkedMenu.getMenuItems()) {
                if ((MenuItemType.SUBMENU.equals(menuItem.getMenuItemType()))) {
                    final Menu itemLinkedMenu = menuItem.getLinkedMenu();
                    if (itemLinkedMenu != null) {
                        this.addMenuLink(menuLinks, itemLinkedMenu.getName());
                        final Long originalId = this.sandBoxHelper.getOriginalId(itemLinkedMenu);
                        if (id.equals(itemLinkedMenu.getId()) || id.equals(originalId)) {
                            menuLinks.delete(menuLinks.lastIndexOf(MENU_SEPARATOR), menuLinks.length());
                            final String errorMessage = BLCMessageUtils.getMessage(
                                    "validationRecursiveRelationship", menuLinks
                            );
                            entity.addValidationError(LINKED_MENU_PROPERTY, errorMessage);
                            throw new ValidationException(entity);
                        }
                        this.validateLinkedMenu(entity, itemLinkedMenu, id, menuLinks);
                    }
                }
            }
        }
    }

    protected void addMenuLink(final StringBuilder menuLinks, final String menuName) {
        menuLinks.append(menuName);
        menuLinks.append(MENU_SEPARATOR);
    }

}
