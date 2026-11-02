package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;

public class TestApplication {

    public static void main(String[] args) throws Exception {
        EmployeService es = new EmployeService();
        ProjetService ps = new ProjetService();
        TacheService ts = new TacheService();
        EmployeTacheService ets = new EmployeTacheService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        Employe e1 = new Employe("Alami", "Ahmed", "0611111111");
        Employe e2 = new Employe("Bennani", "Sara", "0622222222");
        es.create(e1);
        es.create(e2);

        Projet p1 = new Projet("Gestion de stock", sdf.parse("2013-01-14"), sdf.parse("2013-06-30"), e1);
        Projet p2 = new Projet("Site web", sdf.parse("2013-05-01"), sdf.parse("2013-09-30"), e2);
        ps.create(p1);
        ps.create(p2);

        Tache t1 = new Tache("Analyse", sdf.parse("2013-02-01"), sdf.parse("2013-02-28"), 1500, p1);
        Tache t2 = new Tache("Conception", sdf.parse("2013-03-01"), sdf.parse("2013-03-31"), 800, p1);
        Tache t3 = new Tache("Développement", sdf.parse("2013-04-01"), sdf.parse("2013-04-30"), 3000, p1);
        Tache t4 = new Tache("Maquettage", sdf.parse("2013-05-05"), sdf.parse("2013-05-20"), 600, p2);
        ts.create(t1);
        ts.create(t2);
        ts.create(t3);
        ts.create(t4);

        ets.create(new EmployeTache(e1, t1, sdf.parse("2013-02-10"), sdf.parse("2013-02-20")));
        ets.create(new EmployeTache(e2, t2, sdf.parse("2013-03-10"), sdf.parse("2013-03-15")));
        ets.create(new EmployeTache(e1, t3, sdf.parse("2013-04-10"), sdf.parse("2013-04-25")));
        ets.create(new EmployeTache(e2, t4, sdf.parse("2013-05-06"), sdf.parse("2013-05-18")));


        System.out.println("\n=== Tâches réalisées par " + e1.getNom() + " ===");
        for (Tache t : es.getTachesRealiseesParEmploye(e1.getId())) {
            System.out.println(t);
        }

        System.out.println("\n=== Projets gérés par " + e1.getNom() + " ===");
        for (Projet p : es.getProjetsGeresParEmploye(e1.getId())) {
            System.out.println(p);
        }


        System.out.println("\n=== Tâches planifiées du projet " + p1.getNom() + " ===");
        for (Tache t : ps.getTachesPlanifiees(p1.getId())) {
            System.out.println(t);
        }

        System.out.println("\n=== Tâches réalisées avec dates réelles ===");
        ps.afficherTachesRealisees(p1.getId());


        System.out.println("\n=== Tâches dont le prix > 1000 DH ===");
        for (Tache t : ts.getTachesPrixSuperieur(1000)) {
            System.out.println(t);
        }

        System.out.println("\n=== Tâches réalisées entre 2013-02-01 et 2013-03-31 ===");
        for (Tache t : ts.getTachesRealiseesEntreDates(sdf.parse("2013-02-01"), sdf.parse("2013-03-31"))) {
            System.out.println(t);
        }

        HibernateUtil.getSessionFactory().close();
    }
}