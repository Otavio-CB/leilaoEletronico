package br.gov.sp.fatec.lp2.repository;

import br.gov.sp.fatec.lp2.entity.Dispositivo;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {
    @Query("SELECT d FROM Dispositivo d LEFT JOIN FETCH d.lances l WHERE d.leilao.id = :leilaoId")
    List<Dispositivo> findByLeilaoIdWithLances(Long leilaoId);
}