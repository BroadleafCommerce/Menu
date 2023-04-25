package org.broadleafcommerce.menu.processor;

import org.broadleafcommerce.common.web.expression.BroadleafVariableExpression;

import java.util.Map;

public interface MenuExpression extends BroadleafVariableExpression {

    String getName();

    int getPrecedence();

    Map<String, Object> getMenu(String menuId, String menuName);
}
