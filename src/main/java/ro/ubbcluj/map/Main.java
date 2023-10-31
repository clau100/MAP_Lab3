package ro.ubbcluj.map;

import ro.ubbcluj.map.domain.Prietenie;
import ro.ubbcluj.map.domain.Tuple;
import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.domain.validators.PrietenieValidator;
import ro.ubbcluj.map.domain.validators.UtilizatorValidator;
import ro.ubbcluj.map.domain.validators.ValidationException;
import ro.ubbcluj.map.repository.InMemoryRepository;
import ro.ubbcluj.map.service.PrietenieService;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {

        Utilizator u1 = new Utilizator("u1FirstName", "u1LastName");
        u1.setId(1L);
        Utilizator u2 = new Utilizator("u2FirstName", "u2LastName");
        u2.setId(2L);
        Utilizator u3 = new Utilizator("u3FirstName", "u3LastName");
        u3.setId(3L);
        Utilizator u4 = new Utilizator("u4FirstName", "u4LastName");
        u4.setId(4L);
        Utilizator u5 = new Utilizator("u5FirstName", "u5LastName");
        u5.setId(5L);
        Utilizator u6 = new Utilizator("u6FirstName", "u6LastName");
        u6.setId(6L);
        Utilizator u7 = new Utilizator("u7FirstName", "u7LastName");
        u7.setId(7L);
        Utilizator u8 = new Utilizator("u8FirstName", "u8LastName");
        u8.setId(8L);
        Utilizator u9 = new Utilizator("u9FirstName", "u9LastName");
        u9.setId(9L);

        InMemoryRepository<Long, Utilizator> repo = new InMemoryRepository<>(new UtilizatorValidator());
        repo.save(u1);
        repo.save(u2);
        repo.save(u3);
        repo.save(u4);
        repo.save(u5);
        repo.save(u6);
        repo.save(u7);
        repo.save(u8);
        repo.save(u9);

        PrietenieService prieteni = new PrietenieService(new PrietenieValidator());
        // 1-2 3-4-5 6-7-9-8
        prieteni.save(new Prietenie(u1.getId(), u2.getId()));

        prieteni.save(new Prietenie(u3.getId(), u4.getId()));
        prieteni.save(new Prietenie(u4.getId(), u5.getId()));

        prieteni.save(new Prietenie(u6.getId(), u7.getId()));
        prieteni.save(new Prietenie(u7.getId(), u9.getId()));
        prieteni.save(new Prietenie(u9.getId(), u8.getId()));

        run(repo, prieteni);
    }

    public static void run(InMemoryRepository<Long, Utilizator> utilizatori, PrietenieService prieteni) {
        Scanner sc = new Scanner(System.in);
        boolean iesire = false;

        System.out.println("Bine ati venit in aplicatie!");

        while (!iesire) {
            afisareMeniuPrincipal();
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    afisareUtilizatori(utilizatori);
                    break;
                case 2:
                    afisarePrietenii(prieteni);
                    break;
                case 3:
                    adaugaUtilizator(utilizatori);
                    break;
                case 4:
                    stergeUtilizator(utilizatori, prieteni);
                    break;
                case 5:
                    adaugaPrietenie(utilizatori, prieteni);
                    break;
                case 6:
                    stergePrietenie(prieteni);
                    break;
                case 7:
                    afiseazaCeaMaiSociabilaComunitate(utilizatori, prieteni);
                    break;
                case 8:
                    afiseazaUtilizatoriDupaNrPrieteni(utilizatori, prieteni);
                    break;
                case 9:
                    afisareNrComunitati(prieteni);
                    break;
                case 0:
                    iesire = true;
                    break;
                default:
                    System.out.println("Optiunea este nevalida!");
                    break;
            }
        }
    }

    private static void afiseazaUtilizatoriDupaNrPrieteni(InMemoryRepository<Long, Utilizator> utilizatori, PrietenieService prieteni) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Dati numarul de prieteni dupa care se cauta: ");
        int n = sc.nextInt();
        AtomicBoolean found = new AtomicBoolean(false);
        Map<Long, List<Long>> prietenii = prieteni.getPrieteni();
        prietenii.forEach((user, friendList) -> {
            if (friendList.size() >= n) {
                if (utilizatori.findOne(user).isPresent()) {
                    found.set(true);
                    Utilizator util = utilizatori.findOne(user).get();
                    System.out.println(util + ": " + friendList.size());
                }
            }
        });
        if (!found.get()) {
            System.out.println("Niciun utilizator nu are cel putin " + n + " prietenii");
        }
    }

    private static void afiseazaCeaMaiSociabilaComunitate(InMemoryRepository<Long, Utilizator> utilizatori, PrietenieService prieteni) {
        for (Long id : prieteni.mostSociableCommunity()) {
            System.out.println(utilizatori.findOne(id));
        }
    }

    private static void stergePrietenie(PrietenieService prieteni) {
        afisarePrietenii(prieteni);
        Scanner sc = new Scanner(System.in);
        System.out.print("Dati id primului prieten: ");
        Long id1 = sc.nextLong();

        System.out.print("Dati id celui de-al doilea prieten: ");
        Long id2 = sc.nextLong();

        if (prieteni.findOne(new Tuple<>(id1, id2)).isEmpty()) {
            System.out.println("Nu a fost gasita aceasta prietenie!");
            return;
        }
        prieteni.delete(new Tuple<>(id1, id2));
    }

    private static void adaugaPrietenie(InMemoryRepository<Long, Utilizator> utilizatori, PrietenieService prieteni) {
        afisareUtilizatori(utilizatori);
        Scanner sc = new Scanner(System.in);
        System.out.print("Dati id primului utilizator: ");
        Long id1 = sc.nextLong();
        if (utilizatori.findOne(id1).isEmpty()) {
            System.out.println("Nu a fost gasit utilizatorul!");
            return;
        }
        System.out.print("Dati id celui de-al doilea utilizator: ");
        Long id2 = sc.nextLong();
        if (utilizatori.findOne(id2).isEmpty()) {
            System.out.println("Nu a fost gasit utilizatorul!");
            return;
        }

        prieteni.save(new Prietenie(id1, id2));
    }

    private static void stergeUtilizator(InMemoryRepository<Long, Utilizator> utilizatori, PrietenieService prieteni) {
        afisareUtilizatori(utilizatori);
        Scanner sc = new Scanner(System.in);
        System.out.print("Dati id care trebuie sters: ");
        Long id = sc.nextLong();
        Optional<Utilizator> u = utilizatori.delete(id);
        if (u.isEmpty()) {
            System.out.println("Utilizatorul cu id cerut nu a fost gasit!");
            return;
        }
        LinkedList<Prietenie> deSters = new LinkedList<>();
        for(Prietenie p : prieteni.findAll()){
            if(p.getId().getLeft() == u.get().getId() || p.getId().getRight() == u.get().getId()){
                deSters.add(p);
            }
        }
        for(Prietenie p : deSters){
            prieteni.delete(p.getId());
        }
        System.out.println("Utilizatorul a fost sters cu succes!");
    }

    private static void adaugaUtilizator(InMemoryRepository<Long, Utilizator> utilizatori) {
        Utilizator u = citesteUtilizator();
        try {
            utilizatori.save(u);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static Utilizator citesteUtilizator() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Dati id utilizator: ");
        Long id = sc.nextLong();
        sc.nextLine();
        System.out.println("Dati prenumele utilizator: ");
        String firstName = sc.nextLine();

        System.out.println("Dati numele utilizator: ");
        String lastName = sc.nextLine();
        Utilizator u = new Utilizator(firstName, lastName);
        u.setId(id);
        return u;
    }

    private static void afisareNrComunitati(PrietenieService prieteni) {
        System.out.println("Numarul de comunitati este: " + prieteni.numarComunitati());
    }

    private static void afisareUtilizatori(InMemoryRepository<Long, Utilizator> utilizatori) {
        for (Utilizator i : utilizatori.findAll()) {
            System.out.println(i);
        }
    }

    private static void afisarePrietenii(PrietenieService prieteni) {
        for (Prietenie i : prieteni.findAll()) {
            System.out.println(i);
        }
    }

    private static void afisareMeniuPrincipal() {
        System.out.println("Meniu principal:");
        System.out.println("1. Afisare utilizatori");
        System.out.println("2. Afisare prietenii");
        System.out.println("3. Adauga utilizator");
        System.out.println("4. Sterge utilizator");
        System.out.println("5. Adauga prietenie");
        System.out.println("6. Sterge prietenie");
        System.out.println("7. Afiseaza cea mai sociabila comunitate");
        System.out.println("8. Gaseste utilizatorii cu cel putin N prietenii");
        System.out.println("9. Afisare numar comunitati");
        System.out.println("0. Iesire");
        System.out.print("Optiunea aleasa de dvs. este: ");
    }

}
