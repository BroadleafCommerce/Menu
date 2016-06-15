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
import org.broadleafcommerce.menu.domain.MenuItem;
import org.broadleafcommerce.menu.domain.MenuItemImpl;
import org.broadleafcommerce.menu.service.MenuService;
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
import org.broadleafcommerce.openadmin.server.service.handler.CustomPersistenceHandlerAdapter;
import org.broadleafcommerce.openadmin.server.service.persistence.module.InspectHelper;
import org.broadleafcommerce.openadmin.server.service.persistence.module.PersistenceModule;
import org.broadleafcommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Component("blMenuItemCustomPersistenceHandler")
public class MenuItemCustomPersistenceHandler extends CustomPersistenceHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(MenuItemCustomPersistenceHandler.class);

    public static final String DERIVED_LABEL_FIELD_NAME = "derivedLabel";

    @Resource(name = "blMenuService")
    protected MenuService menuService;

    @Override
    public Boolean canHandleInspect(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            Class testClass = Class.forName(ceilingEntityFullyQualifiedClassname);
            return MenuItem.class.isAssignableFrom(testClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
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
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper)
            throws ServiceException {
        Property url = persistencePackage.getEntity().findProperty("image.url");
        if (url != null && StringUtils.isEmpty(url.getValue())) {
            url.setValue(" ");
        }
        return helper.getCompatibleModule(OperationType.BASIC).add(persistencePackage);
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Property url = persistencePackage.getEntity().findProperty("image.url");
        if (url != null && StringUtils.isEmpty(url.getValue())) {
            url.setValue(" ");
        }
        return helper.getCompatibleModule(OperationType.BASIC).update(persistencePackage);
    }
}
