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
package org.mybatis.generator.eclipse.ui.ant;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildLogger;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;

public class GeneratorBuildLogger implements BuildLogger {
    private IOConsoleOutputStream outputStream;
    private int messageOutputLevel;
    
    public GeneratorBuildLogger() {
        IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
        IConsole[] consoles = consoleManager.getConsoles();
        for (IConsole console : consoles) {
            if ("MyBatis Generator".equals(console.getName())) {
                IOConsole ioConsole = (IOConsole) console;
                ioConsole.clearConsole();
                outputStream = ioConsole.newOutputStream();
                return;
            }
        }
        
        if (outputStream == null) {
            IOConsole console = new IOConsole("MyBatis Generator", null); //$NON-NLS-1$
            outputStream = console.newOutputStream();
            consoleManager.addConsoles(new IConsole[] {console});
        }
    }

    @Override
    public void buildStarted(BuildEvent event) {
        writeMessage("MyBatis Generator Started..."); //$NON-NLS-1$
    }

    @Override
    public void buildFinished(BuildEvent event) {
        writeMessage("MyBatis Generator Finished"); //$NON-NLS-1$
    }

    @Override
    public void targetStarted(BuildEvent event) {
        writeMessage(event.getPriority(), "Target " + event.getTarget().getName() + " - Started"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void targetFinished(BuildEvent event) {
        writeMessage(event.getPriority(), "Target " + event.getTarget().getName() + " - Finished"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void taskStarted(BuildEvent event) {
        writeMessage(event.getPriority(), "Task " + event.getTask().getTaskName() + " - Started"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void taskFinished(BuildEvent event) {
        writeMessage(event.getPriority(), "Task " + event.getTask().getTaskName() + " - Finished"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public void messageLogged(BuildEvent event) {
        writeMessage(event.getPriority(), "  " + event.getMessage()); //$NON-NLS-1$
    }

    private void writeMessage(int level, String message) {
        if (level > messageOutputLevel) {
            return;
        }

        writeMessage(message);
    }

    private void writeMessage(String message) {
        try {
            outputStream.write(message);
            outputStream.write('\n');
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void setMessageOutputLevel(int level) {
        this.messageOutputLevel = level;
    }

    @Override
    public void setOutputPrintStream(PrintStream output) {
    }

    @Override
    public void setEmacsMode(boolean emacsMode) {
    }

    @Override
    public void setErrorPrintStream(PrintStream err) {
    }
}
