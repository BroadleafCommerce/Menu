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
package org.broadleafcommerce.menu.service;

import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.domain.MenuItem;
import org.broadleafcommerce.menu.dto.MenuItemDTO;
import java.util.List;

public interface MenuService {

    /**
     * Returns the menu matching the passed in id. 
     * @param menuId
     * @return
     */
    public Menu findMenuById(Long menuId);

    /**
     * Returns the menu matching the passed in name. 
     * @param menuName
     * @return
     */
    public Menu findMenuByName(String menuName);

    /**
     * Returns the menu item matching the passed in id.
     * @param menuItemId
     * @return
     */
    public MenuItem findMenuItemById(Long menuItemId);

    /**
     * A Utility method that constructs generic MenuItemDTOs that are not dependent on a Menu Item Type.
     * Allows for ease of use when building the front-end.
     *
     * @param menu
     * @return
     */
    public List<MenuItemDTO> constructMenuItemDTOsForMenu(Menu menu);
}