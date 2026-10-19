package eduard.covrig.ppoo.models;

public class Produs {
    private int id;
    private String nume;
    private String categorie;
    private double pret;
    private int gramaj;

    public Produs(int id, String nume, String categorie, double pret, int gramaj) throws DateInvalideException {
        if (id <= 0) {
            throw new DateInvalideException("Id-ul trebuie sa fie pozitiv");
        }
        if (nume == null || nume.trim().isEmpty() || nume.contains(";")) {
            throw new DateInvalideException("Numele produsului este invalid");
        }
        if (categorie == null || categorie.trim().isEmpty() || categorie.contains(";")) {
            throw new DateInvalideException("Categoria produsului este invalida");
        }
        if (pret <= 0) {
            throw new DateInvalideException("Pretul trebuie sa fie mai mare decat 0");
        }
        if (gramaj <= 0) {
            throw new DateInvalideException("Gramajul trebuie sa fie mai mare decat 0");
        }
        this.id = id;
        this.nume = nume.trim();
        this.categorie = categorie.trim();
        this.pret = pret;
        this.gramaj = gramaj;
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public String getCategorie() {
        return categorie;
    }

    public double getPret() {
        return pret;
    }

    public int getGramaj() {
        return gramaj;
    }

    @Override
    public String toString() {
        return id + ". " + nume + " (" + categorie + ") - " + pret + " lei, " + gramaj + " g";
    }
}
