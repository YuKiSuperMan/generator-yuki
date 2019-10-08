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
package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by longxiangyu on 2019/7/13.
 */
public class JoinNimbleSelectByExampleMethodGenerator extends AbstractJavaMapperMethodGenerator{

    private boolean isGeneric;

    public JoinNimbleSelectByExampleMethodGenerator() {
        super();
    }

    public JoinNimbleSelectByExampleMethodGenerator(boolean isGeneric) {
        super();
        this.isGeneric = isGeneric;
    }


    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getExampleType());
        importedTypes.add(type);
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method(introspectedTable
                .getNimbleSelectByExampleId()+ "ByJoin");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType
                .getNewListInstance();
        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewMapInstance();
        if (!isGeneric) {
            returnType.addTypeArgument(listType);
        } else {
            returnType.addTypeArgument(new FullyQualifiedJavaType("T"));
        }
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.String"), "columnList","@Param(\"columnList\")"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.String"), "whereConditional","@Param(\"where\")"));
        method.setReturnType(returnType);
        //添加自动生成注解
        addDoc(method);


        addMapperAnnotations(interfaze, method);

        if (context.getPlugins().clientInsertSelectiveMethodGenerated(method,
                interfaze, introspectedTable)) {
            addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }

    }

    public void addDoc(Method method) {
        StringBuilder sb = new StringBuilder();
        method.addJavaDocLine("/**"); //$NON-NLS-1$
        method.addJavaDocLine(" * This method is create by YuKi "); //$NON-NLS-1$
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }

    public void addExtraImports(Interface interfaze) {
    }

}
