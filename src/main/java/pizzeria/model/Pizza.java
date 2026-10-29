package pizzeria.model;

public record Pizza(int id, String tipus, long keszIdo)
{
    // ezzel jelezzuk a futaroknak, hogy nincs tobb rendeles
    public static final Pizza MEREGPIRULA = new Pizza(-1, "STOP", 0);

    public boolean meregpirula()
    {
        return this == MEREGPIRULA;
    }
}

