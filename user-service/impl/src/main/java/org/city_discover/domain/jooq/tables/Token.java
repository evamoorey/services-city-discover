/*
 * This file is generated by jOOQ.
 */
package org.city_discover.domain.jooq.tables;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.city_discover.domain.jooq.Keys;
import org.city_discover.domain.jooq.Public;
import org.city_discover.domain.jooq.tables.records.TokenRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function2;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * User's token
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Token extends TableImpl<TokenRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.token</code>
     */
    public static final Token TOKEN = new Token();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TokenRecord> getRecordType() {
        return TokenRecord.class;
    }

    /**
     * The column <code>public.token.user_id</code>. User id
     */
    public final TableField<TokenRecord, UUID> USER_ID = createField(DSL.name("user_id"), SQLDataType.UUID, this, "User id");

    /**
     * The column <code>public.token.refresh_token</code>. User refresh token
     */
    public final TableField<TokenRecord, String> REFRESH_TOKEN = createField(DSL.name("refresh_token"), SQLDataType.VARCHAR.nullable(false), this, "User refresh token");

    private Token(Name alias, Table<TokenRecord> aliased) {
        this(alias, aliased, null);
    }

    private Token(Name alias, Table<TokenRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("User's token"), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.token</code> table reference
     */
    public Token(String alias) {
        this(DSL.name(alias), TOKEN);
    }

    /**
     * Create an aliased <code>public.token</code> table reference
     */
    public Token(Name alias) {
        this(alias, TOKEN);
    }

    /**
     * Create a <code>public.token</code> table reference
     */
    public Token() {
        this(DSL.name("token"), null);
    }

    public <O extends Record> Token(Table<O> child, ForeignKey<O, TokenRecord> key) {
        super(child, key, TOKEN);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public List<ForeignKey<TokenRecord, ?>> getReferences() {
        return Arrays.asList(Keys.TOKEN__FK_TOKEN_USER_ID);
    }

    private transient User _user;

    /**
     * Get the implicit join path to the <code>public.user</code> table.
     */
    public User user() {
        if (_user == null)
            _user = new User(this, Keys.TOKEN__FK_TOKEN_USER_ID);

        return _user;
    }

    @Override
    public Token as(String alias) {
        return new Token(DSL.name(alias), this);
    }

    @Override
    public Token as(Name alias) {
        return new Token(alias, this);
    }

    @Override
    public Token as(Table<?> alias) {
        return new Token(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Token rename(String name) {
        return new Token(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Token rename(Name name) {
        return new Token(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Token rename(Table<?> name) {
        return new Token(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<UUID, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function2<? super UUID, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function2<? super UUID, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
