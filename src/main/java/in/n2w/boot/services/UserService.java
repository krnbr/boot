package in.n2w.boot.services;

import in.n2w.boot.entities.User;
import in.n2w.boot.entities.VerificationToken;
import in.n2w.boot.exceptions.EmailExistsException;
import in.n2w.boot.repositories.UserRepository;
import in.n2w.boot.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Karanbir Singh on 4/16/2019.
 **/
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public User registerNewUser(final User user) throws EmailExistsException {
        if (emailExist(user.getEmail())) {
            throw new EmailExistsException("There is an account with that email address: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    private boolean emailExist(String email) {
        final User user = userRepository.findByEmail(email);
        return user != null;
    }

    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    public VerificationToken getVerificationToken(final String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }

}
