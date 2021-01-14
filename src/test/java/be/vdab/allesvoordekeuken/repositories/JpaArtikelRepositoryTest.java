package be.vdab.allesvoordekeuken.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import be.vdab.allesvoordekeuken.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@DataJpaTest
@Import(JpaArtikelRepository.class)
@Sql({"/insertArtikelGroep.sql", "/insertArtikel.sql"})
public class JpaArtikelRepositoryTest
        extends AbstractTransactionalJUnit4SpringContextTests{
    private final JpaArtikelRepository repository;
    private static final String ARTIKELS = "artikels";
    private final EntityManager manager;



    JpaArtikelRepositoryTest(JpaArtikelRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    private long idVanTestFoodArtikel() {
        return super.jdbcTemplate.queryForObject(
                "select id from artikels where naam='testfood'", Long.class);
    }

    private long idVanTestNonFoodArtikel() {
        return super.jdbcTemplate.queryForObject(
                "select id from artikels where naam='testnonfood'", Long.class);
    }

    @Test
    void findFoodArtikelById() {
        var artikel = repository.findById(idVanTestFoodArtikel()).get();
        assertThat(artikel).isInstanceOf(FoodArtikel.class);
        assertThat(artikel.getNaam()).isEqualTo("testfood");
    }
    @Test
    void findNonFoodArtikelById() {
        var artikel = repository.findById(idVanTestNonFoodArtikel()).get();
        assertThat(artikel).isInstanceOf(NonFoodArtikel.class);
        assertThat(artikel.getNaam()).isEqualTo("testnonfood");
    }
    @Test
    void findOnbestaandeId() {
        assertThat(repository.findById(-1)).isNotPresent();
    }

    @Test
    void createFoodArtikel() {
        var groep = new ArtikelGroep("test");
        manager.persist(groep);
        var artikel = new FoodArtikel("testfood2",BigDecimal.ONE,BigDecimal.TEN,7,groep);
        repository.create(artikel);
        assertThat(super.countRowsInTableWhere(ARTIKELS,
                "id=" + artikel.getId())).isOne();


    }
    @Test
    void createNonFoodArtikel() {
        var groep = new ArtikelGroep("test");
        manager.persist(groep);
        var artikel =
                new NonFoodArtikel("testnonfood2", BigDecimal.ONE, BigDecimal.TEN, 30, groep);
        repository.create(artikel);
        assertThat(super.countRowsInTableWhere(ARTIKELS,
                "id=" + artikel.getId())).isOne();
    }


    @Test
    void findBijNaamContains() {
        assertThat(repository.findByNaamContains("es"))
                .hasSize(super.jdbcTemplate.queryForObject(
                        "select count(*) from artikels where naam like '%es%'", Integer.class))
                .extracting(artikel -> artikel.getNaam().toLowerCase())
                .allSatisfy(naam -> assertThat(naam).contains("es"))
                .isSorted();
    }

    @Test
    void verhoogAlleVerkoopPrijzen() {
        assertThat(repository.verhoogAlleVerkoopPrijzen(BigDecimal.TEN))
                .isEqualTo(super.countRowsInTable("artikels"));
        assertThat(super.jdbcTemplate.queryForObject(
                "select verkoopprijs from artikels where id=?", BigDecimal.class,
                idVanTestFoodArtikel())).isEqualByComparingTo("132");
    }
    @Test
    void kortingenLezen() {
        assertThat(repository.findById(idVanTestFoodArtikel()).get().getKortingen())
                .containsOnly(new Korting(1, BigDecimal.TEN));
    }

    @Test
    void artikelGroepLazyLoaded() {
        assertThat(repository.findById(idVanTestFoodArtikel()).get()
                .getArtikelGroep().getNaam()).isEqualTo("test");
    }

}
