package be.drissamri.repository;

import be.drissamri.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface LinkRepository extends JpaRepository<LinkEntity, Long> {

  public LinkEntity findByHash(String hash);

  public LinkEntity findByUrl(String url);

}
