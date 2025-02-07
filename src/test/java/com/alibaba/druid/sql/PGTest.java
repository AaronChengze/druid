/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.sql;

import java.util.List;

import com.alibaba.druid.sql.dialect.postgresql.parser.PGSQLStatementParser;
import junit.framework.TestCase;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGOutputVisitor;
import org.junit.Assert;

public class PGTest extends TestCase {
    protected String output(List<SQLStatement> stmtList) {
        StringBuilder out = new StringBuilder();
        PGOutputVisitor visitor = new PGOutputVisitor(out);

        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }

        return out.toString();
    }

    protected void print(List<SQLStatement> stmtList) {
        String text = output(stmtList);
        String outputProperty = System.getProperty("druid.output");
        if ("false".equals(outputProperty)) {
            return;
        }
        System.out.println(text);
    }

    protected void testParseSql(String sql, String expectedSql, String expectedPattern, Class<?> type) {
        PGSQLStatementParser parser = new PGSQLStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statement = statementList.get(0);

        Assert.assertEquals(1, statementList.size());
        Assert.assertTrue(type.isAssignableFrom(statement.getClass()));

        StringBuilder sb = new StringBuilder();
        PGOutputVisitor visitor = new PGOutputVisitor(sb);
        statement.accept(visitor);
        Assert.assertEquals(expectedSql, sb.toString());
    }
}
