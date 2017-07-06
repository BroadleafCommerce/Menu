package org.broadleafcommerce.menu.service;

import org.broadleafcommerce.menu.domain.Menu;

/**
 * This service provides metadata for the menu
 *
 * @author Jacob Mitash
 */
public interface LinkedDataService {

    /**
     * Gets the string representation of the metadata for the menu
     * @return JSON-LD of metadata
     * @param menu
     */
    String getLinkedData(Menu menu);

}
