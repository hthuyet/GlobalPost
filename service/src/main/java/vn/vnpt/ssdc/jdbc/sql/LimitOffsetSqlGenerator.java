/*
 * Copyright 2016 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.vnpt.ssdc.jdbc.sql;

import org.springframework.data.domain.Pageable;
import vn.vnpt.ssdc.jdbc.TableInfo;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;

/**
 * Created by vietnq on 10/25/16.
 */
public class LimitOffsetSqlGenerator extends DefaultSqlGenerator {

    private static final List<String> SUPPORTED_PRODUCTS =
        asList("PostgreSQL", "H2", "HSQL Database Engine", "MySQL");


    @Override
    public boolean isCompatible(DatabaseMetaData metadata) throws SQLException {
        return SUPPORTED_PRODUCTS.contains(metadata.getDatabaseProductName());
    }

    @Override
    public String selectAll(TableInfo table, Pageable page) {
        return format("%s LIMIT %d OFFSET %d",
            selectAll(table, page.getSort()), page.getPageSize(), page.getOffset());
    }

    @Override
    public String search(TableInfo table, String whereExp, Pageable page) {
        return format("%s LIMIT %d OFFSET %d",
                search(table, whereExp,page.getSort()), page.getPageSize(), page.getOffset());
    }
}
