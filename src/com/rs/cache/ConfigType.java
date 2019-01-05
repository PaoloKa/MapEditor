package com.rs.cache;

public enum ConfigType
{
    // types from https://github.com/im-frizzy/OpenRS/blob/master/source/net/openrs/cache/type/ConfigArchive.java
    UNDERLAY(1),
    IDENTKIT(3),
    OVERLAY(4),
    INV(5),
    OBJECT(6),
    ENUM(8),
    NPC(9),
    ITEM(10),
    SEQUENCE(12),
    SPOTANIM(13),
    VARBIT(14),
    VARCLIENT(19),
    VARCLIENTSTRING(15),
    VARPLAYER(16),
    STRUCT(34),
    AREA(35);

    private final int id;

    ConfigType(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}