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

import org.broadleafcommerce.cms.page.domain.Page;
import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.menu.type.MenuItemType;
import java.io.Serializable;
import java.math.BigDecimal;


public interface MenuItem extends Serializable {

    /**
     * Id of this menuItem
     * @return
     */
    public Long getId();

    /**
     * Sets the id of this menuItem.
     * @param id
     */
    public void setId(Long id);

    /**
     * The value to show for this menu item when displayed on a site.
     * Will return the configured Label, unless of type {@link MenuItemType#PRODUCT}, or {@link MenuItemType#CATEGORY},
     * or {@link MenuItemType#SUBMENU} which will return the corresponding linked name.
     */
    public String getLabel();

    /**
     * Sets the value for this menu item to display on a site.
     * @param label
     */
    public void setLabel(String label);

    /**
     * Returns the {@link MenuItemType}
     * @return
     */
    public MenuItemType getMenuItemType();

    /**
     * Sets the {@link MenuItemType} 
     * @param menuItemType
     */
    public void setMenuItemType(MenuItemType menuItemType);

    /**
     * Returns the URL (if applicable) that should be targeted when this menu is clicked.
     * @return
     */
    public String getActionUrl();

    /**
     * Sets the URL to go to if this menu is clicked.
     * @param actionUrl
     */
    public void setActionUrl(String actionUrl);

    /**
     * For items of type {@link MenuItemType#LINK}, gets any Media associated with this Menu Item
     * @return
     */
    public Media getImage();

    /**
     * For items of type {@link MenuItemType#LINK}, sets an Image for this Menu Item
     * @param imageUrl
     */
    public void setImage(Media media);

    /**
     * Returns the {@link Menu} to which this menuItem belongs
     * @return
     */
    public Menu getParentMenu();

    /**
     * Sets the {@link Menu} to which this menuItem belongs
     * @param menu
     */
    public void setParentMenu(Menu menu);
    
    /**
     * For items of type {@link MenuItemType#CATEGORY}, stores the associated category.
     * @return
     */
    public Category getLinkedCategory();

    /**
     * For items of type {@link MenuItemType#CATEGORY}, returns the associated category.
     * @return
     */
    public void setLinkedCategory(Category linkedCategory);

    /**
     * For items of type {@link MenuItemType#PRODUCT}, stores the associated product.
     * @return
     */
    public Product getLinkedProduct();

    /**
     * For items of type {@link MenuItemType#PRODUCT}, returns the associated product.
     * @return
     */
    public void setLinkedProduct(Product linkedProduct);


    /**
     * The menu represented by this MenuItem.   (Allows for nested menus).
     * @return
     */
    public Menu getLinkedMenu();

    /**
     * Sets the linked menu.
     * @param menu
     */
    public void setLinkedMenu(Menu menu);

    /**
     * Sets the order of this MenuItem in the {@link #getParentMenu()}
     * @param sequence
     */
    public void setSequence(BigDecimal sequence);

    /**
     * Returns the order of this MenuItem in the {@link #getParentMenu()}
     * @return
     */
    public BigDecimal getSequence();

    /**
     * For items of type {@link MenuItemType#LINK}, gets the alt text for the associated image.
     * @return
     */
    public String getAltText();

    /**
     * For items of type {@link MenuItemType#LINK}, stores the alt text for the associated image.
     * @return
     */
    public void setAltText(String altText);

    /**
     * For items of type {@link MenuItemType#PAGE}, returns the associated page.
     * @return
     */
    public Page getLinkedPage();

    /**
     * For items of type {@link MenuItemType#PAGE}, sets the associated page.
     * @return
     */
    public void setLinkedPage(Page linkedPage);

    /**
     * For items of type {@link MenuItemType#CUSTOM}, returns the associated custom HTML.
     * @return
     */
    public String getCustomHtml();

    /**
     * For items of type {@link MenuItemType#CUSTOM}, sets the associated custom HTML.
     * @return
     */
    public void setCustomHtml(String customHtml);

    /**
     * Convenience method that returns an anchor URL based on the configured MenuItemType.
     * Will return getActionUrl() unless type is {@link MenuItemType#PAGE},
     * {@link MenuItemType#PRODUCT}, or {@link MenuItemType#CATEGORY} and will use corresponding linked URL.
     *
     * @return
     */
    public String getUrl();

}
