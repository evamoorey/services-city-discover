/*
 * This file is generated by jOOQ.
 */
package org.user_service.domain.jooq.tables;


import java.time.Instant;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.user_service.domain.jooq.Public;
import org.user_service.domain.jooq.tables.records.AuthCodeRecord;


/**
 * User's disposable codes for auth
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AuthCode extends TableImpl<AuthCodeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.auth_code</code>
     */
    public static final AuthCode AUTH_CODE = new AuthCode();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AuthCodeRecord> getRecordType() {
        return AuthCodeRecord.class;
    }

    /**
     * The column <code>public.auth_code.email</code>. User email
     */
    public final TableField<AuthCodeRecord, String> EMAIL = createField(DSL.name("email"), SQLDataType.VARCHAR.nullable(false), this, "User email");

    /**
     * The column <code>public.auth_code.code</code>. Disposable auth code
     */
    public final TableField<AuthCodeRecord, String> CODE = createField(DSL.name("code"), SQLDataType.VARCHAR.nullable(false), this, "Disposable auth code");

    /**
     * The column <code>public.auth_code.creation_date</code>. Code creation
     * time
     */
    public final TableField<AuthCodeRecord, Instant> CREATION_DATE = createField(DSL.name("creation_date"), SQLDataType.INSTANT.nullable(false).defaultValue(DSL.field(DSL.raw("now()"), SQLDataType.INSTANT)), this, "Code creation time");

    private AuthCode(Name alias, Table<AuthCodeRecord> aliased) {
        this(alias, aliased, null);
    }

    private AuthCode(Name alias, Table<AuthCodeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("User's disposable codes for auth"), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.auth_code</code> table reference
     */
    public AuthCode(String alias) {
        this(DSL.name(alias), AUTH_CODE);
    }

    /**
     * Create an aliased <code>public.auth_code</code> table reference
     */
    public AuthCode(Name alias) {
        this(alias, AUTH_CODE);
    }

    /**
     * Create a <code>public.auth_code</code> table reference
     */
    public AuthCode() {
        this(DSL.name("auth_code"), null);
    }

    public <O extends Record> AuthCode(Table<O> child, ForeignKey<O, AuthCodeRecord> key) {
        super(child, key, AUTH_CODE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public AuthCode as(String alias) {
        return new AuthCode(DSL.name(alias), this);
    }

    @Override
    public AuthCode as(Name alias) {
        return new AuthCode(alias, this);
    }

    @Override
    public AuthCode as(Table<?> alias) {
        return new AuthCode(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public AuthCode rename(String name) {
        return new AuthCode(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AuthCode rename(Name name) {
        return new AuthCode(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public AuthCode rename(Table<?> name) {
        return new AuthCode(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, Instant> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super String, ? super String, ? super Instant, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function3<? super String, ? super String, ? super Instant, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
