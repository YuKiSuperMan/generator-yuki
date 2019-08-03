/**
 *    Copyright 2006-2016 the original author or authors.
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
package org.mybatis.generator.eclipse.ui.launcher.tabs;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.mybatis.generator.eclipse.ui.Messages;

public class SqlScriptTab extends AbstractLaunchConfigurationTab {

    private SqlScriptComposite sqlScriptComposite;

    @Override
    public void createControl(Composite parent) {
        sqlScriptComposite = new SqlScriptComposite(parent, SWT.NONE, this);
        setControl(sqlScriptComposite);
    }

    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        // no good defaults available
    }

    @Override
    public void initializeFrom(ILaunchConfiguration configuration) {
        sqlScriptComposite.initializeFrom(configuration);
    }

    @Override
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        sqlScriptComposite.performApply(configuration);
    }

    @Override
    public String getName() {
        return Messages.SQL_SCRIPT_TAB_NAME;
    }

    @Override
    public void updateLaunchConfigurationDialog() {
        super.updateLaunchConfigurationDialog();
    }

    @Override
    public boolean isValid(ILaunchConfiguration launchConfig) {
        return sqlScriptComposite.isValid();
    }

    @Override
    public void setErrorMessage(String message) {
        super.setErrorMessage(message);
    }

    @Override
    public Shell getShell() {
        return super.getShell();
    }
}
