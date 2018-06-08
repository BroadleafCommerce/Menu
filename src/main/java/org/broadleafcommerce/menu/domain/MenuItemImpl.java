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
package org.broadleafcommerce.menu.domain;

import org.broadleafcommerce.cms.page.domain.Page;
import org.broadleafcommerce.cms.page.domain.PageImpl;
import org.broadleafcommerce.common.copy.CreateResponse;
import org.broadleafcommerce.common.copy.MultiTenantCopyContext;
import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import org.broadleafcommerce.common.extensibility.jpa.copy.ProfileEntity;
import org.broadleafcommerce.common.i18n.service.DynamicTranslationProvider;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.common.presentation.PopulateToOneFieldsEnum;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.broadleafcommerce.common.presentation.override.AdminPresentationMergeEntry;
import org.broadleafcommerce.common.presentation.override.AdminPresentationMergeOverride;
import org.broadleafcommerce.common.presentation.override.AdminPresentationMergeOverrides;
import org.broadleafcommerce.common.presentation.override.PropertyType;
import org.broadleafcommerce.menu.type.MenuItemType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
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
@AdminPresentationClass(friendlyName = "MenuItemImpl", populateToOneFields = PopulateToOneFieldsEnum.TRUE)
@AdminPresentationMergeOverrides({
        @AdminPresentationMergeOverride(name = "image.", mergeEntries =
        @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.EXCLUDED,
                booleanOverrideValue = true)
        ),
        @AdminPresentationMergeOverride(name = "image.url", mergeEntries = {
                @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.EXCLUDED, booleanOverrideValue = false),
                @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.ORDER, intOverrideValue = MenuItemImpl.Presentation.FieldOrder.IMAGE_URL),
                @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.FRIENDLYNAME, overrideValue = "MenuItemImpl_Image"),
                @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.REQUIREDOVERRIDE, overrideValue = "NOT_REQUIRED")
        })
})
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps = true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class MenuItemImpl implements MenuItem, ProfileEntity {

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

    @ManyToOne(optional = true, targetEntity = MenuImpl.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PARENT_MENU_ID")
    @AdminPresentation(excluded = true)
    protected Menu parentMenu;

    @Column(name = "ACTION_URL")
    @AdminPresentation(friendlyName = "MenuItemImpl_ActionUrl",
            order = Presentation.FieldOrder.ACTION_URL)
    protected String actionUrl;

    @Column(name = "IMAGE_URL")
    @AdminPresentation(friendlyName = "MenuItemImpl_ImageUrl",
            order = Presentation.FieldOrder.IMAGE_URL,
            fieldType = SupportedFieldType.ASSET_LOOKUP)
    protected String imageUrl;

    @Column(name = "ALT_TEXT")
    @AdminPresentation(friendlyName = "MenuItemImpl_AltText",
            order = Presentation.FieldOrder.ALT_TEXT)
    protected String altText;

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
    @Type(type = "org.hibernate.type.MaterializedClobType")
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
    public String getDerivedUrl() {
        String url = getActionUrl();

        if (MenuItemType.PAGE.equals(getMenuItemType()) && getLinkedPage() != null) {
            url = getLinkedPage().getFullUrl();
        }

        return url;
    }

    @Override
    public String getDerivedLabel() {
        String l = getLabel();

        if (l == null) {
            if (MenuItemType.SUBMENU.equals(getMenuItemType()) && getLinkedMenu() != null) {
                l = getLinkedMenu().getName();
            }
        }

        return l;
    }

    @Override
    public <G extends MenuItem> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }
        MenuItem cloned = createResponse.getClone();
        cloned.setLabel(label);
        cloned.setMenuItemType(getMenuItemType());
        cloned.setSequence(sequence);
        if (parentMenu != null) {
            cloned.setParentMenu(parentMenu.createOrRetrieveCopyInstance(context).getClone());
        }
        cloned.setActionUrl(actionUrl);
        cloned.setImageUrl(imageUrl);
        cloned.setAltText(altText);
        if (linkedMenu != null) {
            cloned.setLinkedMenu(linkedMenu.createOrRetrieveCopyInstance(context).getClone());
        }
        if (linkedPage != null) {
            cloned.setLinkedPage(linkedPage.createOrRetrieveCopyInstance(context).getClone());
        }
        cloned.setCustomHtml(customHtml);
        return createResponse;
    }

    public static class Presentation {
        private Presentation() {
        }

        public static class FieldOrder {
            private FieldOrder() {
            }

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
