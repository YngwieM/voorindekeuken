package be.vdab.allesvoordekeuken.repositories;

import be.vdab.allesvoordekeuken.domain.Artikel;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
class JpaArtikelRepository implements ArtikelRepository {
    private final EntityManager manager;

    JpaArtikelRepository(EntityManager manager) {
        this.manager = manager;
    }


    @Override
    public Optional<Artikel> findById(long id) {
        return Optional.ofNullable(manager.find(Artikel.class, id));
    }

    @Override
    public void create(Artikel artikel) {
        manager.persist(artikel);
    }
    @Override
    public List<Artikel> findByNaamContains(String woord) {
        return manager.createNamedQuery("Artikel.findByNaamContains", Artikel.class)
                .setParameter("zoals", '%' + woord + '%')
                .setHint("javax.persistence.loadgraph",
                        manager.createEntityGraph(Artikel.MET_ARTIKELGROEP))
                .getResultList();
    }

    @Override
    public int  verhoogAlleVerkoopPrijzen(BigDecimal percentage) {
        var factor = BigDecimal.ONE.add(percentage.divide(BigDecimal.valueOf(100)));
        return manager.createNamedQuery("Artikel.algemenePrijsVerhoging")
                .setParameter("factor", factor)
                .executeUpdate();

    }
}