package eduard.covrig.ppoo.service;

import eduard.covrig.ppoo.models.Comanda;
import eduard.covrig.ppoo.models.DateInvalideException;
import eduard.covrig.ppoo.models.Produs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Restaurant {
    // produsele, sortate dupa id
    private Map<Integer, Produs> produse = new TreeMap<>();
    private ArrayList<Comanda> comenzi = new ArrayList<>();

    public void adaugaProdus(Produs p) throws DateInvalideException {
        if (produse.containsKey(p.getId())) {
            throw new DateInvalideException("Exista deja un produs cu id-ul " + p.getId());
        }
        produse.put(p.getId(), p);
    }

    public Produs getProdus(int id) throws DateInvalideException {
        Produs p = produse.get(id);
        if (p == null) {
            throw new DateInvalideException("Produsul cu id-ul " + id + " nu exista");
        }
        return p;
    }

    public void stergeProdus(int id) throws DateInvalideException {
        Produs p = getProdus(id);
        // nu stergem un produs care apare deja intr-o comanda
        for (Comanda c : comenzi) {
            if (c.getProduse().containsKey(p)) {
                throw new DateInvalideException("Produsul apare in comanda " + c.getId() + ", nu poate fi sters");
            }
        }
        produse.remove(id);
    }

    public void adaugaComanda(Comanda c) throws DateInvalideException {
        if (c.getProduse().isEmpty()) {
            throw new DateInvalideException("Comanda nu are niciun produs");
        }
        for (Comanda existenta : comenzi) {
            if (existenta.getId() == c.getId()) {
                throw new DateInvalideException("Exista deja o comanda cu id-ul " + c.getId());
            }
        }
        comenzi.add(c);
    }

    public Comanda getComanda(int id) throws DateInvalideException {
        for (Comanda c : comenzi) {
            if (c.getId() == id) {
                return c;
            }
        }
        throw new DateInvalideException("Comanda cu id-ul " + id + " nu exista");
    }

    public void stergeComanda(int id) throws DateInvalideException {
        comenzi.remove(getComanda(id));
    }

    public ArrayList<Comanda> comenziDinZi(LocalDate zi) {
        ArrayList<Comanda> rezultat = new ArrayList<>();
        for (Comanda c : comenzi) {
            if (c.getData().toLocalDate().equals(zi)) {
                rezultat.add(c);
            }
        }
        return rezultat;
    }

    public int idNouProdus() {
        int max = 0;
        for (int id : produse.keySet()) {
            if (id > max) {
                max = id;
            }
        }
        return max + 1;
    }

    public int idNouComanda() {
        int max = 0;
        for (Comanda c : comenzi) {
            if (c.getId() > max) {
                max = c.getId();
            }
        }
        return max + 1;
    }

    public Map<Integer, Produs> getProduse() {
        return produse;
    }

    public ArrayList<Comanda> getComenzi() {
        return comenzi;
    }
}
