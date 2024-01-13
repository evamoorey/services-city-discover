/*
 * This file is generated by jOOQ.
 */
package org.user_service.domain.jooq.tables.records;


import java.time.Instant;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;
import org.user_service.domain.jooq.tables.User;


/**
 * User's info
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserRecord extends UpdatableRecordImpl<UserRecord> implements Record4<UUID, String, String, Instant> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.user.id</code>. User id
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.user.id</code>. User id
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.user.email</code>. User email
     */
    public void setEmail(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.user.email</code>. User email
     */
    public String getEmail() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.user.username</code>. Username
     */
    public void setUsername(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.user.username</code>. Username
     */
    public String getUsername() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.user.creation_date</code>. Code creation time
     */
    public void setCreationDate(Instant value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.user.creation_date</code>. Code creation time
     */
    public Instant getCreationDate() {
        return (Instant) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<UUID, String, String, Instant> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<UUID, String, String, Instant> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return User.USER.ID;
    }

    @Override
    public Field<String> field2() {
        return User.USER.EMAIL;
    }

    @Override
    public Field<String> field3() {
        return User.USER.USERNAME;
    }

    @Override
    public Field<Instant> field4() {
        return User.USER.CREATION_DATE;
    }

    @Override
    public UUID component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getEmail();
    }

    @Override
    public String component3() {
        return getUsername();
    }

    @Override
    public Instant component4() {
        return getCreationDate();
    }

    @Override
    public UUID value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getEmail();
    }

    @Override
    public String value3() {
        return getUsername();
    }

    @Override
    public Instant value4() {
        return getCreationDate();
    }

    @Override
    public UserRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public UserRecord value2(String value) {
        setEmail(value);
        return this;
    }

    @Override
    public UserRecord value3(String value) {
        setUsername(value);
        return this;
    }

    @Override
    public UserRecord value4(Instant value) {
        setCreationDate(value);
        return this;
    }

    @Override
    public UserRecord values(UUID value1, String value2, String value3, Instant value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UserRecord
     */
    public UserRecord() {
        super(User.USER);
    }

    /**
     * Create a detached, initialised UserRecord
     */
    public UserRecord(UUID id, String email, String username, Instant creationDate) {
        super(User.USER);

        setId(id);
        setEmail(email);
        setUsername(username);
        setCreationDate(creationDate);
        resetChangedOnNotNull();
    }
}
