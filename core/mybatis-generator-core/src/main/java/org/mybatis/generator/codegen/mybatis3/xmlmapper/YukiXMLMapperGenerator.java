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
package org.mybatis.generator.codegen.mybatis3.xmlmapper;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.*;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * 
 * @author longxiangyu
 */
public class YukiXMLMapperGenerator extends AbstractXmlGenerator {

    public YukiXMLMapperGenerator() {
        super();
    }


    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString(
                "Progress.12", table.toString())); //$NON-NLS-1$
        XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = introspectedTable.getMyBatis3SqlNimbleMapNamespace();
        answer.addAttribute(new Attribute("namespace", //$NON-NLS-1$
                namespace));

        context.getCommentGenerator().addRootComment(answer);

        addResultMapWithoutBLOBsElement(answer);
        addResultMapWithBLOBsElement(answer);
        addExampleWhereClauseElement(answer);
        addBaseNimberColumListElement(answer);
        addBaseBatchUpdateListElement(answer);
        addBaseBatchInsertListElement(answer);
        addMyBatis3UpdateByExampleWhereClauseElement(answer);
        addBaseColumnListElement(answer);
        addBlobColumnListElement(answer);
        addNimbleSelectByExampleElement(answer);
        addBatchInsertElement(answer);
        addBatchUpdateElement(answer);
        return answer;
    }

    protected void addBatchInsertElement(XmlElement parentElement) {
            AbstractXmlElementGenerator elementGenerator = new BatchInsertByExampleGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addBatchUpdateElement(XmlElement parentElement) {
            AbstractXmlElementGenerator elementGenerator = new BatchUpdateByExampleGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addNimbleSelectByExampleElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateNimbleSelectByExample()) {
            AbstractXmlElementGenerator elementGenerator = new NimbleSelectByExampleGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addBaseBatchUpdateListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseBatchUpdateList()) {
            AbstractXmlElementGenerator elementGenerator = new BaseBatchUpdateElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addBaseBatchInsertListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseBatchInsertList()) {
            AbstractXmlElementGenerator elementGenerator = new BaseBatchInsertElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addBaseNimberColumListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateNimbleSelectByExample()) {
            AbstractXmlElementGenerator elementGenerator = new BaseNimberColumListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addResultMapWithoutBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseResultMap()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapWithoutBLOBsElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addResultMapWithBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapWithBLOBsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addExampleWhereClauseElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSQLExampleWhereClause()) {
            AbstractXmlElementGenerator elementGenerator = new ExampleWhereClauseElementGenerator(
                    false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addMyBatis3UpdateByExampleWhereClauseElement(
            XmlElement parentElement) {
        if (introspectedTable.getRules()
                .generateMyBatis3UpdateByExampleWhereClause()) {
            AbstractXmlElementGenerator elementGenerator = new ExampleWhereClauseElementGenerator(
                    true);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addBaseColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseColumnList()) {
            AbstractXmlElementGenerator elementGenerator = new BaseColumnListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addBlobColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBlobColumnList()) {
            AbstractXmlElementGenerator elementGenerator = new BlobColumnListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void initializeAndExecuteGenerator(
            AbstractXmlElementGenerator elementGenerator,
            XmlElement parentElement) {
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.setProgressCallback(progressCallback);
        elementGenerator.setWarnings(warnings);
        elementGenerator.addElements(parentElement);
    }

    @Override
    public Document getDocument() {
        Document document = new Document(
                XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        document.setRootElement(getSqlMapElement());

        if (!context.getPlugins().sqlMapDocumentGenerated(document,
                introspectedTable)) {
            document = null;
        }

        return document;
    }
}
