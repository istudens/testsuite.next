/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.testsuite.test.configuration.logging;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.LoggingConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CLASS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MODULE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.*;

public abstract class CustomFormatterAbstractTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations ops = new Operations(client);
    protected static final Administration adminOps = new Administration(client);

    @AfterClass
    public static void closeClient() throws IOException {
        client.close();
    }

    @Inject protected Console console;
    @Inject private CrudOperations crud;

    @Drone
    private WebDriver browser;

    private TableFragment table;
    private FormFragment form;
    protected abstract LoggingConfigurationPage getPage();
    protected abstract Address customFormatterAddress(String name);
    protected abstract void navigateToPage();

    @Before
    public void navigate() {
        navigateToPage();
        table = getPage().getCustomFormatterTable();
        form = getPage().getCustomFormatterForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(customFormatterAddress(CustomFormatter.CUSTOM_FORMATTER_CREATE), table, form -> {
            form.text(NAME, CustomFormatter.CUSTOM_FORMATTER_CREATE);
            form.text(CLASS, CUSTOM_FORMATTER_CLASS_1_VALUE);
            form.text(MODULE, CUSTOM_FORMATTER_MODULE_VALUE);
        });
    }

    @Test
    public void update() throws Exception {
        table.select(CustomFormatter.CUSTOM_FORMATTER_UPDATE);
        crud.update(customFormatterAddress(CustomFormatter.CUSTOM_FORMATTER_UPDATE), form, CLASS, CUSTOM_FORMATTER_CLASS_2_VALUE);
    }

    @Test
    public void reset() throws Exception {
        table.select(CustomFormatter.CUSTOM_FORMATTER_RESET);
        crud.reset(customFormatterAddress(CustomFormatter.CUSTOM_FORMATTER_RESET), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(customFormatterAddress(
            CustomFormatter.CUSTOM_FORMATTER_DELETE), table, CustomFormatter.CUSTOM_FORMATTER_DELETE);
    }
}
