package eduard.covrig.ppoo.main;

import eduard.covrig.ppoo.console.Meniu;
import eduard.covrig.ppoo.service.Fisiere;
import eduard.covrig.ppoo.service.Restaurant;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();

        // la pornire incarcam datele din fisiere
        try {
            Fisiere.incarca(restaurant);
        } catch (IOException e) {
            System.out.println("Eroare la citirea fisierelor: " + e.getMessage());
            return;
        }
        System.out.println("Date incarcate: " + restaurant.getProduse().size() + " produse, "
                + restaurant.getComenzi().size() + " comenzi");

        new Meniu(restaurant).start();

        // la inchidere salvam tot in fisiere
        try {
            Fisiere.salveaza(restaurant);
            System.out.println("Datele au fost salvate. La revedere!");
        } catch (IOException e) {
            System.out.println("Eroare la salvarea fisierelor: " + e.getMessage());
        }
    }
}
