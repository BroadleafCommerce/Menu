/*
 * #%L
 * BroadleafCommerce Framework
 * %%
 * Copyright (C) 2009 - 2013 Broadleaf Commerce
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

import org.broadleafcommerce.common.copy.CreateResponse;
import org.broadleafcommerce.common.copy.MultiTenantCloneable;
import org.broadleafcommerce.common.copy.MultiTenantCopyContext;
import org.broadleafcommerce.common.extensibility.jpa.clone.ClonePolicy;
import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.common.media.domain.MediaImpl;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.PopulateToOneFieldsEnum;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.broadleafcommerce.common.util.UnknownUnwrapTypeException;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BLC_CMS_MENU_ITEM_MEDIA_MAP")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCMSElements")
@AdminPresentationClass(excludeFromPolymorphism = false, populateToOneFields = PopulateToOneFieldsEnum.TRUE)
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class MenuItemMediaXrefImpl implements MenuItemMediaXref, Media, MultiTenantCloneable<MenuItemMediaXref> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    public MenuItemMediaXrefImpl(MenuItem menuItem, Media media, String key) {
        this.menuItem = menuItem;
        this.media = media;
        this.key = key;
    }

    public MenuItemMediaXrefImpl() {
        //support default constructor for Hibernate
    }

    @Id
    @GeneratedValue(generator= "MenuItemMediaId")
    @GenericGenerator(
        name="MenuItemMediaId",
        strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="MenuItemMediaXrefImpl"),
            @Parameter(name="entity_name", value="org.broadleafcommerce.menu.domain.MenuItemMediaXrefImpl")
        }
    )
    @Column(name = "MENU_ITEM_MEDIA_ID")
    protected Long id;

    //for the basic collection join entity - don't pre-instantiate the reference (i.e. don't do myField = new MyFieldImpl())
    @ManyToOne(targetEntity = MenuItemImpl.class, optional=false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "MENU_ITEM_ID")
    @AdminPresentation(excluded = true)
    protected MenuItem menuItem;

    //for the basic collection join entity - don't pre-instantiate the reference (i.e. don't do myField = new MyFieldImpl())
    @ManyToOne(targetEntity = MediaImpl.class, cascade = {CascadeType.ALL})
    @JoinColumn(name = "MEDIA_ID")
    @ClonePolicy
    protected Media media;

    @Column(name = "MAP_KEY", nullable=false)
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected String key;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public MenuItem getMenuItem() {
        return menuItem;
    }

    @Override
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public Media getMedia() {
        return media;
    }

    @Override
    public void setMedia(Media media) {
        this.media = media;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getUrl() {
        createEntityInstance();
        return media.getUrl();
    }

    @Override
    public void setUrl(String url) {
        createEntityInstance();
        media.setUrl(url);
    }

    @Override
    public String getTitle() {
        createEntityInstance();
        return media.getTitle();
    }

    @Override
    public void setTitle(String title) {
        createEntityInstance();
        media.setTitle(title);
    }

    @Override
    public String getAltText() {
        createEntityInstance();
        return media.getAltText();
    }

    @Override
    public void setAltText(String altText) {
        createEntityInstance();
        media.setAltText(altText);
    }

    @Override
    public String getTags() {
        createEntityInstance();
        return media.getTags();
    }

    @Override
    public void setTags(String tags) {
        createEntityInstance();
        media.setTags(tags);
    }

    protected void createEntityInstance() {
        if (media == null) {
            media = new MediaImpl();
        }
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return Media.class.equals(unwrapType);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (isUnwrappableAs(unwrapType)) {
            return (T) media;
        }
        throw new UnknownUnwrapTypeException(unwrapType);
    }

    @Override
    public <G extends MenuItemMediaXref> CreateResponse<G> createOrRetrieveCopyInstance(MultiTenantCopyContext context) throws CloneNotSupportedException {
        CreateResponse<G> createResponse = context.createOrRetrieveCopyInstance(this);
        if (createResponse.isAlreadyPopulated()) {
            return createResponse;
        }

        MenuItemMediaXrefImpl cloned = (MenuItemMediaXrefImpl)createResponse.getClone();
        if (media != null) {
            cloned.setMedia(((MediaImpl) media).createOrRetrieveCopyInstance(context).getClone());
        }
        cloned.setAltText(getAltText());
        cloned.setKey(key);
        if (menuItem != null) {
            cloned.setMenuItem(menuItem.createOrRetrieveCopyInstance(context).getClone());
        }
        cloned.setTags(getTags());
        cloned.setUrl(getUrl());
        cloned.setTitle(getTitle());
        return createResponse;
    }
}
