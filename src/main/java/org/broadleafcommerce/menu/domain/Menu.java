/*
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2014 Broadleaf Commerce
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.broadleafcommerce.menu.domain;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Represents a menu, typically to drive the display of navigation on a website.
 *  
 * @author bpolster
 *
 */
public interface Menu extends Serializable {

    /**
     * Internal id of the menu.
     * @return
     */
    Long getId();

    /**
     * Sets the id of the menu.
     * @param id
     */
    void setId(Long id);

    /**
     * Returns the name of the menu.
     * @return
     */
    String getName();

    /**
     * Sets the name of the menu.
     * @param name
     */
    void setName(String name);

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