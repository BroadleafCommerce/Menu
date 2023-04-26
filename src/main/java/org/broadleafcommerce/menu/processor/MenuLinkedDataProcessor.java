package org.broadleafcommerce.menu.processor;

import org.broadleafcommerce.common.web.expression.BroadleafVariableExpression;
import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.service.LinkedDataService;
import org.broadleafcommerce.menu.service.MenuService;
import org.broadleafcommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("blMenuLinkedDataProcessor")
@ConditionalOnTemplating
public class MenuLinkedDataProcessor implements BroadleafVariableExpression {

    @Resource(name = "blMenuService")
    protected MenuService menuService;

    @Resource(name = "blMenuLinkedDataService")
    protected LinkedDataService linkedDataService;

    @Override
    public String getName() {
        return "menuLinkedData";
    }

    public String getMenuLinkedData(String menuName, String menuId) {
        final Menu menu;

        if (menuId != null) {
            menu = menuService.findMenuById(Long.parseLong(menuId));
        } else {
            menu = menuService.findMenuByName(menuName);
        }

        return menu != null ? this.linkedDataService.getLinkedData(menu) : "";
    }
}
