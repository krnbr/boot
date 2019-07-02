package in.n2w.boot.repositories;

import in.n2w.boot.entities.UrlPermissions;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Karanbir Singh on 7/1/2019.
 **/
public interface UrlPermissionsRepository extends JpaRepository<UrlPermissions, Long> {
}
