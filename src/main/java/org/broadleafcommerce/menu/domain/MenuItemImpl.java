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
import org.hibernate.annotations.Type;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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

    @Column(name = "SEQUENCE", precision = 10, scale = 6)
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected BigDecimal sequence;

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

    @Column(name = "ALT_TEXT")
    @AdminPresentation(friendlyName = "MenuItemImpl_AltText",
            order = Presentation.FieldOrder.ALT_TEXT)
    protected String altText;

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

    @ManyToOne(targetEntity = MenuImpl.class)
    @JoinColumn(name = "LINKED_MENU_ID")
    @AdminPresentation(friendlyName = "MenuItemImpl_LinkedMenu",
            order = Presentation.FieldOrder.LINKED_MENU)
    @AdminPresentationToOneLookup()
    protected Menu linkedMenu;

    @ManyToOne(targetEntity = PageImpl.class)
    @JoinColumn(name = "LINKED_PAGE_ID")
    @AdminPresentation(friendlyName = "MenuItemImpl_LinkedPage",
            order = Presentation.FieldOrder.LINKED_PAGE)
    @AdminPresentationToOneLookup()
    protected Page linkedPage;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name = "CUSTOM_HTML", length = Integer.MAX_VALUE - 1)
    @AdminPresentation(friendlyName = "MenuItemImpl_CustomHtml", order = Presentation.FieldOrder.CUSTOM_HTML,
            largeEntry = true,
            fieldType = SupportedFieldType.HTML_BASIC,
            translatable = true)
    protected String customHtml;

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
        String l = DynamicTranslationProvider.getValue(this, "label", label);

        if (MenuItemType.PRODUCT.equals(getMenuItemType()) &&
                getLinkedProduct() != null) {
            l = getLinkedProduct().getName();
        } else if (MenuItemType.CATEGORY.equals(getMenuItemType()) &&
                getLinkedCategory() != null) {
            l = getLinkedCategory().getName();
        } else if (MenuItemType.SUBMENU.equals(getMenuItemType()) &&
                getLinkedMenu() != null) {
            l = getLinkedMenu().getName();
        }

        return l;
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
    public BigDecimal getSequence() {
        return sequence;
    }

    @Override
    public void setSequence(BigDecimal sequence) {
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
    public Menu getLinkedMenu() {
        return linkedMenu;
    }

    @Override
    public void setLinkedMenu(Menu linkedMenu) {
        this.linkedMenu = linkedMenu;
    }

    @Override
    public String getAltText() {
        return altText;
    }

    @Override
    public void setAltText(String altText) {
        this.altText = altText;
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
    public String getCustomHtml() {
        return DynamicTranslationProvider.getValue(this, "customHtml", customHtml);
    }

    @Override
    public void setCustomHtml(String customHtml) {
        this.customHtml = customHtml;
    }

    @Override
    public String getUrl() {
        String url = getActionUrl();

        if (MenuItemType.PRODUCT.equals(getMenuItemType()) &&
                getLinkedProduct() != null ) {
            url = getLinkedProduct().getUrl();
        } else if (MenuItemType.CATEGORY.equals(getMenuItemType()) &&
                getLinkedCategory() != null) {
            url = getLinkedCategory().getUrl();
        } else if (MenuItemType.PAGE.equals(getMenuItemType()) &&
                getLinkedPage() != null) {
            url = getLinkedPage().getFullUrl();
        }

        return url;
    }

    public static class Presentation {

        public static class FieldOrder {

            // General Fields
            public static final int LABEL = 1000;
            public static final int MENU_ITEM_TYPE = 2000;
            public static final int ACTION_URL = 3000;
            public static final int IMAGE_URL = 4000;
            public static final int ALT_TEXT = 5000;
            public static final int LINKED_MENU = 6000;
            public static final int LINKED_PAGE = 7000;
            public static final int CATEGORY =8000;
            public static final int PRODUCT = 9000;
            public static final int CUSTOM_HTML = 10000;
        }
    }

}
