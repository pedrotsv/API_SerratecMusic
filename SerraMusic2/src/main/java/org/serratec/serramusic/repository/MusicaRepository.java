package org.serratec.serramusic.repository;

import org.serratec.serramusic.domain.Musica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicaRepository extends JpaRepository<Musica, Long>{

}
