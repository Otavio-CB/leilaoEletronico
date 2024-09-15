package br.gov.sp.fatec.lp2;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface VeiculoRepository extends CrudRepository<Veiculo, Long> {
}
