/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
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
package com.alibaba.druid.bvt.sql.oracle.select;

import com.alibaba.druid.sql.OracleTest;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;

public class OracleSelectTest_hierarchical_CONNECT_BY_ISCYCLE extends OracleTest {
    public void test_0() throws Exception {
        String sql = "SELECT last_name \"Employee\", CONNECT_BY_ISCYCLE \"Cycle\",\n" +
                "   LEVEL, SYS_CONNECT_BY_PATH(last_name, '/') \"Path\"\n" +
                "   FROM employees\n" +
                "   WHERE level <= 3 AND department_id = 80\n" +
                "   START WITH last_name = 'King'\n" +
                "   CONNECT BY NOCYCLE PRIOR employee_id = manager_id AND LEVEL <= 4;"; //

        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.ORACLE);
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        assertEquals(1, statementList.size());

        SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(JdbcConstants.ORACLE);
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        assertEquals(1, visitor.getTables().size());

        assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));

        assertEquals(4, visitor.getColumns().size());

//        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "*")));
//        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "YEAR")));
//        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
