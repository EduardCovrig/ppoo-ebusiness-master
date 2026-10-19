package eduard.covrig.ppoo.models;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Comanda {
    private int id;
    private LocalDateTime data;
    // produsul comandat si cantitatea lui
    private Map<Produs, Integer> produse = new LinkedHashMap<>();

    public Comanda(int id, LocalDateTime data) {
        this.id = id;
        this.data = data;
    }

    public void adauga(Produs produs, int cantitate) throws DateInvalideException {
        if (cantitate <= 0) {
            throw new DateInvalideException("Cantitatea trebuie sa fie mai mare decat 0");
        }
        // daca produsul e deja in comanda, adunam cantitatea
        produse.put(produs, produse.getOrDefault(produs, 0) + cantitate);
    }

    public double getTotal() {
        double total = 0;
        for (Map.Entry<Produs, Integer> e : produse.entrySet()) {
            total += e.getKey().getPret() * e.getValue();
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Map<Produs, Integer> getProduse() {
        return produse;
    }

    @Override
    public String toString() {
        return "Comanda " + id + " | " + data.toString().replace("T", " ") + " | total: "
                + String.format("%.2f", getTotal()) + " lei";
    }
}
