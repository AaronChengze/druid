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
package com.alibaba.druid.bvt.filter.wall.oracle;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.druid.wall.WallUtils;

public class OracleWallPermitSchemaTest extends TestCase {
    public void test_true() throws Exception {
        Assert.assertTrue(WallUtils.isValidateOracle("select banner from sys.v_$version where rownum=1"));
        Assert.assertTrue(WallUtils.isValidateOracle("select banner from sys.v where rownum=1"));
    }

    public void test_false() throws Exception {
        Assert.assertFalse(WallUtils.isValidateOracle("SELECT * FROM T UNION select banner from sys.v_$version where rownum=1"));
        Assert.assertFalse(WallUtils.isValidateOracle("SELECT * FROM T UNION select banner from sys.v where rownum=1"));
    }

}
