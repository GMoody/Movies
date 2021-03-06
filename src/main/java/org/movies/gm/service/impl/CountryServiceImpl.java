package org.movies.gm.service.impl;

import org.movies.gm.domain.Country;
import org.movies.gm.repository.CountryRepository;
import org.movies.gm.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Country.
 */
@Service
@Transactional
public class CountryServiceImpl implements CountryService{

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    @Inject
    private CountryRepository countryRepository;

    /**
     * Save a country.
     *
     * @param country the entity to save
     * @return the persisted entity
     */
    public Country save(Country country) {
        log.debug("Request to save Country : {}", country);
        Optional<Country> requested = countryRepository.findByTitleIgnoreCase(country.getTitle());
        if(!requested.isPresent()) return countryRepository.save(country);
        else return requested.get();
    }

    /**
     *  Get all the countries.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Country> findAll(Pageable pageable) {
        log.debug("Request to get all Countries by page");
        Page<Country> result = countryRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one country by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Country findOne(Long id) {
        log.debug("Request to get Country : {}", id);
        Country country = countryRepository.findOne(id);
        return country;
    }

    /**
     *  Delete the  country by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Country : {}", id);
        Country country = countryRepository.findOneWithEagerRelationships(id);
        country.getMovies().forEach(m -> m.getCountries().remove(country));
        countryRepository.delete(id);
    }

    @Override
    public List<Country> findAll() {
        log.debug("Request to get all Countries");
        List<Country> result = countryRepository.findAll();
        return result;
    }
}
