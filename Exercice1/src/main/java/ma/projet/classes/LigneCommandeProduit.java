package ma.projet.classes;

import javax.persistence.*;

@Entity
public class LigneCommandeProduit {

    @EmbeddedId
    private LigneCommandeProduitPK id = new LigneCommandeProduitPK();

    private int quantite;

    @ManyToOne
    @MapsId("produit")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne
    @MapsId("commande")
    @JoinColumn(name = "commande_id")
    private Commande commande;

    public LigneCommandeProduit() {
    }

    public LigneCommandeProduit(Produit produit, Commande commande, int quantite) {
        this.id = new LigneCommandeProduitPK(produit.getId(), commande.getId());
        this.produit = produit;
        this.commande = commande;
        this.quantite = quantite;
    }

    public LigneCommandeProduitPK getId() { return id; }
    public void setId(LigneCommandeProduitPK id) { this.id = id; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public Commande getCommande() { return commande; }
    public void setCommande(Commande commande) { this.commande = commande; }
}