# Pizzéria-szimuláció (termelő–fogyasztó)

Bónuszfeladat **b-10**: 3 szakács süti a pizzákat, és egy korlátos méretű melegítőpultra teszi őket, ahonnan 4 futár viszi el őket. Ha a pult tele van, a szakácsok várnak, ha üres, a futárok. A program a termelő–fogyasztó problémát `BlockingQueue`-val oldja meg.

## Futtatás

- NetBeansben: **Run Project** (főosztály: `pizzeria.main.Main`)
- Tesztek: **Test Project** (Alt+F6), vagy parancssorból `mvn test`

A `Main` öt konfigurációt futtat le egymás után, mindegyiket 200 rendeléssel, és táblázatot ír ki.

## Felépítés

| Osztály | Feladat |
|---|---|
| `Pizza` (record) | Immutábilis pizza: azonosító, típus, elkészülési idő (`System.nanoTime()`). A `MEREGPIRULA` konstans jelzi a futárnak, hogy nincs több munka. |
| `Konfiguracio` (record) | A szakácsok, a futárok, a pulthelyek és a rendelések száma, ellenőrzéssel. |
| `Szakacs` (`Runnable`) | A rendelés-sorból `poll()`-lal vesz azonosítót, "süt" (5–20 ms), majd `pult.put()`. Ha nincs több rendelés, leáll. |
| `Futar` (`Runnable`) | `pult.take()`-kel vár a pizzára, méri a pulton töltött időt, és "kiszállít" (5–20 ms). A méregpirulánál leáll. |
| `Statisztika` | Szálbiztos számlálók (`AtomicInteger`, `AtomicLong`) és a kiszállított azonosítók listája. |
| `Pizzeria` | Két `ExecutorService` (szakácsok és futárok). Megvárja a szakácsokat, majd futáronként egy méregpirulát tesz a pultra. |
| `Main` | A mérések futtatása és a táblázat kiírása. |

Az osztálydiagram: [`docs/osztalydiagram.png`](docs/osztalydiagram.png) (forrás: `docs/osztalydiagram.puml`).

### Tiszta leállítás

1. `szakacsok.shutdown()` + `awaitTermination()`: megvárjuk, hogy minden pizza a pultra kerüljön.
2. Futáronként egy `Pizza.MEREGPIRULA` kerül a pultra. Minden futár egyet vesz ki, és kilép a ciklusból.
3. `futarok.shutdown()` + `awaitTermination()`.

Nincs `Thread.stop()` és nincs végtelen ciklus. Az `InterruptedException`-t nem nyeljük le, hanem visszaállítjuk a jelzőt: `Thread.currentThread().interrupt()`.

## Mérési eredmények

200 rendelés, a sütés és a kiszállítás ideje egyaránt véletlenszerűen 5–20 ms.

| Szakács | Futár | Pult | Kiszállítva | Átl. várakozás a pulton (ms) | Teljes idő (ms) |
|---:|---:|---:|---:|---:|---:|
| 1 | 4 | 10 | 200 | 0,14 | 2510 |
| 3 | 4 | 10 | 200 | 0,74 | 869 |
| 3 | 1 | 10 | 200 | 145,69 | 2542 |
| 6 | 6 | 10 | 200 | 11,48 | 473 |
| 3 | 4 | 2 | 200 | 0,68 | 883 |

**Értelmezés.**
- Egy szakáccsal a konyha a szűk keresztmetszet: a futárok szinte azonnal elviszik a pizzát, de az egész lassú.
- Egy futárral fordított a helyzet: a pult megtelik, a pizzák átlagosan ~150 ms-ot várnak rajta, és a szakácsok is blokkolódnak a `put()`-nál.
- Ha mindkét oldalt növeljük (6+6), a teljes idő a felére csökken, mert a sütés és a kiszállítás párhuzamosan fut.
- A pult mérete (2 és 10) itt alig számít, mert a két oldal átlagos tempója hasonló. A nagyobb pult csak az ingadozásokat simítja ki.

## Tesztek (JUnit 5, 7 db)

- minden pizza pontosan egyszer kerül kiszállításra (nincs elveszett vagy duplikált azonosító)
- az elkészült és a kiszállított darabszám megegyezik
- egy futárral és 1 helyes pulttal sincs holtpont
- 0 rendeléssel is leáll
- hibás konfiguráció esetén kivételt dob
- csak a valódi `MEREGPIRULA` állítja le a futárt (`==`, nem `equals`)
- a statisztika listája kívülről nem módosítható

Mindegyik futás `assertTimeoutPreemptively`-vel fut, így ha a program nem állna le, a teszt elbukik.
