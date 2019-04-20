package in.n2w.boot.repositories;

import in.n2w.boot.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Karanbir Singh on 4/19/2019.
 **/
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

}