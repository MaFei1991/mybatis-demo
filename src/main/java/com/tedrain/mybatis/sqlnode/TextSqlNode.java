package com.tedrain.mybatis.sqlnode;

import com.tedrain.mybatis.sqlnode.iface.SqlNode;
import com.tedrain.mybatis.utils.GenericTokenParser;
import com.tedrain.mybatis.utils.OgnlUtils;
import com.tedrain.mybatis.utils.SimpleTypeRegistry;
import com.tedrain.mybatis.utils.TokenHandler;

public class TextSqlNode implements SqlNode {
    // 封装未解析的sql文本信息
    private String sqlText;

    public TextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }


    public void apply(DynamicContext context) {
        // 用来处理${}中的参数
        // select * from user where name like '${name}%'


        // 用来解析SQL文本中的#{}或者${}
        GenericTokenParser tokenParser = new GenericTokenParser("${","}",new BindingTokenHandler(context));

        // JDBC可以直接执行的SQL语句
        String sql = tokenParser.parse(sqlText);

        context.appendSql(sql);
    }
    //Java是最好的语言

    /**
     * 判断封装的SQL文本是否带有${}
     * @return
     */
    public boolean isDynamic(){
        return (sqlText.indexOf("${") > -1) ;
    }

    class BindingTokenHandler implements TokenHandler {

        private DynamicContext context;

        public BindingTokenHandler(DynamicContext context) {
            this.context = context;
        }

        /**
         * 使用参数值来替换${}
         * @param content 是${}中的内容
         * @return 用来替换${}的值
         */
        public String handleToken(String content) {
            // 获取参数值
            Object parameter = context.getBindings().get("_parameter");

            if (parameter == null) {
                return "";
            }else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())){
                return parameter.toString();
            }
            // POJO类型或者Map类型
            Object value = OgnlUtils.getValue(content, parameter);
            return value == null ? "":value.toString();
        }
    }
}
