package be.vdab.allesvoordekeuken.domain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class ArtikelTest {
    private final static BigDecimal WEDDE = BigDecimal.valueOf(200);
    private Artikel artikel;

    @BeforeEach
    void beforeEach() {
        artikel = new FoodArtikel("test", BigDecimal.ONE, BigDecimal.TEN, 1);
    }
    @Test
    void verhoogVerkoopPrijs() {
        artikel.verhoogVerkoopPrijs(BigDecimal.ONE);
        assertThat(artikel.getVerkoopprijs()).isEqualByComparingTo(new BigDecimal(11));
    }
    @Test
    void verhoogVerkoopPrijsMetNullMislukt() {
        assertThatNullPointerException().isThrownBy(() -> artikel.verhoogVerkoopPrijs(null));
    }
    @Test
    void verhoogVerkoopPrijsMet0Mislukt() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> artikel.verhoogVerkoopPrijs(BigDecimal.ZERO));
    }
    @Test
    void  verhoogVerkoopPrijsMetMetNegatieveWaardeMislukt() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> artikel.verhoogVerkoopPrijs(BigDecimal.valueOf(-1)));
    }
}
