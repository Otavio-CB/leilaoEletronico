package br.gov.sp.fatec.lp2.repository;

import br.gov.sp.fatec.lp2.entity.Leilao;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface LeilaoRepository extends JpaRepository<Leilao, Long> {
    @Query("SELECT l FROM Leilao l ORDER BY l.dataOcorrencia ASC")
    List<Leilao> findAllLeiloesOrdenadosPorData();
}
