/**
 * 
 */
package org.broadleafcommerce.menu.config;

import org.broadleafcommerce.common.module.BroadleafModuleRegistration;

/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class MenuModuleRegistration implements BroadleafModuleRegistration {

    public static final String MODULE_NAME = "Menu";
    
    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
