package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HommeService implements IDao<Homme> {

    @Override
    public boolean create(Homme o) {
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
    public boolean update(Homme o) {
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
    public boolean delete(Homme o) {
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
    public Homme getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Homme.class, id);
        }
    }

    @Override
    public List<Homme> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Homme", Homme.class).list();
        }
    }

    // Épouses d'un homme entre deux dates
    public List<Femme> getEpousesEntreDates(int hommeId, Date d1, Date d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select m.femme from Mariage m " +
                                    "where m.homme.id = :id and m.dateDebut between :d1 and :d2", Femme.class)
                    .setParameter("id", hommeId)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .list();
        }
    }

    // API Criteria : nombre d'hommes mariés à 4 femmes entre deux dates
    public long getNombreHommesMariesQuatreFemmes(Date d1, Date d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Homme> h = cq.from(Homme.class);

            Subquery<Long> sq = cq.subquery(Long.class);
            Root<Mariage> m = sq.from(Mariage.class);
            sq.select(cb.countDistinct(m.get("femme")))
                    .where(cb.equal(m.get("homme"), h),
                            cb.between(m.<Date>get("dateDebut"), d1, d2));

            cq.select(cb.count(h)).where(cb.equal(sq, 4L));
            return session.createQuery(cq).getSingleResult();
        }
    }

    // API Criteria : liste des hommes mariés à 4 femmes entre deux dates
    public List<Homme> getHommesMariesQuatreFemmes(Date d1, Date d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Homme> cq = cb.createQuery(Homme.class);
            Root<Homme> h = cq.from(Homme.class);

            Subquery<Long> sq = cq.subquery(Long.class);
            Root<Mariage> m = sq.from(Mariage.class);
            sq.select(cb.countDistinct(m.get("femme")))
                    .where(cb.equal(m.get("homme"), h),
                            cb.between(m.<Date>get("dateDebut"), d1, d2));

            cq.select(h).where(cb.equal(sq, 4L));
            return session.createQuery(cq).getResultList();
        }
    }

    // Mariages d'un homme avec tous les détails
    public void afficherMariagesHomme(int hommeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Homme homme = session.get(Homme.class, hommeId);
            if (homme == null) {
                System.out.println("Homme introuvable : " + hommeId);
                return;
            }

            List<Mariage> mariages = session.createQuery(
                            "from Mariage m where m.homme.id = :id order by m.dateDebut", Mariage.class)
                    .setParameter("id", hommeId)
                    .list();

            List<Mariage> enCours = new ArrayList<>();
            List<Mariage> echoues = new ArrayList<>();
            for (Mariage m : mariages) {
                if (m.getDateFin() == null) enCours.add(m);
                else echoues.add(m);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Nom : " + homme.getNom() + " " + homme.getPrenom());

            System.out.println("Mariages En Cours :");
            int i = 1;
            for (Mariage m : enCours) {
                String femme = m.getFemme().getPrenom() + " " + m.getFemme().getNom();
                System.out.printf("%d. Femme : %-15s Date Début : %-12s Nbr Enfants : %d%n",
                        i++, femme, sdf.format(m.getDateDebut()), m.getNbrEnfant());
            }

            System.out.println();
            System.out.println("Mariages échoués :");
            i = 1;
            for (Mariage m : echoues) {
                String femme = m.getFemme().getPrenom() + " " + m.getFemme().getNom();
                System.out.printf("%d. Femme : %-15s Date Début : %-12s%n",
                        i++, femme, sdf.format(m.getDateDebut()));
                System.out.printf("   Date Fin : %-12s Nbr Enfants : %d%n",
                        sdf.format(m.getDateFin()), m.getNbrEnfant());
            }
        }
    }
}