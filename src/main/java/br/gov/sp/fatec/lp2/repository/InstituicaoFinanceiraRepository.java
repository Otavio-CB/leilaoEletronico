package br.gov.sp.fatec.lp2.repository;

import br.gov.sp.fatec.lp2.entity.InstituicaoFinanceira;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface InstituicaoFinanceiraRepository extends CrudRepository<InstituicaoFinanceira, Long> {

}
