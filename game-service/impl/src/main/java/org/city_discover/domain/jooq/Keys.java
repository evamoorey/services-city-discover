/*
 * This file is generated by jOOQ.
 */
package org.city_discover.domain.jooq;


import org.city_discover.domain.jooq.tables.Kitty;
import org.city_discover.domain.jooq.tables.Owner;
import org.city_discover.domain.jooq.tables.records.KittyRecord;
import org.city_discover.domain.jooq.tables.records.OwnerRecord;
import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<KittyRecord> PK_PLACE_UUID = Internal.createUniqueKey(Kitty.KITTY, DSL.name("pk_place_uuid"), new TableField[] { Kitty.KITTY.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<OwnerRecord, KittyRecord> OWNER__FK_OWNER_KITTY_ID = Internal.createForeignKey(Owner.OWNER, DSL.name("fk_owner_kitty_id"), new TableField[] { Owner.OWNER.KITTY_ID }, Keys.PK_PLACE_UUID, new TableField[] { Kitty.KITTY.ID }, true);
}
