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
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import org.junit.Assert;

import java.util.List;

public class OracleSelectTest24 extends OracleTest {
    public void test_0() throws Exception {
        String sql = //
                "select /*+ no_parallel_index(t, \"HT_TASK_TRADE_HIS_GOR_IND \") dbms_stats cursor_sharing_exact use_weak_name_resl dynamic_sampling(0) no_monitoring no_expand index_ffs(t, \"HT_TASK_TRADE_HIS_GOR_IND \") */ count(*) as nrw,count(distinct sys_op_lbid(196675,'L',t.rowid)) as nlb,count(distinct hextoraw(sys_op_descend( \"GMT_MODIFIED \")||sys_op_descend( \"OWNER \")||sys_op_descend( \"RECORD_TYPE \"))) as ndk,sys_op_countchg(substrb(t.rowid,1,15),1) as clf from  \"ESCROW\". \"HT_TASK_TRADE_HISTORY\" sample block ( 34.6426417370,1)  t where  \"GMT_MODIFIED \" is not null or  \"OWNER \" is not null or  \"RECORD_TYPE \" is not null"; //

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        String output = SQLUtils.toOracleString(stmt, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION);
        System.out.println(output);

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        stmt.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(1, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("ESCROW.HT_TASK_TRADE_HISTORY")));

        Assert.assertEquals(5, visitor.getColumns().size());

//         Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("ESCROW.HT_TASK_TRADE_HISTORY", "*")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "YEAR")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
