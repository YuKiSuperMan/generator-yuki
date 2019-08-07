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
public class MapperValueGenerator extends AbstractJavaGenerator {

    public MapperValueGenerator(String project) {
        super(project);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        String annotateName = IntrospectedTable.getAloneName(context);
        String targetPackage = IntrospectedTable.getAlonePackage(context);
        TopLevelClass topLevelClass = new TopLevelClass(new FullyQualifiedJavaType(targetPackage +"."+annotateName + "Value"));
        topLevelClass.addImportedType("lombok.Getter");
        topLevelClass.addImportedType("lombok.Setter");
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        List<CompilationUnit> answer = new ArrayList<>();
        topLevelClass.addAnnotation("@Setter");
        topLevelClass.addAnnotation("@Getter");
        Field field = new Field("clazz",new FullyQualifiedJavaType("Class"));
        field.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(field);
        answer.add(topLevelClass);
        return answer;
    }


}
