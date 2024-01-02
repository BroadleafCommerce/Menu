/*-
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2024 Broadleaf Commerce
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
package org.broadleafcommerce.menu.domain;

import org.broadleafcommerce.common.copy.MultiTenantCloneable;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.Nonnull;

/**
 * Represents a menu, typically to drive the display of navigation on a website.
 *  
 * @author bpolster
 *
 */
public interface Menu extends Serializable, MultiTenantCloneable<Menu> {

    /**
     * Internal id of the menu.
     * @return
     */
    public Long getId();

    /**
     * Sets the id of the menu.
     * @param id
     */
    public void setId(Long id);

    /**
     * Returns the name of the menu.
     * @return
     */
    public String getName();

    /**
     * Sets the name of the menu.
     * @param name
     */
    public void setName(String name);

    /**
     * Returns the list of associated {@link MenuItem}s 
     * 
     * @return the featured products
     */
    public List<MenuItem> getMenuItems();

    /**
     * Sets the list of associated {@link MenuItem}s 
     */
    public void setMenuItems(@Nonnull List<MenuItem> menuItems);

}
