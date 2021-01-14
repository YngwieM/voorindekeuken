package be.vdab.allesvoordekeuken.services;
import be.vdab.allesvoordekeuken.domain.Artikel;
import be.vdab.allesvoordekeuken.domain.ArtikelGroep;
import be.vdab.allesvoordekeuken.domain.FoodArtikel;
import be.vdab.allesvoordekeuken.exceptions.ArtikelNietGevondenException;
import be.vdab.allesvoordekeuken.repositories.ArtikelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
 class DefaultArtikelServicetest {
    private DefaultArtikelService service;
    @Mock
    private ArtikelRepository repository;
    private Artikel artikel;
    private ArtikelGroep artikelGroep;


    @BeforeEach
    void beforeEach() {
        artikelGroep = new ArtikelGroep("test");
        artikel = new FoodArtikel("test", BigDecimal.ONE, BigDecimal.TEN, 1,artikelGroep);
        service = new DefaultArtikelService(repository);
    }

    @Test
    void verhoogVerkoopPrijs() {
        when(repository.findById(1)).thenReturn(Optional.of(artikel));
        service.verhoogVerkoopPrijs(1,BigDecimal.ONE);
        assertThat(artikel.getVerkoopprijs()).isEqualByComparingTo("11");
        verify(repository).findById(1);
    }

    @Test
    void opslagVoorOnbestaandeDocent() {
        assertThatExceptionOfType(ArtikelNietGevondenException.class)
                .isThrownBy(()->service.verhoogVerkoopPrijs(-1, BigDecimal.TEN));
        verify(repository).findById(-1);
    }

}
