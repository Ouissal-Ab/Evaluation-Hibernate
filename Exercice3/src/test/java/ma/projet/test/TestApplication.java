package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;

public class TestApplication {

    public static void main(String[] args) throws Exception {
        HommeService hs = new HommeService();
        FemmeService fs = new FemmeService();
        MariageService ms = new MariageService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // ---------- Création de 10 femmes ----------
        Femme f1  = new Femme("RAMI",    "SALIMA", "0600000001", "Marrakech", sdf.parse("1970-05-12"));
        Femme f2  = new Femme("ALI",     "AMAL",   "0600000002", "Rabat",     sdf.parse("1975-08-20"));
        Femme f3  = new Femme("ALAOUI",  "WAFA",   "0600000003", "Fès",       sdf.parse("1978-03-15"));
        Femme f4  = new Femme("ALAMI",   "KARIMA", "0600000004", "Casablanca",sdf.parse("1965-11-02"));
        Femme f5  = new Femme("BENNANI", "NADIA",  "0600000005", "Tanger",    sdf.parse("1980-01-25"));
        Femme f6  = new Femme("TAZI",    "HIND",   "0600000006", "Agadir",    sdf.parse("1982-07-30"));
        Femme f7  = new Femme("IDRISSI", "SOUAD",  "0600000007", "Meknès",    sdf.parse("1968-04-18"));
        Femme f8  = new Femme("FASSI",   "LAILA",  "0600000008", "Oujda",     sdf.parse("1985-12-05"));
        Femme f9  = new Femme("CHRAIBI", "MERYEM", "0600000009", "Safi",      sdf.parse("1990-09-09"));
        Femme f10 = new Femme("KETTANI", "ZINEB",  "0600000010", "Tétouan",   sdf.parse("1988-02-14"));
        Femme[] femmes = {f1, f2, f3, f4, f5, f6, f7, f8, f9, f10};
        for (Femme f : femmes) fs.create(f);


        Homme h1 = new Homme("SAFI",    "SAID",    "0700000001", "Marrakech", sdf.parse("1960-06-10"));
        Homme h2 = new Homme("BERRADA", "OMAR",    "0700000002", "Rabat",     sdf.parse("1965-02-22"));
        Homme h3 = new Homme("SEKKAT",  "YOUSSEF", "0700000003", "Fès",       sdf.parse("1970-10-01"));
        Homme h4 = new Homme("LAHLOU",  "KARIM",   "0700000004", "Casablanca",sdf.parse("1978-04-14"));
        Homme h5 = new Homme("ZIANI",   "HAMZA",   "0700000005", "Tanger",    sdf.parse("1985-08-08"));
        Homme[] hommes = {h1, h2, h3, h4, h5};
        for (Homme h : hommes) hs.create(h);


        ms.create(new Mariage(h1, f4, sdf.parse("1989-09-03"), sdf.parse("1990-09-03"), 0));
        ms.create(new Mariage(h1, f1, sdf.parse("1990-09-03"), null, 4));
        ms.create(new Mariage(h1, f2, sdf.parse("1995-09-03"), null, 2));
        ms.create(new Mariage(h1, f3, sdf.parse("2000-11-04"), null, 3));

        ms.create(new Mariage(h2, f4, sdf.parse("1991-05-10"), null, 2));   // KARIMA : 2e mariage
        ms.create(new Mariage(h3, f5, sdf.parse("2002-06-15"), sdf.parse("2008-01-01"), 1));
        ms.create(new Mariage(h4, f5, sdf.parse("2010-03-20"), null, 2));   // NADIA : 2e mariage
        ms.create(new Mariage(h4, f6, sdf.parse("2012-07-07"), null, 1));
        ms.create(new Mariage(h5, f9, sdf.parse("2015-04-04"), null, 0));


        System.out.println("\n=== Liste des femmes ===");
        for (Femme f : fs.getAll()) {
            System.out.println(f);
        }


        System.out.println("\n=== Femme la plus âgée ===");
        System.out.println(fs.getFemmePlusAgee());


        System.out.println("\n=== Épouses de " + h1.getNom() + " entre 1989 et 2001 ===");
        for (Femme f : hs.getEpousesEntreDates(h1.getId(), sdf.parse("1989-01-01"), sdf.parse("2001-12-31"))) {
            System.out.println(f);
        }


        System.out.println("\n=== Nombre d'enfants de " + f1.getPrenom() + " entre 1990 et 2020 ===");
        System.out.println(fs.getNombreEnfantsEntreDates(f1.getId(),
                sdf.parse("1990-01-01"), sdf.parse("2020-12-31")));


        System.out.println("\n=== Femmes mariées au moins deux fois ===");
        for (Femme f : fs.getFemmesMarieesAuMoinsDeuxFois()) {
            System.out.println(f);
        }


        System.out.println("\n=== Hommes mariés à 4 femmes entre 1989 et 2001 ===");
        System.out.println("Nombre : " + hs.getNombreHommesMariesQuatreFemmes(
                sdf.parse("1989-01-01"), sdf.parse("2001-12-31")));
        for (Homme h : hs.getHommesMariesQuatreFemmes(sdf.parse("1989-01-01"), sdf.parse("2001-12-31"))) {
            System.out.println(h);
        }


        System.out.println("\n=== Détail des mariages ===");
        hs.afficherMariagesHomme(h1.getId());

        HibernateUtil.getSessionFactory().close();
    }
}