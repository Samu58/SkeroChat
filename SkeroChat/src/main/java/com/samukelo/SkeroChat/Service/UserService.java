package com.samukelo.SkeroChat.Service;

import com.samukelo.SkeroChat.Model.User;
import com.samukelo.SkeroChat.Repository.UserRepository;
import com.samukelo.SkeroChat.Status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(String firstname, String lastname, String email, String password ){
        if(email == null || password == null || firstname == null){

            return null;
        }else {
            //Checking duplicate methods
            if(userRepository.findFirstByLogin(email).isPresent()){
                System.out.println("Duplicate login");
                return null;
            }

            User user = new User();
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setEmail(email);
            user.setPassword(password);

            return userRepository.save(user);
        }
    }

    public User authenticate(String email, String password){
        return userRepository.findByEmailAndPassword(email,password).orElse(null);
    }
    public void saveUser(User user){
        //this also means connecting

        user.setStatus(Status.ONLINE);
        userRepository.save(user);
    }

    public void disconnected(User user){
        var storedUser = userRepository.findById(user.getId())
                .orElse(null);

        if (storedUser != null){
            storedUser.setStatus(Status.OFFLINE);
            userRepository.save(storedUser);
        }

    }
    public List<User> findConnectedUsers(){

        return userRepository.findAllByStatus(Status.ONLINE);
    }
}
