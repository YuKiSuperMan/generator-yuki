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
public class ThreadMapperHolderGenerator extends AbstractJavaGenerator {

    public ThreadMapperHolderGenerator(String project) {
        super(project);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        String annotateName = IntrospectedTable.getAloneName(context);
        String targetPackage = IntrospectedTable.getAlonePackage(context);
        String valueName = annotateName + "Value";
        TopLevelClass topLevelClass = new TopLevelClass(new FullyQualifiedJavaType(targetPackage+"."+annotateName + "ThreadMapperHolder"));
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        List<CompilationUnit> answer = new ArrayList<>();
        Field field = new Field("threadLocal",new FullyQualifiedJavaType("ThreadLocal<"+valueName+">"));
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setFinal(true);
        field.setStatic(true);
        field.setInitializationString("new ThreadLocal<"+valueName+">()");
        topLevelClass.addField(field);

        Method method = new Method("getValue");
        method.setReturnType(new FullyQualifiedJavaType(valueName));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setStatic(true);
        method.addBodyLine("return threadLocal.get();");
        topLevelClass.addMethod(method);
        method = new Method("setValue");
        method.addParameter(new Parameter(new FullyQualifiedJavaType(valueName),"value"));
        method.setReturnType(new FullyQualifiedJavaType("void"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setStatic(true);
        method.addBodyLine("threadLocal.set(value);");
        topLevelClass.addMethod(method);
        method = new Method("removeValue");
        method.setReturnType(new FullyQualifiedJavaType("void"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setStatic(true);
        method.addBodyLine("if (threadLocal.get() != null) {");
        method.addBodyLine("threadLocal.remove();");
        method.addBodyLine("}");
        topLevelClass.addMethod(method);

        answer.add(topLevelClass);
        return answer;
    }


}
