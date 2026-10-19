package eduard.covrig.ppoo.service;

import eduard.covrig.ppoo.models.Comanda;
import eduard.covrig.ppoo.models.DateInvalideException;
import eduard.covrig.ppoo.models.Produs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class Fisiere {
    private static final String FISIER_PRODUSE = "produse.txt";
    private static final String FISIER_COMENZI = "comenzi.txt";

    public static void incarca(Restaurant r) throws IOException {
        // produsele se citesc primele, comenzile au nevoie de ele
        File fp = new File(FISIER_PRODUSE);
        if (fp.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fp))) {
                String linie;
                while ((linie = br.readLine()) != null) {
                    if (linie.trim().isEmpty()) {
                        continue;
                    }
                    try {
                        String[] p = linie.split(";");
                        r.adaugaProdus(new Produs(Integer.parseInt(p[0]), p[1], p[2],
                                Double.parseDouble(p[3]), Integer.parseInt(p[4])));
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException | DateInvalideException e) {
                        System.out.println("Linie ignorata din " + FISIER_PRODUSE + ": " + linie);
                    }
                }
            }
        }

        File fc = new File(FISIER_COMENZI);
        if (fc.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(fc))) {
                String linie;
                while ((linie = br.readLine()) != null) {
                    if (linie.trim().isEmpty()) {
                        continue;
                    }
                    try {
                        String[] p = linie.split(";");
                        Comanda c = new Comanda(Integer.parseInt(p[0]), LocalDateTime.parse(p[1]));
                        for (String item : p[2].split(",")) {
                            String[] x = item.split(":");
                            c.adauga(r.getProdus(Integer.parseInt(x[0])), Integer.parseInt(x[1]));
                        }
                        r.adaugaComanda(c);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException | DateTimeParseException
                             | DateInvalideException e) {
                        System.out.println("Linie ignorata din " + FISIER_COMENZI + ": " + linie);
                    }
                }
            }
        }
    }

    public static void salveaza(Restaurant r) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FISIER_PRODUSE))) {
            for (Produs p : r.getProduse().values()) {
                pw.println(p.getId() + ";" + p.getNume() + ";" + p.getCategorie() + ";" + p.getPret() + ";"
                        + p.getGramaj());
            }
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(FISIER_COMENZI))) {
            for (Comanda c : r.getComenzi()) {
                String produse = "";
                for (Map.Entry<Produs, Integer> e : c.getProduse().entrySet()) {
                    produse += e.getKey().getId() + ":" + e.getValue() + ",";
                }
                // scoatem ultima virgula
                produse = produse.substring(0, produse.length() - 1);
                pw.println(c.getId() + ";" + c.getData() + ";" + produse);
            }
        }
    }
}
