package com.rs.cache.definitions;

public class Position
{
    private final int x;
    private final int y;
    private final int z;

    public Position(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString()
    {
        return "Position{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + this.x;
        hash = 67 * hash + this.y;
        hash = 67 * hash + this.z;
        return hash;
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
        final Position other = (Position) obj;
        if (this.x != other.x)
        {
            return false;
        }
        if (this.y != other.y)
        {
            return false;
        }
        if (this.z != other.z)
        {
            return false;
        }
        return true;
    }

    public int getChunkY() {
        return (y >> 3);
    }



    public int getLocalX() {
        return x - 8 * ((x >> 3));
    }

    public int getLocalY() {
        return y - 8 * (y >> 3);
    }
    public int getRegionX() {
        return (x >> 6);
    }

    public int getRegionY() {
        return (y >> 6);
    }

    public int getRegionId() {
        return ( x << 8 | y);
    }


    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }
}
