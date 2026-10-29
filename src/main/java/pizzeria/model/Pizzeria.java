package pizzeria.model;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author boldi
 */
public class Pizzeria 
{
      private final Konfiguracio konfig;

    public Pizzeria(Konfiguracio konfig)
    {
        this.konfig = konfig;
    }

    public Statisztika futtat() throws InterruptedException
    {
        BlockingQueue<Integer> rendelesek = new LinkedBlockingQueue<>();
        for(int i = 1; i <= konfig.rendelesek(); i++)
        {
            rendelesek.add(i);
        }
        // korlatos pult: ha tele van, a put() var
        BlockingQueue<Pizza> pult = new LinkedBlockingQueue<>(konfig.pultMeret());
        Statisztika statisztika = new Statisztika();

        ExecutorService szakacsok = Executors.newFixedThreadPool(konfig.szakacsok());
        ExecutorService futarok = Executors.newFixedThreadPool(konfig.futarok());
        for(int i = 0; i < konfig.szakacsok(); i++)
        {
            szakacsok.submit(new Szakacs(rendelesek, pult, statisztika));
        }
        for(int i = 0; i < konfig.futarok(); i++)
        {
            futarok.submit(new Futar(pult, statisztika));
        }

        // megvarjuk, hogy minden pizza elkeszuljon
        szakacsok.shutdown();
        szakacsok.awaitTermination(1, TimeUnit.MINUTES);

        // futaronkent egy meregpirula, igy mind leall
        for(int i = 0; i < konfig.futarok(); i++)
        {
            pult.put(Pizza.MEREGPIRULA);
        }
        futarok.shutdown();
        futarok.awaitTermination(1, TimeUnit.MINUTES);
        return statisztika;
    }}
