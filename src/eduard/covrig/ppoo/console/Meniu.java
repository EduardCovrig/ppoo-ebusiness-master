package eduard.covrig.ppoo.console;

import eduard.covrig.ppoo.models.Comanda;
import eduard.covrig.ppoo.models.DateInvalideException;
import eduard.covrig.ppoo.models.Produs;
import eduard.covrig.ppoo.service.Rapoarte;
import eduard.covrig.ppoo.service.Restaurant;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Meniu {
    private Restaurant restaurant;
    private Rapoarte rapoarte;
    private Scanner sc = new Scanner(System.in);

    public Meniu(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.rapoarte = new Rapoarte(restaurant);
    }

    public void start() {
        int optiune = -1;
        while (optiune != 0) {
            System.out.println("\n===== MENIU RESTAURANT =====");
            System.out.println("1. Afiseaza produsele");
            System.out.println("2. Adauga produs");
            System.out.println("3. Sterge produs");
            System.out.println("4. Comanda noua");
            System.out.println("5. Afiseaza toate comenzile");
            System.out.println("6. Detalii comanda");
            System.out.println("7. Sterge comanda");
            System.out.println("8. Rapoarte");
            System.out.println("0. Iesire (salveaza datele)");
            try {
                optiune = citesteInt("Alege optiunea: ");
                switch (optiune) {
                    case 1:
                        afiseazaProduse();
                        break;
                    case 2:
                        adaugaProdus();
                        break;
                    case 3:
                        restaurant.stergeProdus(citesteInt("Id produs: "));
                        System.out.println("Produs sters");
                        break;
                    case 4:
                        comandaNoua();
                        break;
                    case 5:
                        for (Comanda c : restaurant.getComenzi()) {
                            System.out.println(c);
                        }
                        break;
                    case 6:
                        detaliiComanda();
                        break;
                    case 7:
                        restaurant.stergeComanda(citesteInt("Id comanda: "));
                        System.out.println("Comanda stearsa");
                        break;
                    case 8:
                        meniuRapoarte();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Optiune inexistenta");
                }
            } catch (NumberFormatException e) {
                System.out.println("Trebuie sa introduci un numar valid!");
            } catch (DateTimeParseException e) {
                System.out.println("Data invalida, foloseste formatul 2026-10-19");
            } catch (DateInvalideException e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }
    }

    private void meniuRapoarte() {
        int optiune = -1;
        while (optiune != 0) {
            System.out.println("\n--- RAPOARTE ---");
            System.out.println("1. Valoarea comenzilor dintr-o zi");
            System.out.println("2. Numarul de comenzi dintr-o perioada");
            System.out.println("3. Cele mai vandute produse");
            System.out.println("4. Vanzarile unui produs");
            System.out.println("5. Comenzile dintr-o zi");
            System.out.println("6. Valoarea medie a unei comenzi");
            System.out.println("7. Incasari pe zile si comenzi pe ore");
            System.out.println("8. Salveaza raportul complet in raport.txt");
            System.out.println("0. Inapoi");
            try {
                optiune = citesteInt("Alege optiunea: ");
                switch (optiune) {
                    case 1:
                        LocalDate zi = citesteData("Ziua");
                        System.out.println("Total: " + String.format("%.2f", rapoarte.totalZi(zi)) + " lei");
                        break;
                    case 2:
                        LocalDate de = citesteData("De la");
                        LocalDate pana = citesteData("Pana la");
                        System.out.println("Numar comenzi: " + rapoarte.nrComenzi(de, pana));
                        break;
                    case 3:
                        int n = citesteInt("Cate produse sa afisez: ");
                        List<Map.Entry<Produs, Integer>> top = rapoarte.topProduse(n);
                        for (Map.Entry<Produs, Integer> e : top) {
                            System.out.println(e.getKey().getNume() + " - " + e.getValue() + " buc");
                        }
                        break;
                    case 4:
                        Produs p = restaurant.getProdus(citesteInt("Id produs: "));
                        System.out.println(p.getNume() + ": " + rapoarte.cantitateProdus(p) + " buc, "
                                + String.format("%.2f", rapoarte.vanzariProdus(p)) + " lei");
                        break;
                    case 5:
                        List<Comanda> comenzi = restaurant.comenziDinZi(citesteData("Ziua"));
                        if (comenzi.isEmpty()) {
                            System.out.println("Nu exista comenzi in ziua aceea");
                        }
                        for (Comanda c : comenzi) {
                            System.out.println(c);
                        }
                        break;
                    case 6:
                        System.out.println("Valoare medie: " + String.format("%.2f", rapoarte.valoareMedie()) + " lei");
                        break;
                    case 7:
                        double[] incasari = rapoarte.incasariPeZiSaptamana();
                        for (int i = 0; i < incasari.length; i++) {
                            System.out.println(rapoarte.getZile()[i] + ": " + String.format("%.2f", incasari[i]) + " lei");
                        }
                        int[] peOra = rapoarte.comenziPeOra();
                        for (int ora = 0; ora < peOra.length; ora++) {
                            if (peOra[ora] > 0) {
                                System.out.println("Ora " + ora + ":00 - " + peOra[ora] + " comenzi");
                            }
                        }
                        break;
                    case 8:
                        rapoarte.salveazaRaport();
                        System.out.println("Raport salvat in raport.txt");
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Optiune inexistenta");
                }
            } catch (NumberFormatException e) {
                System.out.println("Trebuie sa introduci un numar valid!");
            } catch (DateTimeParseException e) {
                System.out.println("Data invalida, foloseste formatul 2026-10-19");
            } catch (DateInvalideException e) {
                System.out.println("Eroare: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Eroare la scrierea fisierului: " + e.getMessage());
            }
        }
    }

    private void afiseazaProduse() {
        for (Produs p : restaurant.getProduse().values()) {
            System.out.println(p);
        }
    }

    private void adaugaProdus() throws DateInvalideException {
        System.out.print("Nume: ");
        String nume = sc.nextLine();
        System.out.print("Categorie: ");
        String categorie = sc.nextLine();
        double pret = Double.parseDouble(citesteText("Pret: ").replace(",", "."));
        int gramaj = citesteInt("Gramaj: ");
        restaurant.adaugaProdus(new Produs(restaurant.idNouProdus(), nume, categorie, pret, gramaj));
        System.out.println("Produs adaugat");
    }

    private void comandaNoua() throws DateInvalideException {
        Comanda c = new Comanda(restaurant.idNouComanda(), LocalDateTime.now().withNano(0));
        afiseazaProduse();
        while (true) {
            try {
                int id = citesteInt("Id produs (0 pentru finalizare): ");
                if (id == 0) {
                    break;
                }
                Produs p = restaurant.getProdus(id);
                int cantitate = citesteInt("Cantitate: ");
                c.adauga(p, cantitate);
            } catch (NumberFormatException e) {
                System.out.println("Trebuie sa introduci un numar valid!");
            } catch (DateInvalideException e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }
        // o comanda goala nu se adauga, adaugaComanda arunca exceptie
        restaurant.adaugaComanda(c);
        System.out.println("Comanda " + c.getId() + " a fost adaugata, total " + String.format("%.2f", c.getTotal()) + " lei");
    }

    private void detaliiComanda() throws DateInvalideException {
        Comanda c = restaurant.getComanda(citesteInt("Id comanda: "));
        System.out.println(c);
        for (Map.Entry<Produs, Integer> e : c.getProduse().entrySet()) {
            System.out.println("  " + e.getValue() + " x " + e.getKey().getNume() + " = "
                    + String.format("%.2f", e.getValue() * e.getKey().getPret()) + " lei");
        }
    }

    private String citesteText(String mesaj) {
        System.out.print(mesaj);
        return sc.nextLine().trim();
    }

    private int citesteInt(String mesaj) {
        return Integer.parseInt(citesteText(mesaj));
    }

    private LocalDate citesteData(String mesaj) {
        return LocalDate.parse(citesteText(mesaj + " (aaaa-ll-zz): "));
    }
}
