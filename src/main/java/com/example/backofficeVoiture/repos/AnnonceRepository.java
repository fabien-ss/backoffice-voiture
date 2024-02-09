package com.example.backofficeVoiture.repos;

import com.example.backofficeVoiture.domain.Annonce;
import com.example.backofficeVoiture.domain.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, String> {
    @Query(value = "SELECT NEXTVAL('seq_annonce')", nativeQuery = true)
    Long getNextSequenceValue();
    List<Annonce> findAnnonceByUtilisateur(Utilisateur utilisateur);
    Annonce findAnnonceByIdAnnonceAndUtilisateur(String idAnnonce, Utilisateur utilisateur);
    List<Annonce> findAllByHistoriqueModificationAnnonceUtilisateurs(Utilisateur utilisateur);

    boolean existsByIdAnnonceIgnoreCase(String idAnnonce);

    List<Annonce> findAnnonceByEtat(Integer etat);
    Annonce findAnnonceByIdAnnonce(String idAnnonce);
    @Query(value = "with part1 as " +
            "(select * from annonce  order by id_annonce limit cast(:a as BIGINT) offset cast(:b as BIGINT)) " +
            "select * from part1 where part1.etat = :etat limit cast(:a as BIGINT)- cast(:b as Bigint) ", nativeQuery = true)
    List<Annonce> findAnnonceBetween(@Param("a") String a, @Param("b") String b, @Param("etat") String etat);

    @Query(value = " :sql ", nativeQuery = true)
    List<Annonce> executeListAnnonceSql(@Param(value = "sql") String sql);

    @Query(value = "WITH valeur_to_table AS (SELECT regexp_split_to_table(:sqlValues, ',') AS valeur) SELECT a.id_annonce, a.annee, a.kilometrage, a.date_annonce, a.description, a.etat, a.id_utilisateur, a.prix, COUNT(dm.id_annonce) nombre FROM details_modele dm JOIN valeur_to_table vtb ON dm.value=vtb.valeur JOIN annonce a ON dm.id_annonce = a.id_annonce WHERE a.prix::INTEGER BETWEEN :prixInf AND :prixSup AND a.annee::INTEGER BETWEEN :anneeInf AND :anneeSup GROUP BY a.id_annonce ORDER BY nombre", nativeQuery = true)
    List<Annonce> findAnnoncesBySearchParameters(@Param("sqlValues") String sqlValues, @Param("prixInf") Integer prixInf, @Param("prixSup") Integer prixSup, @Param("anneeInf") Integer anneeInf, @Param("anneeSup") Integer anneeSup);

    @Query(value = "with part1 as (select count(id_modele) as nombre, cast(id_modele as varchar) from axe_possible_values where id_value in " +
            "                                  (:sql)" +
            "    group by id_modele order by nombre desc)" +
            "   select distinct a.* from details_modele dm join part1 on part1.id_modele=dm.id_modele" +
            "   join annonce a on dm.id_annonce = a.id_annonce ", nativeQuery = true)
    List<Annonce> findModeleByIdValue(@Param("sql") String sql);
}
