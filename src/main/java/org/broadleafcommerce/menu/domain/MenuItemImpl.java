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
import org.broadleafcommerce.cms.page.domain.PageImpl;
import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import org.broadleafcommerce.common.i18n.service.DynamicTranslationProvider;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.CategoryImpl;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.ProductImpl;
import org.broadleafcommerce.menu.type.MenuItemType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BLC_CMS_MENU_ITEM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blCMSElements")
@AdminPresentationClass(friendlyName = "MenuItemImpl")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps = true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class MenuItemImpl implements MenuItem {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "MenuItemId")
    @GenericGenerator(
            name = "MenuItemId",
            strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @Parameter(name = "segment_value", value = "MenuItemImpl"),
                    @Parameter(name = "entity_name", value = "org.broadleafcommerce.menu.domain.MenuItemImpl")
            })
    @Column(name = "MENU_ITEM_ID")
    protected Long id;

    @Column(name = "LABEL")
    @AdminPresentation(friendlyName = "MenuItemImpl_Label",
            order = Presentation.FieldOrder.LABEL,
            gridOrder = Presentation.FieldOrder.LABEL,
            prominent = true,
            translatable = true)
    protected String label;

    @Column(name = "MENU_ITEM_TYPE")
    @AdminPresentation(friendlyName = "MenuItemImpl_Type",
            order = Presentation.FieldOrder.MENU_ITEM_TYPE,
            prominent = true,
            gridOrder = Presentation.FieldOrder.MENU_ITEM_TYPE,
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
            broadleafEnumeration = "org.broadleafcommerce.menu.type.MenuItemType")
    protected String type;

    @Column(name = "SEQUENCE")
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long sequence;

    @ManyToOne(optional = true, targetEntity = MenuImpl.class)
    @JoinColumn(name = "PARENT_MENU_ID")
    protected Menu parentMenu;

    @Column(name = "ACTION_URL")
    @AdminPresentation(friendlyName = "MenuItemImpl_ActionUrl",
            order = Presentation.FieldOrder.ACTION_URL)
    protected String actionUrl;

    @Column(name = "IMAGE_URL")
    @AdminPresentation(friendlyName = "MenuItemImpl_ImageUrl",
            order = Presentation.FieldOrder.IMAGE_URL)
    protected String imageUrl;

    @ManyToOne(targetEntity = CategoryImpl.class)
    @JoinColumn(name = "CATEGORY_ID")
    @AdminPresentation(friendlyName = "MenuItemImpl_Category",
            order = Presentation.FieldOrder.CATEGORY)
    @AdminPresentationToOneLookup()
    protected Category linkedCategory;

    @ManyToOne(targetEntity = ProductImpl.class)
    @JoinColumn(name = "PRODUCT_ID")
    @AdminPresentation(friendlyName = "MenuItemImpl_Product",
            order = Presentation.FieldOrder.PRODUCT)
    @AdminPresentationToOneLookup()
    protected Product linkedProduct;

    @ManyToOne(targetEntity = PageImpl.class)
    @JoinColumn(name = "PAGE_ID")
    @AdminPresentation(friendlyName = "MenuItemImpl_Page",
            order = Presentation.FieldOrder.PAGE)
    @AdminPresentationToOneLookup()
    protected Page linkedPage;

    @ManyToOne(targetEntity = MenuImpl.class)
    @JoinColumn(name = "LINKED_MENU_ID")
    @AdminPresentation(friendlyName = "MenuItemImpl_LinkedMenu",
            order = Presentation.FieldOrder.LINKED_MENU)
    @AdminPresentationToOneLookup()
    protected Menu linkedMenu;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public MenuItemType getMenuItemType() {
        return MenuItemType.getInstance(type);
    }

    @Override
    public void setMenuItemType(MenuItemType menuItemType) {
        type = menuItemType.getType();
    }

    @Override
    public String getLabel() {
        return DynamicTranslationProvider.getValue(this, "label", label);
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getActionUrl() {
        return actionUrl;
    }

    @Override
    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Long getSequence() {
        return sequence;
    }

    @Override
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    @Override
    public Menu getParentMenu() {
        return parentMenu;
    }

    @Override
    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    @Override
    public Category getLinkedCategory() {
        return linkedCategory;
    }

    @Override
    public void setLinkedCategory(Category linkedCategory) {
        this.linkedCategory = linkedCategory;
    }

    @Override
    public Product getLinkedProduct() {
        return linkedProduct;
    }

    @Override
    public void setLinkedProduct(Product linkedProduct) {
        this.linkedProduct = linkedProduct;
    }

    @Override
    public Page getLinkedPage() {
        return linkedPage;
    }

    @Override
    public void setLinkedPage(Page linkedPage) {
        this.linkedPage = linkedPage;
    }

    @Override
    public Menu getLinkedMenu() {
        return linkedMenu;
    }

    @Override
    public void setLinkedMenu(Menu linkedMenu) {
        this.linkedMenu = linkedMenu;
    }

    public static class Presentation {

        public static class Tab {

            public static class Name {
                public static final String Advanced = "MenuItemImpl_Advanced_Tab";
            }

            public static class Order {
                public static final int Advanced = 3000;
            }
        }

        public static class FieldOrder {

            // General Fields
            public static final int LABEL = 2000;
            public static final int MENU_ITEM_TYPE = 3000;
            public static final int ACTION_URL = 4000;
            public static final int IMAGE_URL = 5000;
            public static final int LINKED_MENU = 6000;
            public static final int CATEGORY = 7000;
            public static final int PRODUCT = 8000;
            public static final int PAGE = 9000;
        }
    }

}
