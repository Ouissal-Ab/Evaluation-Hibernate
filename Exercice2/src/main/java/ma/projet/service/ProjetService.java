package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjetService implements IDao<Projet> {

    @Override
    public boolean create(Projet o) {
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
    public boolean update(Projet o) {
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
    public boolean delete(Projet o) {
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
    public Projet getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Projet.class, id);
        }
    }

    @Override
    public List<Projet> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Projet", Projet.class).list();
        }
    }


    public List<Tache> getTachesPlanifiees(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Tache t where t.projet.id = :id", Tache.class)
                    .setParameter("id", projetId)
                    .list();
        }
    }


    public void afficherTachesRealisees(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Projet projet = session.get(Projet.class, projetId);
            if (projet == null) {
                System.out.println("Projet introuvable : " + projetId);
                return;
            }

            List<EmployeTache> liste = session.createQuery(
                            "from EmployeTache et where et.tache.projet.id = :id", EmployeTache.class)
                    .setParameter("id", projetId)
                    .list();

            SimpleDateFormat sdfLong = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            SimpleDateFormat sdfCourt = new SimpleDateFormat("dd/MM/yyyy");

            System.out.println("Projet : " + projet.getId()
                    + "      Nom : " + projet.getNom()
                    + "     Date début : " + sdfLong.format(projet.getDateDebut()));
            System.out.println("Liste des tâches:");
            System.out.printf("%-4s%-15s%-20s%s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
            for (EmployeTache et : liste) {
                System.out.printf("%-4d%-15s%-20s%s%n",
                        et.getTache().getId(),
                        et.getTache().getNom(),
                        sdfCourt.format(et.getDateDebutReelle()),
                        sdfCourt.format(et.getDateFinReelle()));
            }
        }
    }
}