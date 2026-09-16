/*-
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
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
import org.broadleafcommerce.common.persistence.BroadleafIdGenerator;
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
import org.hibernate.Length;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcType;
import java.io.Serial;
import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;

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

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @BroadleafIdGenerator(
            segmentValue = "MenuItemImpl",
            entityName = "org.broadleafcommerce.menu.domain.MenuItemImpl"
    ) {
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
        protected Presentation() {
        }

        public static class FieldOrder {
            protected FieldOrder() {
            }

            // General Fields
            public static final int LABEL = 1000;
            public static final int MENU_ITEM_TYPE = 2000;
            public static final int ACTION_URL = 3000;
            public static final int IMAGE_URL = 4000;
            public static final int ALT_TEXT = 5000;
            public static final int LINKED_MENU = 6000;
            public static final int LINKED_PAGE = 7000;
            public static final int CATEGORY = 8000;
            public static final int PRODUCT = 9000;
            public static final int CUSTOM_HTML = 10000;
        }
    }

}
