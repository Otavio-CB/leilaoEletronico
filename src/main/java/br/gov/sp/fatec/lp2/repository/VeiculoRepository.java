package br.gov.sp.fatec.lp2.repository;

import br.gov.sp.fatec.lp2.entity.Veiculo;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    @Query("SELECT v FROM Veiculo v LEFT JOIN FETCH v.lances l WHERE v.leilao.id = :leilaoId")
    List<Veiculo> findByLeilaoIdWithLances(Long leilaoId);
}
