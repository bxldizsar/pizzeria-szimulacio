package pizzeria.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Szakacs implements Runnable
{
    private static final String[] TIPUSOK = { "Margherita", "Hawaii", "Songoku", "Quattro Formaggi" };

    private final BlockingQueue<Integer> rendelesek;
    private final BlockingQueue<Pizza> pult;
    private final Statisztika statisztika;

    public Szakacs(BlockingQueue<Integer> rendelesek, BlockingQueue<Pizza> pult, Statisztika statisztika)
    {
        this.rendelesek = rendelesek;
        this.pult = pult;
        this.statisztika = statisztika;
    }

    @Override
    public void run()
    {
        try
        {
            Integer id = rendelesek.poll();
            while(id != null)
            {
                // sutes szimulalasa
                Thread.sleep(ThreadLocalRandom.current().nextLong(5, 21));
                pult.put(new Pizza(id, TIPUSOK[id % TIPUSOK.length], System.nanoTime()));
                statisztika.elkeszult();
                id = rendelesek.poll();
            }
        }
        catch(InterruptedException e)
        {
            // nem nyeljuk le, visszaallitjuk a jelzot
            Thread.currentThread().interrupt();
        }
    }
}