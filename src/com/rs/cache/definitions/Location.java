package com.rs.cache.definitions;

public class Location
{
    private int id;
    private int type;
    private int orientation;
    private Position position;

    public Location(int id, int type, int orientation, Position position) {
        this.id = id;
        this.type = type;
        this.orientation = orientation;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Location other = (Location) obj;
        if (this.position.getX() == other.getPosition().getX() && this.position.getY() == other.getPosition().getY() && other.getId() == this.getId())
        {
            return true;
        }

        return false;
    }
}
