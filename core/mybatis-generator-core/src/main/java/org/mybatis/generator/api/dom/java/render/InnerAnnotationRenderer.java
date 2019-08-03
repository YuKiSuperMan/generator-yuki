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
package org.mybatis.generator.api.dom.java.render;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.InnerAnnotation;
import org.mybatis.generator.api.dom.java.JavaDomUtils;
import org.mybatis.generator.internal.util.CustomCollectors;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.*;

/**
 * Created by longxiangyu on 2019/7/14.
 */
public class InnerAnnotationRenderer {


    public List<String> render(InnerAnnotation innerAnnotation, CompilationUnit compilationUnit) {
        List<String> lines = new ArrayList<>();

        lines.addAll(innerAnnotation.getJavaDocLines());
        lines.addAll(innerAnnotation.getAnnotations());
        lines.add(renderFirstLine(innerAnnotation, compilationUnit));
        lines.addAll(renderFields(innerAnnotation.getFields(), compilationUnit));
        lines.addAll(renderInterfaceMethods(innerAnnotation.getMethods(), compilationUnit));
        // last line might be blank, remove it if so
        if (lines.get(lines.size() - 1).isEmpty()) {
            lines = lines.subList(0, lines.size() - 1);
        }

        lines.add("}"); //$NON-NLS-1$

        return lines;
    }

    private String renderFirstLine(InnerAnnotation innerAnnotation, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        sb.append(innerAnnotation.getVisibility().getValue());

        if (innerAnnotation.isAbstract()) {
            sb.append("abstract "); //$NON-NLS-1$
        }

        if (innerAnnotation.isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }

        if (innerAnnotation.isFinal()) {
            sb.append("final "); //$NON-NLS-1$
        }

        sb.append("@interface "); //$NON-NLS-1$
        sb.append(innerAnnotation.getType().getShortName());
        sb.append(renderTypeParameters(innerAnnotation.getTypeParameters(), compilationUnit));
        sb.append(renderSuperClass(innerAnnotation, compilationUnit));
        sb.append(renderSuperInterfaces(innerAnnotation, compilationUnit));
        sb.append(" {"); //$NON-NLS-1$

        return sb.toString();
    }

    private String renderSuperClass(InnerAnnotation innerAnnotation, CompilationUnit compilationUnit) {
        return innerAnnotation.getSuperClass()
                .map(sc -> " extends " + JavaDomUtils.calculateTypeName(compilationUnit, sc)) //$NON-NLS-1$
                .orElse(""); //$NON-NLS-1$
    }

    // should return an empty string if no super interfaces
    private String renderSuperInterfaces(InnerAnnotation innerAnnotation, CompilationUnit compilationUnit) {
        return innerAnnotation.getSuperInterfaceTypes().stream()
                .map(tp -> JavaDomUtils.calculateTypeName(compilationUnit, tp))
                .collect(CustomCollectors.joining(", ", " implements ", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}
