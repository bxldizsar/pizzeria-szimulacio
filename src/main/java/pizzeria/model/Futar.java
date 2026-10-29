package pizzeria.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Futar implements Runnable
{
    private final BlockingQueue<Pizza> pult;
    private final Statisztika statisztika;

    public Futar(BlockingQueue<Pizza> pult, Statisztika statisztika)
    {
        this.pult = pult;
        this.statisztika = statisztika;
    }

    @Override
    public void run()
    {
        try
        {
            Pizza pizza = pult.take();
            while(!pizza.meregpirula())
            {
                // ennyit allt a pizza a pulton
                long varakozas = System.nanoTime() - pizza.keszIdo();
                // kiszallitas szimulalasa
                Thread.sleep(ThreadLocalRandom.current().nextLong(5, 21));
                statisztika.kiszallitva(pizza, varakozas);
                pizza = pult.take();
            }
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}