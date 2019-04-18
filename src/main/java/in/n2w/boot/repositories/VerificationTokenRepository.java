package in.n2w.boot.repositories;

import in.n2w.boot.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Karanbir Singh on 4/17/2019.
 **/
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

}