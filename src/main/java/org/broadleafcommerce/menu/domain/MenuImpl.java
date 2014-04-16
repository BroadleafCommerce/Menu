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

import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransform;
import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransformMember;
import org.broadleafcommerce.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import org.broadleafcommerce.common.i18n.domain.TranslatedEntity;
import org.broadleafcommerce.common.i18n.service.DynamicTranslationProvider;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationCollection;
import org.broadleafcommerce.common.presentation.client.AddMethodType;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BLC_CMS_MENU")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blCMSElements")
@AdminPresentationClass(friendlyName = "MenuImpl")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps = true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class MenuImpl implements Menu {

    private static final long serialVersionUID = 1L;

    static {
        new TranslatedEntity("org.broadleafcommerce.menu.domain.Menu", "Menu");
    }

    @Id
    @GeneratedValue(generator = "MenuId")
    @GenericGenerator(
            name = "MenuId",
            strategy = "org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @Parameter(name = "segment_value", value = "MenuImpl"),
                    @Parameter(name = "entity_name", value = "org.broadleafcommerce.menu.domain.MenuImpl")
            })
    @Column(name = "MENU_ID")
    protected Long id;

    @Column(name = "NAME", nullable = false)
    @AdminPresentation(friendlyName = "MenuImpl_Name",
            order = Presentation.FieldOrder.NAME,
            gridOrder = Presentation.FieldOrder.NAME,
            prominent = true)
    protected String name;

    @OneToMany(mappedBy = "parentMenu", targetEntity = MenuItemImpl.class, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @AdminPresentationCollection(friendlyName = "MenuItemImpl_MenuItems",
            addType = AddMethodType.PERSIST)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blCMSElements")
    @BatchSize(size = 50)
    @OrderBy(value = "sequence")
    protected List<MenuItem> menuItems = new ArrayList<MenuItem>(20);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    @Override
    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public static class Presentation {

        public static class Tab {

            public static class Name {

                public static final String Advanced = "MenuImpl_Advanced_Tab";
            }

            public static class Order {

                public static final int Advanced = 2000;
            }
        }

        public static class FieldOrder {

            // General Fields
            public static final int NAME = 1000;
            public static final int MENU_ITEMS = 3000;
        }
    }
}
