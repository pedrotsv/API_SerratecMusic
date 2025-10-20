package org.serratec.serramusic.repository;

import org.serratec.serramusic.domain.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long>{

}
