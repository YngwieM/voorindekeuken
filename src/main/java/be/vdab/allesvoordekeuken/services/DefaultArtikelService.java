package be.vdab.allesvoordekeuken.services;

import be.vdab.allesvoordekeuken.exceptions.ArtikelNietGevondenException;
import be.vdab.allesvoordekeuken.repositories.ArtikelRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
@Service
@Transactional
public class DefaultArtikelService implements ArtikelService {
    public final ArtikelRepository artikelRepository;

    public DefaultArtikelService(ArtikelRepository artikelRepository) {
        this.artikelRepository = artikelRepository;
    }


    @Override
    public void verhoogVerkoopPrijs(long id, BigDecimal waarde) {
        artikelRepository.findById(id)
                .orElseThrow(() -> new ArtikelNietGevondenException())
                .verhoogVerkoopPrijs(waarde);

    }
}
