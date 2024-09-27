package br.gov.sp.fatec.lp2.repository;

import br.gov.sp.fatec.lp2.entity.Leilao;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface LeilaoRepository extends CrudRepository<Leilao, Long> {
}
