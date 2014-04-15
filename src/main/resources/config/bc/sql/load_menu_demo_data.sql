----------------------------------------------------------------------------------------------------------------------------
-- IMPORTANT NOTE: Assumes that a version of the DemoSite's load_content_structure.sql and
-- load_content_data.sql have been executed first.
-- This file makes modifications to the default Demo content to demonstrate Advanced CMS functionality.
----------------------------------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------------------------------------------------------
-- The range of IDs available for the AdvancedCMS module is [ -22,000 to -22,999 ]
----------------------------------------------------------------------------------------------------------------------------

-- Create Related Products Widget Fields
INSERT INTO BLC_FLD_GROUP(FLD_GROUP_ID, NAME, INIT_COLLAPSED_FLAG) VALUES (7, 'Related Products Widget Fields', FALSE);
INSERT INTO BLC_FLD_DEF(FLD_DEF_ID, NAME, FRIENDLY_NAME, FLD_TYPE, SECURITY_LEVEL, HIDDEN_FLAG, VLDTN_REGEX, VLDTN_ERROR_MSSG_KEY, MAX_LENGTH, COLUMN_WIDTH, TEXT_AREA_FLAG, ENUM_ID, ALLOW_MULTIPLES, FLD_GROUP_ID, FLD_ORDER) VALUES (11, 'relatedProductsType', 'Related Products Type', 'DATA_DRIVEN_ENUMERATION', NULL, FALSE, null, null, 150, '*', FALSE, -22002, FALSE, 7, 0);
INSERT INTO BLC_FLD_DEF(FLD_DEF_ID, NAME, FRIENDLY_NAME, FLD_TYPE, SECURITY_LEVEL, HIDDEN_FLAG, VLDTN_REGEX, VLDTN_ERROR_MSSG_KEY, MAX_LENGTH, COLUMN_WIDTH, TEXT_AREA_FLAG, ENUM_ID, ALLOW_MULTIPLES, FLD_GROUP_ID, FLD_ORDER) VALUES (12, 'relatedProductsMaxNum', 'Max Products', 'DATA_DRIVEN_ENUMERATION', NULL, FALSE, null, null, 150, '*', FALSE, -22003, FALSE, 7, 1);
INSERT INTO BLC_FLD_DEF(FLD_DEF_ID, NAME, FRIENDLY_NAME, FLD_TYPE, SECURITY_LEVEL, HIDDEN_FLAG, VLDTN_REGEX, VLDTN_ERROR_MSSG_KEY, MAX_LENGTH, COLUMN_WIDTH, TEXT_AREA_FLAG, ENUM_ID, ALLOW_MULTIPLES, FLD_GROUP_ID, FLD_ORDER) VALUES (13, 'headerText', 'Header Text', 'STRING', NULL, FALSE, null, null, 150, '*', FALSE, NULL, FALSE, 7, 2);

INSERT INTO BLC_SC_FLD_TMPLT(SC_FLD_TMPLT_ID, NAME) VALUES(4, 'Related Products Template');
INSERT INTO BLC_SC_FLDGRP_XREF(SC_FLD_TMPLT_ID, FLD_GROUP_ID, GROUP_ORDER) VALUES (4,7,0);

-- With Advanced CMS, Structured Content Type should now just be thought of as 1 to 1 with the StructuredContentFieldTemplate
UPDATE BLC_SC SET SC_TYPE_ID = NULL;
DELETE FROM BLC_SC_TYPE WHERE NAME = 'Homepage Banner Ad' OR NAME = 'Right Hand Side Banner Ad' OR NAME = 'Homepage Middle Promo Snippet' OR NAME = 'Homepage Featured Products Title';

-- Developer note: have to use an inner select by name to get IDs as some internal enterprise demo's change the ID's to be different
INSERT INTO BLC_SC_TYPE (SC_TYPE_ID, NAME, DESCRIPTION, SC_FLD_TMPLT_ID) SELECT 1, 'Ad Template', NULL, ft.SC_FLD_TMPLT_ID FROM BLC_SC_FLD_TMPLT ft WHERE ft.NAME = 'Ad Template';
INSERT INTO BLC_SC_TYPE (SC_TYPE_ID, NAME, DESCRIPTION, SC_FLD_TMPLT_ID) SELECT 2, 'HTML Template', NULL, ft.SC_FLD_TMPLT_ID FROM BLC_SC_FLD_TMPLT ft WHERE ft.NAME = 'HTML Template';
INSERT INTO BLC_SC_TYPE (SC_TYPE_ID, NAME, DESCRIPTION, SC_FLD_TMPLT_ID) SELECT 3, 'Message Template', NULL, ft.SC_FLD_TMPLT_ID FROM BLC_SC_FLD_TMPLT ft WHERE ft.NAME = 'Message Template';
INSERT INTO BLC_SC_TYPE (SC_TYPE_ID, NAME, DESCRIPTION, SC_FLD_TMPLT_ID) SELECT 4, 'Related Products Template', NULL, ft.SC_FLD_TMPLT_ID FROM BLC_SC_FLD_TMPLT ft WHERE ft.NAME = 'Related Products Template';

-- Re-assign the above Structured Content Types to the demo loaded structured content items
UPDATE BLC_SC SET SC_TYPE_ID = 1 WHERE CONTENT_NAME = 'Buy One Get One - Twice the Burn';
UPDATE BLC_SC SET SC_TYPE_ID = 1 WHERE CONTENT_NAME = 'Shirt Special - 20% off all shirts';
UPDATE BLC_SC SET SC_TYPE_ID = 1 WHERE CONTENT_NAME = 'Member Special - $10 off next order over $50';
UPDATE BLC_SC SET SC_TYPE_ID = 1 WHERE CONTENT_NAME = 'Promocion - 20% de descuento en todas las camisas';

UPDATE BLC_SC SET SC_TYPE_ID = 2 WHERE CONTENT_NAME = 'Home Page Snippet - Aficionado';
UPDATE BLC_SC SET SC_TYPE_ID = 2 WHERE CONTENT_NAME = 'Home Page Snippet (es) - Aficionado';

UPDATE BLC_SC SET SC_TYPE_ID = 3 WHERE CONTENT_NAME = 'Home Page Featured Products Title';

UPDATE BLC_SC SET SC_TYPE_ID = 1 WHERE CONTENT_NAME = 'RHS - The Essentials Collection';

-- Create new Content Zones and Widgets for the original Demo Site content
INSERT INTO BLC_CONTENT_ZONE_DEF (ZONE_DEF_ID, NAME) VALUES (1, '940px');
INSERT INTO BLC_CONTENT_ZONE_DEF (ZONE_DEF_ID, NAME) VALUES (2, '220px');

---------------------------------------------------
-- HOME PAGE BANNER
---------------------------------------------------
INSERT INTO BLC_CONTENT_ZONE (CONTENT_ZONE_ID, DESCRIPTION, NAME, DEFAULT_CONTENT_ITEM, CONTENT_ZONE_DEF) VALUES (1, 'Homepage Banner Ad Zone', 'Homepage Banner Ad Zone', NULL, 1);
INSERT INTO BLC_WIDGET (WIDGET_ID, CSS_CONTENTS, DESCRIPTION, HANDLE_MULTIPLE_ITEMS, HTML_CONTENTS, JS_CONTENTS, NAME) VALUES (1, NULL, 'Homepage Banner Widget that can display Ads', 0, '<div id="banners" th:if="${sc !=null and sc.fieldValues[''targetUrl''] != null and sc.fieldValues[''imageUrl''] != null}"><a th:href="@{${sc.fieldValues[''targetUrl'']}}"><img th:src="@{${sc.fieldValues[''imageUrl'']}}" /></a></div>', NULL, 'Homepage Banner Ad Widget');
INSERT INTO BLC_SC_TYPE_WIDGET_XREF (SC_TYPE_WIDGET_XREF_ID, WIDGET_ID, SC_TYPE_ID) VALUES (1, 1, 1);
INSERT INTO BLC_CON_ZONE_DEF_WDGT_XREF (CONTENT_ZONE_DEF_XREF_ID, ZONE_DEF_ID, WIDGET_ID) VALUES (1, 1, 1);
INSERT INTO BLC_CONTENT_ITEM (CONTENT_ITEM_ID, DESCRIPTION, NAME, WIDGET_ID, CONTENT_ZONE_ID) VALUES (100, 'Buy One Get One - Twice the Burn', 'Buy One Get One - Twice the Burn', 1, 1);
UPDATE BLC_CONTENT_ZONE SET DEFAULT_CONTENT_ITEM = 100 WHERE NAME = 'Homepage Banner Ad Zone';
INSERT INTO BLC_SC_CONTENT_ITEM_XREF (SC_CONTENT_ITEM_XREF_ID, CONTENT_ITEM_ID, SC_ID) VALUES (1, 100, 100);

INSERT INTO BLC_CONTENT_ITEM (CONTENT_ITEM_ID, DESCRIPTION, NAME, WIDGET_ID, CONTENT_ZONE_ID) VALUES (101, 'Shirt Special - 20% off all shirts', 'Shirt Special - 20% off all shirts', 1, 1);
INSERT INTO BLC_SC_CONTENT_ITEM_XREF (SC_CONTENT_ITEM_XREF_ID, CONTENT_ITEM_ID, SC_ID) VALUES (2, 101, 101);

INSERT INTO BLC_CONTENT_ITEM (CONTENT_ITEM_ID, DESCRIPTION, NAME, WIDGET_ID, CONTENT_ZONE_ID) VALUES (102, 'Member Special - $10 off next order over $50', 'Member Special - $10 off next order over $50', 1, 1);
INSERT INTO BLC_SC_CONTENT_ITEM_XREF (SC_CONTENT_ITEM_XREF_ID, CONTENT_ITEM_ID, SC_ID) VALUES (3, 102, 102);
---------------------------------------------------
-- HOME PAGE SNIPPET
---------------------------------------------------
INSERT INTO BLC_CONTENT_ZONE (CONTENT_ZONE_ID, DESCRIPTION, NAME, DEFAULT_CONTENT_ITEM, CONTENT_ZONE_DEF) VALUES (2, 'Homepage Middle Promo Snippet Zone', 'Homepage Middle Promo Snippet Zone', NULL, 1);
INSERT INTO BLC_WIDGET (WIDGET_ID, CSS_CONTENTS, DESCRIPTION, HANDLE_MULTIPLE_ITEMS, HTML_CONTENTS, JS_CONTENTS, NAME) VALUES (2, NULL, 'Widget that can display custom HTML', 0, '<a th:href="@{/register}" th:if="${sc !=null and sc.fieldValues[''htmlContent''] !=null}" style="text-decoration:none"><div id="home_feature" th:utext="${sc.fieldValues[''htmlContent'']}"></div></a>', NULL, 'HTML Widget');
INSERT INTO BLC_SC_TYPE_WIDGET_XREF (SC_TYPE_WIDGET_XREF_ID, WIDGET_ID, SC_TYPE_ID) VALUES (2, 2, 2);
INSERT INTO BLC_CON_ZONE_DEF_WDGT_XREF (CONTENT_ZONE_DEF_XREF_ID, ZONE_DEF_ID, WIDGET_ID) VALUES (2, 1, 2);
INSERT INTO BLC_CONTENT_ITEM (CONTENT_ITEM_ID, DESCRIPTION, NAME, WIDGET_ID, CONTENT_ZONE_ID) VALUES (110, 'Homepage Middle Promo HTML Snippet', 'Homepage Middle Promo HTML Snippet', 2, 2);
UPDATE BLC_CONTENT_ZONE SET DEFAULT_CONTENT_ITEM = 110 WHERE NAME = 'Homepage Middle Promo Snippet Zone';

INSERT INTO BLC_SC_CONTENT_ITEM_XREF (SC_CONTENT_ITEM_XREF_ID, CONTENT_ITEM_ID, SC_ID) VALUES (4, 110, 110);

---------------------------------------------------
-- HOME PAGE FEATURED PRODUCTS MESSAGE
---------------------------------------------------
INSERT INTO BLC_CONTENT_ZONE (CONTENT_ZONE_ID, DESCRIPTION, NAME, DEFAULT_CONTENT_ITEM, CONTENT_ZONE_DEF) VALUES (3, 'Homepage Featured Products Title Zone', 'Homepage Featured Products Title Zone', NULL, 1);
INSERT INTO BLC_WIDGET (WIDGET_ID, CSS_CONTENTS, DESCRIPTION, HANDLE_MULTIPLE_ITEMS, HTML_CONTENTS, JS_CONTENTS, NAME) VALUES (3, NULL, 'Widget that can display custom Text', 0, '<div th:if="${sc !=null and sc.fieldValues[''messageText''] !=null}" class="title_bar" th:text="${sc.fieldValues[''messageText'']}"></div>', NULL, 'Text Widget');
INSERT INTO BLC_SC_TYPE_WIDGET_XREF (SC_TYPE_WIDGET_XREF_ID, WIDGET_ID, SC_TYPE_ID) VALUES (3, 3, 3);
INSERT INTO BLC_CON_ZONE_DEF_WDGT_XREF (CONTENT_ZONE_DEF_XREF_ID, ZONE_DEF_ID, WIDGET_ID) VALUES (3, 1, 3);
INSERT INTO BLC_CONTENT_ITEM (CONTENT_ITEM_ID, DESCRIPTION, NAME, WIDGET_ID, CONTENT_ZONE_ID) VALUES (130, 'Homepage Featured Products Title', 'Homepage Featured Products Title', 3, 3);
UPDATE BLC_CONTENT_ZONE SET DEFAULT_CONTENT_ITEM = 130 WHERE NAME = 'Homepage Featured Products Title Zone';

INSERT INTO BLC_SC_CONTENT_ITEM_XREF (SC_CONTENT_ITEM_XREF_ID, CONTENT_ITEM_ID, SC_ID) VALUES (5, 130, 130);

---------------------------------------------------
-- RIGHT HAND SIDE - AD
---------------------------------------------------
INSERT INTO BLC_CONTENT_ZONE (CONTENT_ZONE_ID, DESCRIPTION, NAME, DEFAULT_CONTENT_ITEM, CONTENT_ZONE_DEF) VALUES (4, 'Right Hand Side Banner Ad Zone', 'Right Hand Side Banner Ad Zone', NULL, 2);
INSERT INTO BLC_WIDGET (WIDGET_ID, CSS_CONTENTS, DESCRIPTION, HANDLE_MULTIPLE_ITEMS, HTML_CONTENTS, JS_CONTENTS, NAME) VALUES (4, NULL, 'Right Hand Side Banner Widget that can display Ads', 0, '<a th:if="${sc !=null and sc.fieldValues[''targetUrl''] !=null and sc.fieldValues[''imageUrl''] !=null}"  th:href="@{${sc.fieldValues[''targetUrl'']}}"><img th:src="@{${sc.fieldValues[''imageUrl'']}}" /></a>', NULL, 'Right Hand Side Banner Ad Widget');
INSERT INTO BLC_SC_TYPE_WIDGET_XREF (SC_TYPE_WIDGET_XREF_ID, WIDGET_ID, SC_TYPE_ID) VALUES (4, 4, 1);
INSERT INTO BLC_CON_ZONE_DEF_WDGT_XREF (CONTENT_ZONE_DEF_XREF_ID, ZONE_DEF_ID, WIDGET_ID) VALUES (4, 2, 4);
INSERT INTO BLC_CONTENT_ITEM (CONTENT_ITEM_ID, DESCRIPTION, NAME, WIDGET_ID, CONTENT_ZONE_ID) VALUES (140, 'Right Hand Side Banner Ad', 'Right Hand Side Banner Ad', 4, 4);
UPDATE BLC_CONTENT_ZONE SET DEFAULT_CONTENT_ITEM = 140 WHERE NAME = 'Right Hand Side Banner Ad Zone';

INSERT INTO BLC_SC_CONTENT_ITEM_XREF (SC_CONTENT_ITEM_XREF_ID, CONTENT_ITEM_ID, SC_ID) VALUES (6, 140, 140);

---------------------------------------------------
-- RIGHT HAND SIDE - RELATED PRODUCTS
---------------------------------------------------
-- Content Item
INSERT INTO BLC_SC (SC_ID, CREATED_BY, DATE_CREATED, DATE_UPDATED, UPDATED_BY, CONTENT_NAME, OFFLINE_FLAG, PRIORITY, LOCALE_CODE, SC_TYPE_ID) VALUES (150, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'RHS - Related Products', FALSE, 6, 'en', 4);

-- Fields
INSERT INTO BLC_SC_FLD (SC_FLD_ID, DATE_CREATED, FLD_KEY, CREATED_BY, VALUE) VALUES (14, CURRENT_TIMESTAMP, 'relatedProductsType', 1, 'FEATURED');
INSERT INTO BLC_SC_FLD (SC_FLD_ID, DATE_CREATED, FLD_KEY, CREATED_BY, VALUE) VALUES (15, CURRENT_TIMESTAMP, 'relatedProductsMaxNum', 1, '3');
INSERT INTO BLC_SC_FLD (SC_FLD_ID, DATE_CREATED, FLD_KEY, CREATED_BY, VALUE) VALUES (16, CURRENT_TIMESTAMP, 'headerText', 1, 'Featured Products');

-- Field XREF
INSERT INTO BLC_SC_FLD_MAP (SC_ID, SC_FLD_ID, MAP_KEY) VALUES (150, 14, 'relatedProductsType');
INSERT INTO BLC_SC_FLD_MAP (SC_ID, SC_FLD_ID, MAP_KEY) VALUES (150, 15, 'relatedProductsMaxNum');
INSERT INTO BLC_SC_FLD_MAP (SC_ID, SC_FLD_ID, MAP_KEY) VALUES (150, 16, 'headerText');

-- Content Zones and Widgets
INSERT INTO BLC_CONTENT_ZONE (CONTENT_ZONE_ID, DESCRIPTION, NAME, DEFAULT_CONTENT_ITEM, CONTENT_ZONE_DEF) VALUES (5, 'Right Hand Side Related Products Zone', 'Right Hand Side Related Products Zone', NULL, 2);
INSERT INTO BLC_WIDGET (WIDGET_ID, CSS_CONTENTS, DESCRIPTION, HANDLE_MULTIPLE_ITEMS, HTML_CONTENTS, JS_CONTENTS, NAME) VALUES (5, NULL, 'Right Hand Side Related Products Widget', 0, '<section id="right_column" th:if="${sc !=null and sc.fieldValues[''relatedProductsType''] !=null and sc.fieldValues[''relatedProductsMaxNum''] !=null}"><header th:if="${sc.fieldValues[''headerText''] !=null}"><span th:text="${sc.fieldValues[''headerText'']}">Featured Products</span></header><div id="options"><div class="section" th:object="${category}"><blc:related_products categoryId="*{id}" type="${sc.fieldValues[''relatedProductsType'']}" quantity="${sc.fieldValues[''relatedProductsMaxNum'']}"/><ul id="featured_products" class="group"><li th:each="product : ${products}" th:object="${product}" th:include="catalog/partials/productListItem" class="product_container"></li></ul></div></div></section>', NULL, 'Right Hand Side Related Products Widget');
INSERT INTO BLC_SC_TYPE_WIDGET_XREF (SC_TYPE_WIDGET_XREF_ID, WIDGET_ID, SC_TYPE_ID) VALUES (5, 5, 4);
INSERT INTO BLC_CON_ZONE_DEF_WDGT_XREF (CONTENT_ZONE_DEF_XREF_ID, ZONE_DEF_ID, WIDGET_ID) VALUES (5, 2, 5);
INSERT INTO BLC_CONTENT_ITEM (CONTENT_ITEM_ID, DESCRIPTION, NAME, WIDGET_ID, CONTENT_ZONE_ID) VALUES (150, 'Right Hand Side Related Products', 'Right Hand Side Related Products', 5, 5);
UPDATE BLC_CONTENT_ZONE SET DEFAULT_CONTENT_ITEM = 150 WHERE NAME = 'Right Hand Side Related Products Zone';

INSERT INTO BLC_SC_CONTENT_ITEM_XREF (SC_CONTENT_ITEM_XREF_ID, CONTENT_ITEM_ID, SC_ID) VALUES (7, 150, 150);

-- Menu Items
INSERT INTO BLC_CMS_MENU (MENU_ID, NAME, LABEL,  ACTION_URL) VALUES (-22001, 'Header Nav', NULL, NULL);

-- Example using all links 
INSERT INTO BLC_CMS_MENU_ITEM (MENU_ITEM_ID, PARENT_MENU_ID, SEQUENCE, MENU_ITEM_TYPE, NAME, LABEL, ACTION_URL) VALUES (-22001, -22001, 0,  'LINK', 'Home', NULL, '/');
INSERT INTO BLC_CMS_MENU_ITEM (MENU_ITEM_ID, PARENT_MENU_ID, SEQUENCE, MENU_ITEM_TYPE, NAME, LABEL, ACTION_URL) VALUES (-22002, -22001, 1,  'LINK', 'Hot Sauces', NULL, '/hot-sauces');
INSERT INTO BLC_CMS_MENU_ITEM (MENU_ITEM_ID, PARENT_MENU_ID, SEQUENCE, MENU_ITEM_TYPE, NAME, LABEL, ACTION_URL) VALUES (-22003, -22001, 2,  'LINK', 'Merchandise', NULL, '/merchandise');
INSERT INTO BLC_CMS_MENU_ITEM (MENU_ITEM_ID, PARENT_MENU_ID, SEQUENCE, MENU_ITEM_TYPE, NAME, LABEL, ACTION_URL) VALUES (-22004, -22001, 3,  'LINK', 'Clearance', NULL, '/clearance');
INSERT INTO BLC_CMS_MENU_ITEM (MENU_ITEM_ID, PARENT_MENU_ID, SEQUENCE, MENU_ITEM_TYPE, NAME, LABEL, ACTION_URL) VALUES (-22005, -22001, 4,  'LINK', 'FAQs', NULL, '/faq');