/**
 *    Copyright 2006-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseBatchInsertElementGenerator  extends AbstractXmlElementGenerator {

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    public BaseBatchInsertElementGenerator () {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                introspectedTable.getBaseBatchInsertListId()));
        boolean and = false;
        StringBuilder sb = new StringBuilder();
        List<IntrospectedColumn> introspectedColumnList = introspectedTable.getPrimaryKeyColumns();
        for (IntrospectedColumn introspectedColumn : introspectedColumnList) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getSelectListPhrase(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append("#{item." + lineToHump(MyBatis3FormattingUtilities.getSelectListPhrase(introspectedColumn)));
        }
        context.getCommentGenerator().addComment(answer);
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", ","));
        String text = "(";
        Iterator<IntrospectedColumn> iter = introspectedTable
                .getNonBLOBColumns().iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (isExsit(introspectedColumnList,introspectedColumn)) {
//                continue;
            }
            String name = MyBatis3FormattingUtilities.getSelectListPhrase(introspectedColumn);
            text += "#{";
            text += "item." + lineToHump(name);
            text += "}";
            if (iter.hasNext()) {
                text += ",";
            }
        }
        text += ")";
        foreachElement.addElement(new TextElement(text));
        answer.addElement(foreachElement);
        if (context.getPlugins().sqlMapBaseColumnListElementGenerated(
                answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

    /** 下划线转驼峰 */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private boolean isExsit (List<IntrospectedColumn> introspectedColumnList,IntrospectedColumn introspectedColumn) {
        for (IntrospectedColumn introspectedColumn1 : introspectedColumnList) {
            if (introspectedColumn.equals(introspectedColumn1)) {
                return true;
            }
        }
        return false;
    }

}
