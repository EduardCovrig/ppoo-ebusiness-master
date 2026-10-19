package eduard.covrig.ppoo.service;

import eduard.covrig.ppoo.models.Comanda;
import eduard.covrig.ppoo.models.DateInvalideException;
import eduard.covrig.ppoo.models.Produs;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rapoarte {
    private Restaurant restaurant;
    private String[] zile = {"Luni", "Marti", "Miercuri", "Joi", "Vineri", "Sambata", "Duminica"};

    public Rapoarte(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public double totalZi(LocalDate zi) {
        double suma = 0;
        for (Comanda c : restaurant.comenziDinZi(zi)) {
            suma += c.getTotal();
        }
        return suma;
    }

    public int nrComenzi(LocalDate de, LocalDate pana) throws DateInvalideException {
        if (de.isAfter(pana)) {
            throw new DateInvalideException("Data de inceput este dupa data de sfarsit");
        }
        int nr = 0;
        for (Comanda c : restaurant.getComenzi()) {
            LocalDate zi = c.getData().toLocalDate();
            if (!zi.isBefore(de) && !zi.isAfter(pana)) {
                nr++;
            }
        }
        return nr;
    }

    public List<Map.Entry<Produs, Integer>> topProduse(int n) {
        Map<Produs, Integer> cantitati = new HashMap<>();
        for (Comanda c : restaurant.getComenzi()) {
            for (Map.Entry<Produs, Integer> e : c.getProduse().entrySet()) {
                cantitati.put(e.getKey(), cantitati.getOrDefault(e.getKey(), 0) + e.getValue());
            }
        }
        List<Map.Entry<Produs, Integer>> lista = new ArrayList<>(cantitati.entrySet());
        // sortam descrescator dupa cantitate
        lista.sort((a, b) -> b.getValue() - a.getValue());
        return lista.subList(0, Math.min(n, lista.size()));
    }

    public int cantitateProdus(Produs p) {
        int cant = 0;
        for (Comanda c : restaurant.getComenzi()) {
            cant += c.getProduse().getOrDefault(p, 0);
        }
        return cant;
    }

    public double vanzariProdus(Produs p) {
        return cantitateProdus(p) * p.getPret();
    }

    public double valoareMedie() {
        if (restaurant.getComenzi().isEmpty()) {
            return 0;
        }
        double suma = 0;
        for (Comanda c : restaurant.getComenzi()) {
            suma += c.getTotal();
        }
        return suma / restaurant.getComenzi().size();
    }

    // incasarile pe fiecare zi a saptamanii (0 = luni)
    public double[] incasariPeZiSaptamana() {
        double[] incasari = new double[7];
        for (Comanda c : restaurant.getComenzi()) {
            incasari[c.getData().getDayOfWeek().getValue() - 1] += c.getTotal();
        }
        return incasari;
    }

    // numarul de comenzi pe fiecare ora din zi
    public int[] comenziPeOra() {
        int[] comenzi = new int[24];
        for (Comanda c : restaurant.getComenzi()) {
            comenzi[c.getData().getHour()]++;
        }
        return comenzi;
    }

    public String raportComplet() {
        String t = "RAPORT RESTAURANT\n";
        t += "Numar comenzi: " + restaurant.getComenzi().size() + "\n";
        t += "Valoare medie comanda: " + String.format("%.2f", valoareMedie()) + " lei\n\n";

        t += "Top 5 produse vandute:\n";
        for (Map.Entry<Produs, Integer> e : topProduse(5)) {
            t += "  " + e.getKey().getNume() + " - " + e.getValue() + " buc\n";
        }

        t += "\nVanzari pe produs:\n";
        for (Produs p : restaurant.getProduse().values()) {
            t += "  " + p.getNume() + ": " + cantitateProdus(p) + " buc, "
                    + String.format("%.2f", vanzariProdus(p)) + " lei\n";
        }

        t += "\nIncasari pe zi a saptamanii:\n";
        double[] incasari = incasariPeZiSaptamana();
        for (int i = 0; i < incasari.length; i++) {
            t += "  " + zile[i] + ": " + String.format("%.2f", incasari[i]) + " lei\n";
        }

        t += "\nComenzi pe ora:\n";
        int[] comenzi = comenziPeOra();
        for (int ora = 0; ora < comenzi.length; ora++) {
            if (comenzi[ora] > 0) {
                t += "  " + ora + ":00 - " + comenzi[ora] + " comenzi\n";
            }
        }
        return t;
    }

    public void salveazaRaport() throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter("raport.txt"))) {
            pw.print(raportComplet());
        }
    }

    public String[] getZile() {
        return zile;
    }
}
