# Data Model

[![Menu](MenuERD.png)](\_img/MenuERD.png)

### Tables


| Table                            | Related Entity                                                                                 | Description                                                                                         |
| :------------------------------- | :--------------------------------------------------------------------------------------------- | :-------------------------------------------------------------------------------------------------- |
| `BLC_CMS_MENU`                   | ^[javadoc:org.broadleafcommerce.menu.domain.Menu]                                              | Represents a menu, typically to drive the display of navigation on a website.                       |
| `BLC_CMS_MENU_ITEM`              | ^[javadoc:org.broadleafcommerce.menu.domain.MenuItem]                                          | Represents a menu item. Can be of different types: Link, Category, Page, Submenu, Product, Custom...|
