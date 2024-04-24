/*
 * This file is generated by jOOQ.
 */
package org.city_discover.domain.jooq.tables.records;


import java.util.UUID;

import org.city_discover.domain.jooq.tables.Owner;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.TableRecordImpl;


/**
 * Owners
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OwnerRecord extends TableRecordImpl<OwnerRecord> implements Record2<UUID, UUID> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.owner.owner_id</code>. Owner id
     */
    public void setOwnerId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.owner.owner_id</code>. Owner id
     */
    public UUID getOwnerId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.owner.kitty_id</code>. Kitty id
     */
    public void setKittyId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.owner.kitty_id</code>. Kitty id
     */
    public UUID getKittyId() {
        return (UUID) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<UUID, UUID> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<UUID, UUID> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Owner.OWNER.OWNER_ID;
    }

    @Override
    public Field<UUID> field2() {
        return Owner.OWNER.KITTY_ID;
    }

    @Override
    public UUID component1() {
        return getOwnerId();
    }

    @Override
    public UUID component2() {
        return getKittyId();
    }

    @Override
    public UUID value1() {
        return getOwnerId();
    }

    @Override
    public UUID value2() {
        return getKittyId();
    }

    @Override
    public OwnerRecord value1(UUID value) {
        setOwnerId(value);
        return this;
    }

    @Override
    public OwnerRecord value2(UUID value) {
        setKittyId(value);
        return this;
    }

    @Override
    public OwnerRecord values(UUID value1, UUID value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached OwnerRecord
     */
    public OwnerRecord() {
        super(Owner.OWNER);
    }

    /**
     * Create a detached, initialised OwnerRecord
     */
    public OwnerRecord(UUID ownerId, UUID kittyId) {
        super(Owner.OWNER);

        setOwnerId(ownerId);
        setKittyId(kittyId);
        resetChangedOnNotNull();
    }
}