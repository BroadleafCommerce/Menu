/*
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2016 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License” located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License” located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package org.broadleafcommerce.menu.domain;

import org.broadleafcommerce.common.i18n.domain.TranslatedEntity;
import org.springframework.stereotype.Component;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@Component("blMenuTranslatedEntity")
public class MenuTranslatedEntity extends TranslatedEntity {

    private static final long serialVersionUID = -1;

    public static final TranslatedEntity MENU = new TranslatedEntity("org.broadleafcommerce.menu.domain.Menu", "Menu");
    public static final TranslatedEntity MENU_ITEM = new TranslatedEntity("org.broadleafcommerce.menu.domain.MenuItem", "MenuItem");

}
