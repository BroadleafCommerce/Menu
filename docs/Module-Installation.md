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
    /config/bc/sql/load_menu_admin_security.sql
```

> If you are creating the database using `blPU.hibernate.hbm2ddl.auto=create`, then you must ensure that you have set `import.sql.enabled=true` as well. If not, then you will need to run the SQL in the above file against your database to setup the proper Admin permissions.

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
