package br.gov.sp.fatec.lp2.repository;

import br.gov.sp.fatec.lp2.entity.Lance;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface LanceRepository extends JpaRepository<Lance, Long> {
}