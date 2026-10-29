package pizzeria.main;

import java.util.List;
import pizzeria.model.Konfiguracio;
import pizzeria.model.Pizzeria;
import pizzeria.model.Statisztika;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        List<Konfiguracio> konfigok = List.of(
                new Konfiguracio(1, 4, 10, 200),
                new Konfiguracio(3, 4, 10, 200),
                new Konfiguracio(3, 1, 10, 200),
                new Konfiguracio(6, 6, 10, 200),
                new Konfiguracio(3, 4, 2, 200));

        System.out.println("Szakacs | Futar | Pult | Rendeles | Kiszallitva | Atl. varakozas (ms) | Ido (ms)");
        for(Konfiguracio k : konfigok)
        {
            long kezdet = System.nanoTime();
            Statisztika stat = new Pizzeria(k).futtat();
            long ido = (System.nanoTime() - kezdet) / 1000000;
            System.out.printf("%7d | %5d | %4d | %8d | %11d | %19.2f | %8d%n",
                    k.szakacsok(), k.futarok(), k.pultMeret(), k.rendelesek(),
                    stat.getKiszallitott(), stat.atlagosVarakozasMs(), ido);
        }
    }
}