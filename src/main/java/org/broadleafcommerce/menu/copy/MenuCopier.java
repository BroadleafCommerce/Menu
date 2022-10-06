/*-
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2022 Broadleaf Commerce
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
package org.broadleafcommerce.menu.copy;

import org.broadleafcommerce.common.copy.MultiTenantCopier;
import org.broadleafcommerce.common.copy.MultiTenantCopyContext;
import org.broadleafcommerce.common.site.domain.Catalog;
import org.broadleafcommerce.common.site.domain.Site;
import org.broadleafcommerce.menu.domain.MenuImpl;

/**
 * Clone menu entities
 *
 * @author Jeff Fischer
 */
public class MenuCopier extends MultiTenantCopier {

    @Override
    public void copyEntities(MultiTenantCopyContext context) throws Exception {
        Site fromSite = context.getFromSite();
        Catalog fromCatalog = context.getFromCatalog();

        copyEntitiesOfType(MenuImpl.class, fromSite, fromCatalog, context);

    }

}
