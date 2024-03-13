package org.city_discover.config.jooq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultExecuteListener;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

@Slf4j
public class ExceptionTranslator extends DefaultExecuteListener {

    @Override
    public void exception(ExecuteContext context) {
        SQLDialect dialect = context.configuration().dialect();
        SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dialect.name());

        // Если запрос вернул несколько записей, а в коде попытка fetch-ить результат запроса в 1 объект, то context.sqlException() равен null
        if (context.exception() != null) {
            log.error(ExceptionUtils.getStackTrace(context.exception()));
        }
        if (context.sqlException() != null) {
            context.exception(translator.translate("jOOQ", context.sql(), context.sqlException()));
        }
    }
}