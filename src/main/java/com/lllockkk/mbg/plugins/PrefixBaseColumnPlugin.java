package com.lllockkk.mbg.plugins;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.config.MergeConstants;

import java.util.List;

public class PrefixBaseColumnPlugin extends PluginAdapter {
    public static final String SQL_ID = "Prefix_Base_Column_List";
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement root = document.getRootElement();

        removeOrigin(root);

        XmlElement sqlEle = new XmlElement("sql");
        root.addElement(3, sqlEle);

        sqlEle.addAttribute(new Attribute("id", SQL_ID));

        // @mbggenerated 添加注释码，每次生成才会删除元素
        String comment = "<!--" + MergeConstants.NEW_ELEMENT_TAG + "-->";
        sqlEle.addElement(new TextElement(comment));

        List<IntrospectedColumn> baseColumns = introspectedTable.getNonBLOBColumns();
        String pre1 = "${pre}.";
        String pre2 = " ${pre}_";
        int maxSize = 100;
        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn columns : baseColumns) {
            String columnName = columns.getActualColumnName();
            sb.append(pre1).append(columnName).append(pre2).append(columnName).append(", ");
        }

        String text = sb.deleteCharAt(sb.length()-2).toString();

        while (true) {
            int index = text.lastIndexOf(",", maxSize - 1);
            if (text.length() < maxSize || index == -1) {
                sqlEle.addElement(new TextElement(text));
                break;
            } else {
                String startStr = text.substring(0, index + 1);
                sqlEle.addElement(new TextElement(startStr));
                text = text.substring(index + 1);
            }
        }

        return true;
    }

    // 删除原来的元素
    private void removeOrigin(XmlElement root) {
        for (Element element : root.getElements()) {
            if (element instanceof XmlElement) {
                XmlElement xmlElement = (XmlElement) element;
                if (!"sql".equals(xmlElement.getName()))
                    continue;

                List<Attribute> attributes = xmlElement.getAttributes();
                for (Attribute attribute : attributes) {
                    if (!"id".equals(attribute.getName()))
                        continue;

                    if (SQL_ID.equals(attribute.getValue())) {
                        root.getElements().remove(xmlElement);
                        break;
                    }
                }
            }
        }
    }
}
