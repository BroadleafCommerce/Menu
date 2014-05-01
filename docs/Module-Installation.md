Steps to enable this module in your custom Broadleaf Commerce project. These steps are based on projects that are
similar to the Broadleaf Commerce demo application which contains a core, site, and admin project which are all contained in a "parent project".

> Note: that the 3.2 version of the demo application already has this menu module included.

## Steps

**Step 1.** Add the following to the dependency management section of your **parent** `pom.xml`:
_The parent pom is the one located in the root directory of the Broadleaf Commerce project._

```xml
    <dependency>
        <groupId>org.broadleafcommerce</groupId>
        <artifactId>broadleaf-menu</artifactId>
        <version>1.0.0-GA</version>
        <type>jar</type>
        <scope>compile</scope>
    </dependency>
```

**Step 2.** Pull this dependency into your `core/pom.xml`:

```xml
    <dependency>
        <groupId>org.broadleafcommerce</groupId>
        <artifactId>broadleaf-menu</artifactId>
    </dependency>
```

**Step 3.** Update the `patchConfigLocation` files in your `admin/web.xml` file:

```xml
    classpath:/bl-menu-applicationContext.xml
    classpath:/bl-menu-admin-applicationContext.xml
```
> Note: This line should go before the `classpath:/applicationContext.xml` line


**Step 4.** Update the `contextConfigLocation` for `DispatcherServlet` files in your `admin/web.xml` file:

```xml
    classpath:/bl-menu-admin-applicationContext-servlet.xml
```

> Note: This line should go just before the `/WEB-INF/applicationContext-servlet-admin.xml` line


**Step 5.** Update the `patchConfigLocation` files in your `site/web.xml` file:

```xml
    classpath:/bl-menu-applicationContext.xml
```
> Note: This line should go before the `classpath:/applicationContext.xml` line


## Data Changes

### Schema Changes

If you are allowing hibernate to create and modify your tables, this will be done automatically when you restart with the new configuration settings.

Otherwise, you will need to generate the SQL to customize your Broadleaf implementation. See the [Broadleaf Schema Upgrade Documentation](http://docs.broadleafcommerce.org/core/current/broadleaf-data-upgrade-process) for details.

### Admin Security Changes

To create the Menu management section in the Broadleaf admin, you will need to load new permissions. The recommended changes are located in the following files:

```
/config/bc/sql/load_menu_admin_security.sql
```

> Note: In development, you can automatically load this SQL files by adding it to the blPU.hibernate.hbm2ddl.import\_files property in the development-shared.properties file.
