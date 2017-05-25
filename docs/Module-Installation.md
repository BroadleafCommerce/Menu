# Module Installation

Steps to enable this module in your custom Broadleaf Commerce project

## Steps

**Step 1** Pull this dependency into your `core/pom.xml`:

```xml
<dependency>
    <groupId>org.broadleafcommerce</groupId>
    <artifactId>broadleaf-menu</artifactId>
</dependency>
```

> This assumes that you are using the Broadleaf BOM that pins all version information. If not, you will need to also add a `<version>` qualifier

## Data Changes

### Schema Changes

To add all of the necessary database tables and columns for this module, please follow the [Liquibase update documentation](https://www.broadleafcommerce.com/docs/core/current/appendix/managing-db-versions-migrations-with-liquibase).

### Admin Security Changes

The data in the following SQL file is required to establish Admin sections and permissions for this module:

```
classpath:/config/bc/sql/load_menu_admin_security.sql
```

This file is automatically included if you have set `blPU.hibernate.hbm2ddl.auto=create` and you have not set `import.sql.enabled=false` in your properties files. If you are not using Hibernate's auto DDL process and are using Liquibase, you can add a new `changeSet` that references this file:


```xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog" xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">
    <changeSet author="broadleaf" id="some-unique-id">
	    <sqlFile path="config/bc/sql/load_menu_admin_security.sql" encoding="utf8" stripComments="true" />
    </changeSet>
</databaseChangeLog>
```

Finally, you can unpack the downloaded `.jar` file and look at the files in the `config/bc/sql` folder to execute this sql manually.

### Demo Data

Demo data that sets up a menu structure based on categories is included in `classpath:/config/bc/sql/demo`. 

## View Changes

If you are using the Heat Clinic demo as a starting point. You can utilize the new Thymeleaf MenuProcessor to draw your navigation.
Add something like the following to your `nav.html` file:

```html
<blc:menu resultVar="menuItems" menuName="Header Nav" />
<ul th:if="${menuItems}">
    <li th:each="menuItem : ${menuItems}">

        <a th:href="@{${menuItem.url}}" th:class="${menuItemStat.first}? 'home'">
            <span th:text="${menuItem.label}"></span>
        </a>
        <ul th:if="${menuItem.submenu != null}" class="sub-menu">
            <li th:each="submenuItem : ${menuItem.submenu}">
                <a th:href="@{${submenuItem.url}}" th:text="${submenuItem.label}"></a>
            </li>
        </ul>

    </li>
</ul>
```
