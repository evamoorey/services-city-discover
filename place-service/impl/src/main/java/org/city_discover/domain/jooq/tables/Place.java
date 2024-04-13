/*
 * This file is generated by jOOQ.
 */
package org.city_discover.domain.jooq.tables;


import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.city_discover.domain.jooq.Keys;
import org.city_discover.domain.jooq.Public;
import org.city_discover.domain.jooq.tables.records.PlaceRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function9;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row9;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * Places
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Place extends TableImpl<PlaceRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.place</code>
     */
    public static final Place PLACE = new Place();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PlaceRecord> getRecordType() {
        return PlaceRecord.class;
    }

    /**
     * The column <code>public.place.id</code>. Place ID
     */
    public final TableField<PlaceRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("gen_random_uuid()"), SQLDataType.UUID)), this, "Place ID");

    /**
     * The column <code>public.place.name</code>. Place name
     */
    public final TableField<PlaceRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR.nullable(false), this, "Place name");

    /**
     * The column <code>public.place.description</code>. Place description
     */
    public final TableField<PlaceRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR, this, "Place description");

    /**
     * The column <code>public.place.creation_date</code>. Creation date
     */
    public final TableField<PlaceRecord, Instant> CREATION_DATE = createField(DSL.name("creation_date"), SQLDataType.INSTANT.nullable(false).defaultValue(DSL.field(DSL.raw("now()"), SQLDataType.INSTANT)), this, "Creation date");

    /**
     * The column <code>public.place.modification_date</code>. Modification date
     */
    public final TableField<PlaceRecord, Instant> MODIFICATION_DATE = createField(DSL.name("modification_date"), SQLDataType.INSTANT.nullable(false).defaultValue(DSL.field(DSL.raw("now()"), SQLDataType.INSTANT)), this, "Modification date");

    /**
     * The column <code>public.place.author</code>. Place author (system or user
     * ID)
     */
    public final TableField<PlaceRecord, String> AUTHOR = createField(DSL.name("author"), SQLDataType.VARCHAR.nullable(false), this, "Place author (system or user ID)");

    /**
     * The column <code>public.place.latitude</code>. Place latitude
     */
    public final TableField<PlaceRecord, Double> LATITUDE = createField(DSL.name("latitude"), SQLDataType.DOUBLE.nullable(false), this, "Place latitude");

    /**
     * The column <code>public.place.longitude</code>. Place longitude
     */
    public final TableField<PlaceRecord, Double> LONGITUDE = createField(DSL.name("longitude"), SQLDataType.DOUBLE.nullable(false), this, "Place longitude");

    /**
     * The column <code>public.place.photo_id</code>. Place photo ids
     */
    public final TableField<PlaceRecord, String[]> PHOTO_ID = createField(DSL.name("photo_id"), SQLDataType.VARCHAR(36).array(), this, "Place photo ids");

    private Place(Name alias, Table<PlaceRecord> aliased) {
        this(alias, aliased, null);
    }

    private Place(Name alias, Table<PlaceRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("Places"), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.place</code> table reference
     */
    public Place(String alias) {
        this(DSL.name(alias), PLACE);
    }

    /**
     * Create an aliased <code>public.place</code> table reference
     */
    public Place(Name alias) {
        this(alias, PLACE);
    }

    /**
     * Create a <code>public.place</code> table reference
     */
    public Place() {
        this(DSL.name("place"), null);
    }

    public <O extends Record> Place(Table<O> child, ForeignKey<O, PlaceRecord> key) {
        super(child, key, PLACE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<PlaceRecord> getPrimaryKey() {
        return Keys.PK_PLACE_UUID;
    }

    @Override
    public List<UniqueKey<PlaceRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.UNIQUE_PLACE_NAME);
    }

    @Override
    public Place as(String alias) {
        return new Place(DSL.name(alias), this);
    }

    @Override
    public Place as(Name alias) {
        return new Place(alias, this);
    }

    @Override
    public Place as(Table<?> alias) {
        return new Place(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Place rename(String name) {
        return new Place(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Place rename(Name name) {
        return new Place(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Place rename(Table<?> name) {
        return new Place(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<UUID, String, String, Instant, Instant, String, Double, Double, String[]> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function9<? super UUID, ? super String, ? super String, ? super Instant, ? super Instant, ? super String, ? super Double, ? super Double, ? super String[], ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function9<? super UUID, ? super String, ? super String, ? super Instant, ? super Instant, ? super String, ? super Double, ? super Double, ? super String[], ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
