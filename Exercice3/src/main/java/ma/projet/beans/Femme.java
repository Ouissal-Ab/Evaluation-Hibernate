package ma.projet.beans;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NamedNativeQuery(
        name = "Femme.nbrEnfantsEntreDates",
        query = "SELECT COALESCE(SUM(m.nbrEnfant), 0) FROM mariage m " +
                "WHERE m.femme_id = :id AND m.dateDebut BETWEEN :d1 AND :d2")
@NamedQuery(
        name = "Femme.marieesAuMoinsDeuxFois",
        query = "select f from Femme f " +
                "where (select count(m) from Mariage m where m.femme = f) >= 2")
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme")
    private List<Mariage> mariages;

    public Femme() {
    }

    public Femme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }

    public List<Mariage> getMariages() { return mariages; }
    public void setMariages(List<Mariage> mariages) { this.mariages = mariages; }
}