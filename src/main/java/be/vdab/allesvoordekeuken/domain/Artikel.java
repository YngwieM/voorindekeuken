package be.vdab.allesvoordekeuken.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "artikels")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "soort")
public abstract class Artikel {
    @Id
    @Column(columnDefinition = "binary(16)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;
    private BigDecimal aankoopprijs;
    private BigDecimal verkoopprijs;
    @ElementCollection @OrderBy("vanafAantal")
    @CollectionTable(name = "kortingen",
            joinColumns = @JoinColumn(name = "artikelid"))
    private Set<Korting> kortingen;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artikelgroepid")
    private ArtikelGroep artikelGroep;



    public Artikel(String naam, BigDecimal aankoopprijs, BigDecimal verkoopprijs,
                   ArtikelGroep artikelGroep) {
        this.naam = naam;
        this.aankoopprijs = aankoopprijs;
        this.verkoopprijs = verkoopprijs;
        this.kortingen = new LinkedHashSet<>();
        setArtikelGroep(artikelGroep);
    }

    protected Artikel() {

    }
    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public BigDecimal getAankoopprijs() {
        return aankoopprijs;
    }

    public BigDecimal getVerkoopprijs() {
        return verkoopprijs;
    }

    public Set<Korting> getKortingen() {
        return Collections.unmodifiableSet(kortingen);
    }
    public void verhoogVerkoopPrijs(BigDecimal bedrag) {
        if (bedrag.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
        verkoopprijs = verkoopprijs.add(bedrag);
    }

    public ArtikelGroep getArtikelGroep() {
        return artikelGroep;
    }
    public void setArtikelGroep(ArtikelGroep artikelGroep) {
        if (!artikelGroep.getArtikels().contains(this)) {
            artikelGroep.add(this);
        }
        this.artikelGroep = artikelGroep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artikel)) return false;
        Artikel artikel = (Artikel) o;
        return id == artikel.id && Objects.equals(naam, artikel.naam) && Objects.equals(aankoopprijs, artikel.aankoopprijs) && Objects.equals(verkoopprijs, artikel.verkoopprijs) && Objects.equals(kortingen, artikel.kortingen) && Objects.equals(artikelGroep, artikel.artikelGroep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, naam, aankoopprijs, verkoopprijs, kortingen, artikelGroep);
    }
}
