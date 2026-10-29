package pizzeria.model;

public record Konfiguracio(int szakacsok,int futarok,int pultMeret,int rendelesek) 
{
    public Konfiguracio
    {
        if(szakacsok < 1 || futarok < 1 || pultMeret < 1 || rendelesek < 0)
        {
            throw new IllegalArgumentException("hibas konfiguracio");
        }
    }
    
}
