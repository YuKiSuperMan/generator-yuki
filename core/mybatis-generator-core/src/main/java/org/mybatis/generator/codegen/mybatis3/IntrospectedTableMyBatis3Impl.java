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
package org.mybatis.generator.codegen.mybatis3;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.AnnotatedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.LxyJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.MixedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.YukiJavaNimbleClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.YukiJavaSimpleClientGenerator;
import org.mybatis.generator.codegen.mybatis3.model.*;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * Introspected table implementation for generating MyBatis3 artifacts.
 * 
 * @author Jeff Butler
 * change author longxiangyu
 */
public class IntrospectedTableMyBatis3Impl extends IntrospectedTable {
    
    protected List<AbstractJavaGenerator> javaGenerators = new ArrayList<>();

    protected AbstractXmlGenerator xmlMapperGenerator;

    public IntrospectedTableMyBatis3Impl() {
        super(TargetRuntime.MYBATIS3);
    }

    @Override
    public void calculateGenerators(List<String> warnings,
            ProgressCallback progressCallback) {
        calculateJavaModelGenerators(warnings, progressCallback);

        if (context.getJavaBizGeneratorConfiguration() != null) {
            calculateBizGenerator(warnings, progressCallback);
        }
        AbstractJavaClientGenerator javaClientGenerator =
                calculateClientGenerators(warnings, progressCallback);

        calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
    }



    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, 
            List<String> warnings,
            ProgressCallback progressCallback) {
        if (javaClientGenerator == null) {
            if (context.getSqlMapGeneratorConfiguration() != null) {
                xmlMapperGenerator = new XMLMapperGenerator();
            }
        } else {
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
        }
        
        initializeAbstractGenerator(xmlMapperGenerator, warnings,
                progressCallback);
    }

    protected AbstractJavaClientGenerator calculateClientGenerators(List<String> warnings,
            ProgressCallback progressCallback) {
        if (!rules.generateJavaClient()) {
            return null;
        }
        
        AbstractJavaClientGenerator javaGenerator = createJavaClientGenerator();
        if (javaGenerator == null) {
            return null;
        }

        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        javaGenerators.add(javaGenerator);

        return javaGenerator;
    }


    protected AbstractJavaBizGenerator calculateBizGenerator(List<String> warnings,
                                                             ProgressCallback progressCallback) {
        AbstractJavaBizGenerator javaGenerator = new JavaBizGenerator(getBizProject());
        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        javaGenerators.add(javaGenerator);
        return javaGenerator;
    }

    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        }
        
        String type = context.getJavaClientGeneratorConfiguration()
                .getConfigurationType();

        AbstractJavaClientGenerator javaGenerator;
        if ("XMLMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new JavaMapperGenerator(getClientProject());
        } else if ("MIXEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new MixedClientGenerator(getClientProject());
        } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new AnnotatedClientGenerator(getClientProject());
        } else if ("MAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new JavaMapperGenerator(getClientProject());
        } else if ("LXYMAPPER".equalsIgnoreCase(type)) {
            //此处加入LxyJavaClientGenerator
            javaGenerator = new LxyJavaClientGenerator(getClientProject());
        } else if ("YUKIMAPPER".equalsIgnoreCase(type)) {
            javaGenerator = new YukiJavaNimbleClientGenerator(getClientProject());
        } else if ("YUKISIMPLEMAPPER".equalsIgnoreCase(type)){
            javaGenerator = new YukiJavaSimpleClientGenerator(getClientProject());
        } else {
            javaGenerator = (AbstractJavaClientGenerator) ObjectFactory
                    .createInternalObject(type);
        }

        return javaGenerator;
    }

    protected List<String> warnings = null;
    protected ProgressCallback progressCallback = null;


    protected void calculateJavaModelGenerators(List<String> warnings,
            ProgressCallback progressCallback) {
        this.warnings = warnings;
        this.progressCallback = progressCallback;
        String type = "";
        if (getRules().generateExampleClass()) {
            if (context.getJavaClientGeneratorConfiguration() != null) {
                type = context.getJavaClientGeneratorConfiguration()
                        .getConfigurationType();
            }
            if ("YUKIMAPPER".equalsIgnoreCase(type)) {
                AbstractJavaGenerator javaGenerator = new YuKiExampleGenerator(getExampleProject());
                initializeAbstractGenerator(javaGenerator, warnings,
                        progressCallback);
                javaGenerators.add(javaGenerator);
            } else {
                AbstractJavaGenerator javaGenerator = new ExampleGenerator(getExampleProject());
                initializeAbstractGenerator(javaGenerator, warnings,
                        progressCallback);
                javaGenerators.add(javaGenerator);
            }
        }

        if (getRules().generatePrimaryKeyClass()) {
            AbstractJavaGenerator javaGenerator = new PrimaryKeyGenerator(getModelProject());
            initializeAbstractGenerator(javaGenerator, warnings,
                    progressCallback);
            javaGenerators.add(javaGenerator);
        }

        if (getRules().generateBaseRecordClass()) {
            AbstractJavaGenerator javaGenerator = new BaseRecordGenerator(getModelProject());
            initializeAbstractGenerator(javaGenerator, warnings,
                    progressCallback);
            javaGenerators.add(javaGenerator);
        }

        if (getRules().generateRecordWithBLOBsClass()) {
            AbstractJavaGenerator javaGenerator = new RecordWithBLOBsGenerator(getModelProject());
            initializeAbstractGenerator(javaGenerator, warnings,
                    progressCallback);
            javaGenerators.add(javaGenerator);
        }
    }

    /**
     *
     * @param warnings
     * @param progressCallback
     */
    public static List<GeneratedJavaFile> calculateAloneGenerators(List<String> warnings,
                                                                   ProgressCallback progressCallback, Context context) {

        List<GeneratedJavaFile> answer = new ArrayList<>();

        List<AbstractJavaGenerator> javaGenerators = new ArrayList<>();
        String type = "";
        if (context.getJavaClientGeneratorConfiguration() != null) {
            type = context.getJavaClientGeneratorConfiguration()
                    .getConfigurationType();
        }
        if ("YUKIMAPPER".equalsIgnoreCase(type)) {
            AbstractJavaGenerator threadMapperHolderGenerator = new ThreadMapperHolderGenerator(getAloneClassProject(context));
            initializeAbstractGenerator(threadMapperHolderGenerator, warnings,
                    progressCallback,context);
            javaGenerators.add(threadMapperHolderGenerator);

            AbstractJavaGenerator tableColumnGenerator = new TableColumnGenerator(getAloneClassProject(context));
            initializeAbstractGenerator(tableColumnGenerator, warnings,
                    progressCallback,context);
            javaGenerators.add(tableColumnGenerator);

            AbstractJavaGenerator mapperValueGenerator = new MapperValueGenerator(getAloneClassProject(context));
            initializeAbstractGenerator(mapperValueGenerator, warnings,
                    progressCallback,context);
            javaGenerators.add(mapperValueGenerator);

            AbstractJavaGenerator mapperHandleGenerator = new MapperHandleGenerator(getAloneClassProject(context));
            initializeAbstractGenerator(mapperHandleGenerator, warnings,
                    progressCallback,context);
            javaGenerators.add(mapperHandleGenerator);

            AbstractJavaGenerator mapperFilterGenerator = new MapperFilterGenerator(getAloneClassProject(context));
            initializeAbstractGenerator(mapperFilterGenerator, warnings,
                    progressCallback,context);
            javaGenerators.add(mapperFilterGenerator);
        }
        for (AbstractJavaGenerator javaGenerator : javaGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator
                    .getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                        javaGenerator.getProject(),
                        context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                        context.getJavaFormatter());
                answer.add(gjf);
            }
        }
        return answer;
    }

    protected static void initializeAbstractGenerator(
            AbstractGenerator abstractGenerator, List<String> warnings,
            ProgressCallback progressCallback,Context context) {
        if (abstractGenerator == null) {
            return;
        }

        abstractGenerator.setContext(context);
        abstractGenerator.setProgressCallback(progressCallback);
        abstractGenerator.setWarnings(warnings);
    }

    protected void initializeAbstractGenerator(
            AbstractGenerator abstractGenerator, List<String> warnings,
            ProgressCallback progressCallback) {
        if (abstractGenerator == null) {
            return;
        }

        abstractGenerator.setContext(context);
        abstractGenerator.setIntrospectedTable(this);
        abstractGenerator.setProgressCallback(progressCallback);
        abstractGenerator.setWarnings(warnings);
    }

    @Override
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        List<GeneratedJavaFile> answer = new ArrayList<>();

        for (AbstractJavaGenerator javaGenerator : javaGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator
                    .getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                                javaGenerator.getProject(),
                                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                                context.getJavaFormatter());
                answer.add(gjf);
            }
        }

        return answer;
    }

    protected String getClientProject() {
        return context.getJavaClientGeneratorConfiguration().getTargetProject();        
    }

    protected String getBizProject() {
        return context.getJavaBizGeneratorConfiguration() != null ? context.getJavaBizGeneratorConfiguration().getTargetProject() : null;
    }

    protected String getModelProject() {
        return context.getJavaModelGeneratorConfiguration().getTargetProject();        
    }
    
    protected String getExampleProject() {
        String project = context.getJavaModelGeneratorConfiguration().getProperty(PropertyRegistry.MODEL_GENERATOR_EXAMPLE_PROJECT);
        
        if (StringUtility.stringHasValue(project)) {
            return project;
        } else {
            return getModelProject();
        }
    }

    protected static String getAloneClassProject (Context context) {
        String project = context.getJavaAnnotateTableColumnConfiguration().getTargetProject();
        return project;
    }
    
    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<>();

        if (xmlMapperGenerator != null) {
            Document document = xmlMapperGenerator.getDocument();
            GeneratedXmlFile gxf = new GeneratedXmlFile(document,
                    getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(),
                    context.getSqlMapGeneratorConfiguration().getTargetProject(),
                    true, context.getXmlFormatter());
            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }

        return answer;
    }

    @Override
    public int getGenerationSteps() {
        return javaGenerators.size()
                + (xmlMapperGenerator == null ? 0 : 1);
    }

    @Override
    public boolean requiresXMLGenerator() {
        AbstractJavaClientGenerator javaClientGenerator =
                createJavaClientGenerator();
        
        if (javaClientGenerator == null) {
            return false;
        } else {
            return javaClientGenerator.requiresXMLGenerator();
        }
    }
}
