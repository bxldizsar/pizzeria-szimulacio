package pizzeria.model;
import java.util.concurrent.BlockingQueue;

record Pizza(int id, String tipus, long keszIdo) {
    static final Pizza MEREGPIRULA = new Pizza(-1, "STOP", 0);
}

class Szakacs implements Runnable {
    private final BlockingQueue<Pizza> pult;
    Szakacs(BlockingQueue<Pizza> pult) { this.pult = pult; }

    @Override
    public void run() {
        // TODO: rendelés elvétele, sütés, pult.put(...)
    }
}


