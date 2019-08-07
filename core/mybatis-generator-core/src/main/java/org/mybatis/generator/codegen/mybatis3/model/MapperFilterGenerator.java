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
public class MapperFilterGenerator extends AbstractJavaGenerator {

    public MapperFilterGenerator(String project) {
        super(project);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        String annotateName = IntrospectedTable.getAloneName(context);
        String targetPackage = IntrospectedTable.getAlonePackage(context);
        String pointPackage = IntrospectedTable.getAlonePointPackage(context);
        String tableColumnName = annotateName + "TableColumn";
        String threadMapperHolderName = annotateName + "ThreadMapperHolder";
        String valueName = annotateName + "Value";
        TopLevelClass topLevelClass = new TopLevelClass(new FullyQualifiedJavaType(targetPackage + "." + annotateName + "MapperFilter"));
        topLevelClass.addImportedType("org.aspectj.lang.ProceedingJoinPoint");
        topLevelClass.addImportedType("org.aspectj.lang.annotation.Around");
        topLevelClass.addImportedType("org.aspectj.lang.annotation.Aspect");
        topLevelClass.addImportedType("org.aspectj.lang.annotation.Pointcut");
        topLevelClass.addImportedType("org.slf4j.Logger");
        topLevelClass.addImportedType("org.slf4j.LoggerFactory");
        topLevelClass.addImportedType("org.springframework.stereotype.Component");
        topLevelClass.addImportedType("java.lang.reflect.Field");
        topLevelClass.addImportedType("java.util.ArrayList");
        topLevelClass.addImportedType("java.util.List");
        topLevelClass.addImportedType("java.util.Map");
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addAnnotation("@Aspect");
        topLevelClass.addAnnotation("@Component");
        Method method = new Method("nimbleSelectByExample");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Pointcut(\"execution(public * "+pointPackage+".*.nimbleSelectByExample(..)) \")");
        topLevelClass.addMethod(method);
        method = new Method("handle");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("Object"));
        method.addException(new FullyQualifiedJavaType("Throwable"));
        method.addAnnotation("@Around(\"nimbleSelectByExample()\")");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("ProceedingJoinPoint"),"point"));
        method.addBodyLine("try {");
        method.addBodyLine(valueName + " value = "+threadMapperHolderName+".getValue();");
        method.addBodyLine("if (value != null) {");
        method.addBodyLine("List objectList = new ArrayList<>();");
        method.addBodyLine("Class clazz = value.getClazz();");
        method.addBodyLine("List<Map> mapList = (List<Map>) point.proceed(point.getArgs());");
        method.addBodyLine("Field[] fields = clazz.getDeclaredFields();");
        method.addBodyLine("for (Map map : mapList) {");
        method.addBodyLine("Object obj = clazz.newInstance();");
        method.addBodyLine("for (Field field : fields) {");
        method.addBodyLine(tableColumnName + " tableColumn = field.getAnnotation("+tableColumnName+".class);");
        method.addBodyLine("if (tableColumn != null) {");
        method.addBodyLine("String tableName = tableColumn.name();");
        method.addBodyLine("if (!tableColumn.resName().equals(\"\")) {");
        method.addBodyLine("tableName = tableColumn.resName();");
        method.addBodyLine("}");
        method.addBodyLine("field.setAccessible(true);");
        method.addBodyLine("field.set(obj, map.get(tableName));");
        method.addBodyLine("}");
        method.addBodyLine("}");
        method.addBodyLine("objectList.add(obj);");
        method.addBodyLine("}");
        method.addBodyLine("return objectList;");
        method.addBodyLine("} else {");
        method.addBodyLine("return point.proceed(point.getArgs());");
        method.addBodyLine("}");
        method.addBodyLine("} finally {");
        method.addBodyLine(threadMapperHolderName + ".removeValue();");
        method.addBodyLine("}");
        topLevelClass.addMethod(method);
        List<CompilationUnit> answer = new ArrayList<>();
        answer.add(topLevelClass);
        return answer;
    }


}
