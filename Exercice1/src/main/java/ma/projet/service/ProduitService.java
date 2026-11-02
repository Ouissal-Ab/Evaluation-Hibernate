package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProduitService implements IDao<Produit> {

    @Override
    public boolean create(Produit o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean update(Produit o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean delete(Produit o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Produit getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Produit.class, id);
        }
    }

    @Override
    public List<Produit> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Produit", Produit.class).list();
        }
    }


    public List<Produit> findByCategorie(Categorie categorie) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Produit p where p.categorie.id = :id", Produit.class)
                    .setParameter("id", categorie.getId())
                    .list();
        }
    }


    public List<Produit> findProduitsCommandesEntreDates(Date d1, Date d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select distinct l.produit from LigneCommandeProduit l " +
                                    "where l.commande.date between :d1 and :d2", Produit.class)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .list();
        }
    }


    public void afficherProduitsParCommande(int commandeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Commande commande = session.get(Commande.class, commandeId);
            if (commande == null) {
                System.out.println("Commande introuvable : " + commandeId);
                return;
            }

            List<LigneCommandeProduit> lignes = session.createQuery(
                            "from LigneCommandeProduit l where l.commande.id = :id",
                            LigneCommandeProduit.class)
                    .setParameter("id", commandeId)
                    .list();

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            System.out.println("Commande : " + commande.getId()
                    + "     Date : " + sdf.format(commande.getDate()));
            System.out.println("Liste des produits :");
            System.out.printf("%-12s%-8s%s%n", "Référence", "Prix", "Quantité");
            for (LigneCommandeProduit l : lignes) {
                System.out.printf("%-12s%-8s%d%n",
                        l.getProduit().getReference(),
                        String.format("%.0f DH", l.getProduit().getPrix()),
                        l.getQuantite());
            }
        }
    }


    public List<Produit> findPrixSuperieur(float prix) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Produit.prixSuperieur", Produit.class)
                    .setParameter("prix", prix)
                    .list();
        }
    }
}