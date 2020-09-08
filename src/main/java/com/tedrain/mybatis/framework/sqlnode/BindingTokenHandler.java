package com.tedrain.mybatis.framework.sqlnode;

import com.tedrain.mybatis.framework.utils.OgnlUtils;
import com.tedrain.mybatis.framework.utils.SimpleTypeRegistry;
import com.tedrain.mybatis.framework.utils.TokenHandler;

public class BindingTokenHandler implements TokenHandler {
    private DynamicContext context;

    public BindingTokenHandler(DynamicContext context) {
        this.context = context;
    }

    /**
     * 使用参数值替换 ${}
     *
     * @param content 是${}中的内容
     * @return 用来替换${}的值
     */
    public String handleToken(String content) {
        Object parameter = context.getBindings().get("_parameter");
        if (parameter == null) {
            return "";
        } else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) {
            return parameter.toString();
        }

        // POJO 类型或者 Map 类型
        Object value = OgnlUtils.getValue(content, parameter);
        return value == null ? "" : value.toString();
    }
}
