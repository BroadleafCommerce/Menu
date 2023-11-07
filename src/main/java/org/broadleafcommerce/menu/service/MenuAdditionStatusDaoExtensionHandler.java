/*-
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2023 Broadleaf Commerce
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
package org.broadleafcommerce.menu.service;

import org.broadleafcommerce.common.extension.ExtensionResultStatusType;
import org.broadleafcommerce.core.catalog.dao.AbstractAdditionStatusDaoExtensionHandler;
import org.broadleafcommerce.core.catalog.dao.AdditionStatusDaoExtensionHandler;
import org.broadleafcommerce.core.catalog.dao.AdditionStatusDaoExtensionManager;
import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.domain.MenuItem;
import org.broadleafcommerce.menu.domain.MenuItemImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component("blMenuAdditionStatusDaoExtensionHandler")
public class MenuAdditionStatusDaoExtensionHandler extends AbstractAdditionStatusDaoExtensionHandler
        implements AdditionStatusDaoExtensionHandler {

    @Resource(name = "blAdditionStatusDaoExtensionManager")
    protected AdditionStatusDaoExtensionManager extensionManager;

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @PostConstruct
    public void init() {
        if (this.isEnabled()) {
            this.extensionManager.registerHandler(this);
        }
    }

    @Override
    public ExtensionResultStatusType cleanUpEntity(Object entity) {
        if (entity instanceof Menu) {
            cleanUpMenus((Menu) entity);
        }

        return ExtensionResultStatusType.HANDLED_CONTINUE;
    }

    protected void cleanUpMenus(final Menu additionStatus) {
        findMenuItems(additionStatus.getId()).forEach(menuItem -> em.remove(menuItem));
    }

    protected List<MenuItem> findMenuItems(final Long id) {
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<MenuItem> criteria = builder.createQuery(MenuItem.class);
        final Root<MenuItemImpl> root = criteria.from(MenuItemImpl.class);
        criteria.select(root);

        final List<Predicate> restrictions = new ArrayList<>();
        restrictions.add(builder.equal(root.get("parentMenu").get("id"), id));
        restrictions.add(root.get("parentMenu")
                .get("embeddableAdditionStatusDiscriminator").get("additionStatus").in("NEW"));
        criteria.where(restrictions.toArray(new Predicate[0]));

        return em.createQuery(criteria).getResultList();
    }

}
