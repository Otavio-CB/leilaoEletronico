package br.gov.sp.fatec.lp2.repository;

import br.gov.sp.fatec.lp2.entity.Veiculo;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface VeiculoRepository extends CrudRepository<Veiculo, Long> {
}
