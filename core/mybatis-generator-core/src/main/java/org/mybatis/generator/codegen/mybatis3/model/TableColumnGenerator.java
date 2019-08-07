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
package org.mybatis.generator.codegen.mybatis3.model;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jeff Butler
 * change author longxiangyu
 */
public class TableColumnGenerator extends AbstractJavaGenerator {

    public TableColumnGenerator(String project) {
        super(project);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        String annotateName = IntrospectedTable.getAloneName(context);
        String targetPackage = IntrospectedTable.getAlonePackage(context);
        TopLevelAnnotation topLevelAnnotation = new TopLevelAnnotation(new FullyQualifiedJavaType(targetPackage+"."+annotateName + "TableColumn"));
        topLevelAnnotation.setVisibility(JavaVisibility.PUBLIC);
        topLevelAnnotation.addAnnotation("@Target({ElementType.FIELD})");
        topLevelAnnotation.addAnnotation("@Retention(RetentionPolicy.RUNTIME)");
        topLevelAnnotation.addAnnotation("@Inherited");
        topLevelAnnotation.addAnnotation("@Documented");
        topLevelAnnotation.addImportedType(new FullyQualifiedJavaType("java.lang.annotation.Inherited"));
        topLevelAnnotation.addImportedType(new FullyQualifiedJavaType("java.lang.annotation.Documented"));
        topLevelAnnotation.addImportedType(new FullyQualifiedJavaType("java.lang.annotation.ElementType"));
        topLevelAnnotation.addImportedType(new FullyQualifiedJavaType("java.lang.annotation.Retention"));
        topLevelAnnotation.addImportedType(new FullyQualifiedJavaType("java.lang.annotation.RetentionPolicy"));
        topLevelAnnotation.addImportedType(new FullyQualifiedJavaType("java.lang.annotation.Target"));
        Method method = new Method("name");
        method.setAnnatation(true);
        method.setReturnType(new FullyQualifiedJavaType("String"));
        topLevelAnnotation.addMethod(method);
        method = new Method("resName");
        method.setAnnatation(true);
        method.setDefaultValue("\"\"");
        method.setReturnType(new FullyQualifiedJavaType("String"));
        topLevelAnnotation.addMethod(method);
        List<CompilationUnit> answer = new ArrayList<>();
        answer.add(topLevelAnnotation);
        return answer;
    }


}
