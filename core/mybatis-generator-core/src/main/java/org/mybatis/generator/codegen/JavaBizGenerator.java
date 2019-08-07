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
package org.mybatis.generator.codegen;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.*;
import java.util.ArrayList;
import java.util.List;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

public  class JavaBizGenerator extends AbstractJavaBizGenerator {

    private String project;

    public JavaBizGenerator(String project) {
        super(project);
        this.project = project;
    }
    
    public String getProject() {
        return project;
    }

    @Override
    public  List<CompilationUnit> getCompilationUnits(){
        if (introspectedTable.getMyBatis3JavaMapperType() == null) {
            return new ArrayList<>();
        }
        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getMyBatisBizType()+"<T,I>");
        TopLevelClass topLevelClass = new TopLevelClass(type);
        addImport(topLevelClass);
        addNimbleMethod(topLevelClass);
        addBatchNimbleSelect(topLevelClass);
        FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(
                introspectedTable.getMyBatis3JavaMapperType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        Field field = new Field(handle(mapperType.getShortName()),mapperType);

        field.addAnnotation("@Autowired");
        field.setVisibility(JavaVisibility.PRIVATE);
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        topLevelClass.addAnnotation("@Service");
        commentGenerator.addJavaFileComment(topLevelClass);

        List<CompilationUnit> answer = new ArrayList<>();
        answer.add(topLevelClass);
        return answer;
    }


    //添加批量灵活查询方法
    private void addBatchNimbleSelect (TopLevelClass topLevelClass) {
        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(
                introspectedTable.getExampleType());
        String exampleShortName= new FullyQualifiedJavaType(
                introspectedTable.getExampleType()).getShortName();
        String exampleFieldShortName= handle(exampleShortName);
        String mapperFieldShortName = handle(new FullyQualifiedJavaType(
                introspectedTable.getMyBatis3JavaMapperType()).getShortName());
        String biFunctionType = "BiFunction<" + exampleShortName + ".Criteria,List<I>," +
                exampleShortName + ".Criteria>";
        Method method = new Method("batchNimbleSelectByExample");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(exampleType, "example")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType
                ("Class<T>"), "clazz")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType
                (biFunctionType), "function")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType
                ("List<I>"), "list")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType
                ("int"), "batchSize")); //$NON-NLS-1$
        method.setReturnType(new FullyQualifiedJavaType("List<T>"));
        method.addBodyLine("List<I> queryList = new ArrayList<>();");
        method.addBodyLine("List<T> resList = new ArrayList<>();");
        method.addBodyLine("int flag = 0;");
        method.addBodyLine("for (I i : list) {");
        method.addBodyLine("queryList.add(i);");
        method.addBodyLine("flag++;");
        method.addBodyLine("if (flag == batchSize) {");
        method.addBodyLine("function.apply(example.getCriteria(),queryList);");
        method.addBodyLine("resList.addAll(nimbleSelectByExample(example,clazz));");
        method.addBodyLine("queryList.clear();");
        method.addBodyLine("flag = 0;");
        method.addBodyLine("}");
        method.addBodyLine("}");
        method.addBodyLine("if (!queryList.isEmpty()) {");
        method.addBodyLine("function.apply(example.getCriteria(),queryList);");
        method.addBodyLine("resList.addAll(nimbleSelectByExample(example,clazz));");
        method.addBodyLine("}");
        method.addBodyLine("return resList;");
        topLevelClass.addMethod(method);
    }

    //添加批量更新方法
    private void addBatchUpdate (TopLevelClass topLevelClass) {
        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(
                introspectedTable.getExampleType());
        String exampleShortName= new FullyQualifiedJavaType(
                introspectedTable.getExampleType()).getShortName();
        String exampleFieldShortName= handle(exampleShortName);
        String mapperFieldShortName = handle(new FullyQualifiedJavaType(
                introspectedTable.getMyBatis3JavaMapperType()).getShortName());
        Method method = new Method("batchUpdate");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(exampleType, "example")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType
                ("Class<T>"), "clazz")); //$NON-NLS-1$
        method.setReturnType(new FullyQualifiedJavaType("List<T>"));
        topLevelClass.addMethod(method);
    }

    //添加批量新增方法
    private void addBatchInsert (TopLevelClass topLevelClass) {
        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(
                introspectedTable.getExampleType());
        String exampleShortName= new FullyQualifiedJavaType(
                introspectedTable.getExampleType()).getShortName();
        String exampleFieldShortName= handle(exampleShortName);
        String mapperFieldShortName = handle(new FullyQualifiedJavaType(
                introspectedTable.getMyBatis3JavaMapperType()).getShortName());
        Method method = new Method("batchInsert");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List"), "list")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType
                ("Class<T>"), "clazz")); //$NON-NLS-1$
        method.setReturnType(new FullyQualifiedJavaType("List<T>"));
        topLevelClass.addMethod(method);
    }

    private void addNimbleMethod (TopLevelClass topLevelClass) {
        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(
                introspectedTable.getExampleType());
        String exampleShortName= new FullyQualifiedJavaType(
                introspectedTable.getExampleType()).getShortName();
        String exampleFieldShortName= handle(exampleShortName);
        String mapperFieldShortName = handle(new FullyQualifiedJavaType(
                introspectedTable.getMyBatis3JavaMapperType()).getShortName());
        Method method = new Method("nimbleSelectByExample");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(exampleType, "example")); //$NON-NLS-1$
        method.addParameter(new Parameter(new FullyQualifiedJavaType
                ("Class<T>"), "clazz")); //$NON-NLS-1$
        method.setReturnType(new FullyQualifiedJavaType("List<T>"));
        method.addBodyLine("List<T> objectList = new ArrayList<>();"); //$NON-NLS-1$
        method.addBodyLine("List<Map> mapList = "+ mapperFieldShortName +".nimbleSelectByExample(example);"); //$NON-NLS-1$

        method.addBodyLine("Field[] fields = clazz.getDeclaredFields();"); //$NON-NLS-1$
        method.addBodyLine("for (Map map : mapList) {"); //$NON-NLS-1$
        method.addBodyLine("try {"); //$NON-NLS-1$
        method.addBodyLine("Object obj = clazz.newInstance();"); //$NON-NLS-1$
        method.addBodyLine("for (Field field : fields) {"); //$NON-NLS-1$
        method.addBodyLine(exampleShortName + ".TableColumn tableColumn = field.getAnnotation(" + exampleShortName +".TableColumn.class);"); //$NON-NLS-1$
        method.addBodyLine("if (tableColumn != null) {"); //$NON-NLS-1$
        method.addBodyLine("String tableName = tableColumn.name();"); //$NON-NLS-1$
        method.addBodyLine("if (!tableColumn.resName().equals(\"\")) {"); //$NON-NLS-1$
        method.addBodyLine("tableName = tableColumn.resName();"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("field.setAccessible(true);"); //$NON-NLS-1$
        method.addBodyLine("field.set(obj, map.get(tableName));"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("objectList.add((T)obj);"); //$NON-NLS-1$
        method.addBodyLine("} catch (Exception e) {"); //$NON-NLS-1$
        method.addBodyLine("continue;"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("return objectList;"); //$NON-NLS-1$
        topLevelClass.addMethod(method);
    }

    private void addImport(TopLevelClass topLevelClass) {
        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(
                introspectedTable.getExampleType());
        FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(
                introspectedTable.getMyBatis3JavaMapperType());
        topLevelClass.addImportedType(exampleType);
        topLevelClass.addImportedType(mapperType);
        topLevelClass.addImportedType("java.util.Map");
        topLevelClass.addImportedType("java.util.List");
        topLevelClass.addImportedType("java.util.ArrayList");
        topLevelClass.addImportedType("java.lang.reflect.Field");
        topLevelClass.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        topLevelClass.addImportedType("org.springframework.stereotype.Service");
        topLevelClass.addImportedType("java.util.function.BiFunction");
    }

    public static String handle(String s) {
        char[] chars = s.toCharArray();
        chars[0] = chars[0] > 90 ? chars[0] : (char)(chars[0] + 32);
        return String.valueOf(chars);
    }

}
