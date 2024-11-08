package br.gov.sp.fatec.lp2.repository;

import br.gov.sp.fatec.lp2.entity.Lance;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface LanceRepository extends JpaRepository<Lance, Long> {
    List<Lance> findByDispositivoId(Long dispositivoId);
    List<Lance> findByVeiculoId(Long veiculoId);
}