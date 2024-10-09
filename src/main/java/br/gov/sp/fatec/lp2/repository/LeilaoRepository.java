package br.gov.sp.fatec.lp2.repository;

import br.gov.sp.fatec.lp2.entity.Leilao;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface LeilaoRepository extends JpaRepository<Leilao, Long> {
}
