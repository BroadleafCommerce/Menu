package org.broadleafcommerce.menu.copy;
/*
 * #%L
 * BroadleafCommerce Pricelist
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


import org.broadleafcommerce.common.copy.MultiTenantCopier;
import org.broadleafcommerce.common.copy.MultiTenantCopyContext;
import org.broadleafcommerce.common.site.domain.Catalog;
import org.broadleafcommerce.common.site.domain.Site;
import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.domain.MenuItem;


public class MenuCopier extends MultiTenantCopier {

    @Override
    public void copyEntities(MultiTenantCopyContext context) throws Exception {
        Site fromSite = context.getFromSite();
        Catalog fromCatalog = context.getFromCatalog();

        for (Menu obj : readAll(Menu.class, fromSite, fromCatalog)) {
            persistCopyObjectTree(obj.createOrRetrieveCopyInstance(context).getClone(), context);
        }

        for (MenuItem obj : readAll(MenuItem.class, fromSite, fromCatalog)) {
            persistCopyObjectTree(obj.createOrRetrieveCopyInstance(context).getClone(), context);
        }

    }

    /* ************ *
     * COPY METHODS *
     * ************ */
//
//    protected Menu cloneMenu(final MultiTenantCopyContext context,
//            final Menu from) throws Exception {
//        return executeSmartObjectCopy(context, from, new CopyOperation<Menu, Exception>() {
//            @Override
//            public Menu execute() throws Exception {
//                Menu to = from.getClass().newInstance();
//                to.setName(from.getName());
//
//                modifyMenu(from, to);
//
//                return to;
//            }
//
//            @Override
//            public Object getId(Menu obj) {
//                return obj.getId();
//            }
//
//            @Override
//            public Class<Menu> getCacheClass() {
//                return Menu.class;
//            }
//        });
//    }
//
//    protected MenuItem cloneMenuItem(final MultiTenantCopyContext context,
//            final MenuItem from) throws Exception {
//        return executeSmartObjectCopy(context, from, new CopyOperation<MenuItem, Exception>() {
//            @Override
//            public MenuItem execute() throws Exception {
//                MenuItem to = from.getClass().newInstance();
//                to.setActionUrl(from.getActionUrl());
//                to.setImage(from.getImage());
//                to.setLabel(from.getLabel());
//                to.setMenuItemType(from.getMenuItemType());
//                to.setSequence(from.getSequence());
//                to.setAltText(from.getAltText());
//                to.setCustomHtml(from.getCustomHtml());
//
//                if (from.getParentMenu() != null) {
//                    to.setParentMenu(context.getClonedVersion(Menu.class, from.getParentMenu().getId()));
//                }
//                if (from.getLinkedMenu() != null) {
//                    to.setLinkedMenu(context.getClonedVersion(Menu.class, from.getLinkedMenu().getId()));
//                }
//                if (from.getLinkedPage() != null) {
//                    to.setLinkedPage(context.getClonedVersion(Page.class, from.getLinkedPage().getId()));
//                }
//                /*
//                if (from.getLinkedCategory() != null) {
//                    to.setLinkedCategory(context.getClonedVersion(Category.class, from.getLinkedCategory().getId()));
//                }
//                if (from.getLinkedProduct() != null) {
//                    to.setLinkedProduct(context.getClonedVersion(Product.class, from.getLinkedProduct().getId()));
//                }
//                */
//
//                modifyMenuItem(from, to);
//
//                return to;
//            }
//
//            @Override
//            public Object getId(MenuItem obj) {
//                return obj.getId();
//            }
//
//            @Override
//            public Class<MenuItem> getCacheClass() {
//                return MenuItem.class;
//            }
//        });
//    }
//
//    /* ************ *
//     * HOOK METHODS *
//     * ************ */
//
//    /**
//     * Hook method for custom implementations to alter cloning behavior.
//     *
//     * @param from
//     * @param to
//     */
//    protected void modifyMenu(Menu from, Menu to) { }
//
//    /**
//     * Hook method for custom implementations to alter cloning behavior.
//     *
//     * @param from
//     * @param to
//     */
//    protected void modifyMenuItem(MenuItem from, MenuItem to) { }

}
