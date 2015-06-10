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

            var menuItemType = $("#fields\\'type\\'\\.value", $form).val()

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
