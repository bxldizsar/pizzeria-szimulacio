package pizzeria.model;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author boldi
 */
public class Statisztika 
{
     private final AtomicInteger elkeszult = new AtomicInteger();
    private final AtomicInteger kiszallitott = new AtomicInteger();
    private final AtomicLong osszVarakozasNano = new AtomicLong();
    private final Queue<Integer> kiszallitottIdk = new ConcurrentLinkedQueue<>();

    public void elkeszult()
    {
        elkeszult.incrementAndGet();
    }

    public void kiszallitva(Pizza pizza, long varakozasNano)
    {
        kiszallitott.incrementAndGet();
        osszVarakozasNano.addAndGet(varakozasNano);
        kiszallitottIdk.add(pizza.id());
    }

    public int getElkeszult()
    {
        return elkeszult.get();
    }

    public int getKiszallitott()
    {
        return kiszallitott.get();
    }

    public double atlagosVarakozasMs()
    {
        int db = kiszallitott.get();
        if(db == 0)
        {
            return 0;
        }
        return osszVarakozasNano.get() / 1_000_000.0 / db;
    }

    public List<Integer> getKiszallitottIdk()
    {
        // masolatot adunk, kivulrol ne lehessen modositani
        return List.copyOf(kiszallitottIdk);
    }
}
