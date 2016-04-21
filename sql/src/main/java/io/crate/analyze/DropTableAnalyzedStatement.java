/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */

package io.crate.analyze;

import io.crate.exceptions.ResourceUnknownException;
import io.crate.metadata.Schemas;
import io.crate.metadata.TableIdent;
import io.crate.metadata.doc.DocTableInfo;

public class DropTableAnalyzedStatement extends AbstractDropTableAnalyzedStatement<DocTableInfo> {

    public DropTableAnalyzedStatement(Schemas schemas, boolean ignoreNonExistentTable) {
        super(schemas, ignoreNonExistentTable);
    }

    @Override
    public <C, R> R accept(AnalyzedStatementVisitor<C, R> analyzedStatementVisitor, C context) {
        return analyzedStatementVisitor.visitDropTableStatement(this, context);
    }

    public void table(TableIdent tableIdent) {
        try {
            tableInfo = schemas.getDropableTable(tableIdent);
        } catch (ResourceUnknownException e) {
            if (dropIfExists) {
                noop = true;
            } else {
                throw e;
            }
        }
    }

}
