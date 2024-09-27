package br.gov.sp.fatec.lp2.repository;

import br.gov.sp.fatec.lp2.entity.Cliente;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}
