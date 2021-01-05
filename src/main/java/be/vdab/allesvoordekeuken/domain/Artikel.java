package be.vdab.allesvoordekeuken.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "artikels")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "soort")
public abstract class Artikel {
    @Id
    @Column(columnDefinition = "binary(16)")
    private long id;
    private String naam;
    private BigDecimal aankoopprijs;
    private BigDecimal verkoopprijs;

    public Artikel(String naam, BigDecimal aankoopprijs, BigDecimal verkoopprijs) {
        this.naam = naam;
        this.aankoopprijs = aankoopprijs;
        this.verkoopprijs = verkoopprijs;
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
    public void verhoogVerkoopPrijs(BigDecimal bedrag) {
        if (bedrag.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
        verkoopprijs = verkoopprijs.add(bedrag);
    }
}
