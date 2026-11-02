package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;

public class TestApplication {

    public static void main(String[] args) throws Exception {
        CategorieService cs = new CategorieService();
        ProduitService ps = new ProduitService();
        CommandeService cms = new CommandeService();
        LigneCommandeService ls = new LigneCommandeService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        Categorie c1 = new Categorie("INF", "Informatique");
        Categorie c2 = new Categorie("ACC", "Accessoires");
        cs.create(c1);
        cs.create(c2);

        Produit p1 = new Produit("ES12", 120, c1);
        Produit p2 = new Produit("ZR85", 100, c1);
        Produit p3 = new Produit("EE85", 200, c2);
        Produit p4 = new Produit("AB10", 50, c2);
        ps.create(p1);
        ps.create(p2);
        ps.create(p3);
        ps.create(p4);

        Commande cmd1 = new Commande(sdf.parse("2013-03-14"));
        Commande cmd2 = new Commande(sdf.parse("2013-06-20"));
        cms.create(cmd1);
        cms.create(cmd2);

        ls.create(new LigneCommandeProduit(p1, cmd1, 7));
        ls.create(new LigneCommandeProduit(p2, cmd1, 14));
        ls.create(new LigneCommandeProduit(p3, cmd1, 5));
        ls.create(new LigneCommandeProduit(p4, cmd2, 3));


        System.out.println("\n=== Produits de la catégorie " + c1.getLibelle() + " ===");
        for (Produit p : ps.findByCategorie(c1)) {
            System.out.println(p);
        }


        System.out.println("\n=== Produits commandés entre 2013-01-01 et 2013-04-30 ===");
        for (Produit p : ps.findProduitsCommandesEntreDates(
                sdf.parse("2013-01-01"), sdf.parse("2013-04-30"))) {
            System.out.println(p);
        }


        System.out.println("\n=== Détail d'une commande ===");
        ps.afficherProduitsParCommande(cmd1.getId());


        System.out.println("\n=== Produits dont le prix > 100 DH ===");
        for (Produit p : ps.findPrixSuperieur(100)) {
            System.out.println(p);
        }

        HibernateUtil.getSessionFactory().close();
    }
}