package pizzeria.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class PizzeriaTest
{
    private Statisztika futtat(int szakacsok, int futarok, int pult, int rendelesek)
    {
        return assertTimeoutPreemptively(Duration.ofSeconds(10),
                () -> new Pizzeria(new Konfiguracio(szakacsok, futarok, pult, rendelesek)).futtat());
    }

    @Test
    public void mindenPizzaPontosanEgyszerMegyKi()
    {
        Statisztika stat = futtat(3, 4, 10, 50);
        List<Integer> idk = new ArrayList<>(stat.getKiszallitottIdk());

        // nincs duplikalt elem
        Set<Integer> egyediek = new HashSet<>(idk);
        assertEquals(idk.size(), egyediek.size());

        // nincs elveszett elem
        for(int i = 1; i <= 50; i++)
        {
            assertTrue(egyediek.contains(i), "hianyzik: " + i);
        }
    }

    @Test
    public void elkeszultEsKiszallitottDarabszamEgyezik()
    {
        Statisztika stat = futtat(2, 2, 5, 40);
        assertEquals(40, stat.getElkeszult());
        assertEquals(40, stat.getKiszallitott());
    }

    @Test
    public void egyFutarralEsKisPulttalIsLeall()
    {
        // tele pultnal sem lehet holtpont
        Statisztika stat = futtat(4, 1, 1, 30);
        assertEquals(30, stat.getKiszallitott());
    }

    @Test
    public void rendelesNelkulIsLeall()
    {
        Statisztika stat = futtat(3, 4, 10, 0);
        assertEquals(0, stat.getKiszallitott());
        assertEquals(0, stat.atlagosVarakozasMs());
    }

    @Test
    public void hibasKonfiguracioKivetelt()
    {
        assertThrows(IllegalArgumentException.class, () -> new Konfiguracio(0, 4, 10, 200));
        assertThrows(IllegalArgumentException.class, () -> new Konfiguracio(3, 0, 10, 200));
        assertThrows(IllegalArgumentException.class, () -> new Konfiguracio(3, 4, 0, 200));
        assertDoesNotThrow(() -> new Konfiguracio(1, 1, 1, 0));
    }

    @Test
    public void csakAValodiMeregpirulaAllitjaLeAFutart()
    {
        assertTrue(Pizza.MEREGPIRULA.meregpirula());
        assertFalse(new Pizza(-1, "STOP", 0).meregpirula());
    }

    @Test
    public void statisztikaListajaNemModosithato()
    {
        Statisztika stat = futtat(1, 1, 1, 3);
        assertThrows(UnsupportedOperationException.class, () -> stat.getKiszallitottIdk().add(99));
    }
}