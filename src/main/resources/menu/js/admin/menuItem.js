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
/*
 * Code to hide and show fields on Menu Item Add form.
 */
(function($, BLCAdmin) {

    BLCAdmin.menuItem = {

        addOnChangeTriggers : function($form) {
            $form.find("#fields\\'type\\'\\.value").on('change', function() {
                BLCAdmin.menuItem.initializeMenuItemFormFields($form);
            });
        },

        /**
         * Show or hide certain Menu Item fields based on the currently selected Menu Item Type
         */
        initializeMenuItemFormFields : function($form) {

            var menuItemType = $("#fields\\'type\\'\\.value", $form).val();

            // Initialize relevant fields
            var $actionUrl        = $form.find('#field-actionUrl');
            var $image            = $form.find('#field-image');
            var $altText          = $form.find('#field-altText');
            var $linkedMenu       = $form.find('#field-linkedMenu');
            var $linkedCategory   = $form.find('#field-linkedCategory');
            var $linkedProduct    = $form.find('#field-linkedProduct');
            var $linkedPage       = $form.find('#field-linkedPage');
            var $customHtml       = $form.find('#field-customHtml');

            // Hide everything
            $actionUrl.addClass('hidden');
            $image.addClass('hidden');
            $altText.addClass('hidden');
            $linkedMenu.addClass('hidden');
            $linkedPage.addClass('hidden');
            $customHtml.addClass('hidden');

            switch (menuItemType) {
                case "CATEGORY":
                    $actionUrl.removeClass('hidden');
                    break;
                case "SUBMENU":
                    $actionUrl.removeClass('hidden');
                    $linkedMenu.removeClass('hidden');
                    break;
                case "PRODUCT":
                    $actionUrl.removeClass('hidden');
                    break;
                case "PAGE":
                    $linkedPage.removeClass('hidden');
                    break;
                case "LINK":
                    $actionUrl.removeClass('hidden');
                    $image.removeClass('hidden');
                    $altText.removeClass('hidden');
                    break;
                case "CUSTOM":
                    $customHtml.removeClass('hidden');
                    break;
            }
        }
    };

    BLCAdmin.addInitializationHandler(function($container) {
        var $form = $container.closest('form.menu-item-form');
        if ($form) {
            BLCAdmin.menuItem.addOnChangeTriggers($form);
            BLCAdmin.menuItem.initializeMenuItemFormFields($form);
        }
    });


})(jQuery, BLCAdmin);
