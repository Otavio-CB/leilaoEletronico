package br.gov.sp.fatec.lp2.repository;

import br.gov.sp.fatec.lp2.entity.Dispositivo;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface DispositivoRepository extends CrudRepository<Dispositivo, Long> {
}
