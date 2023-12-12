package com.samukelo.SkeroChat.Repository;

import com.samukelo.SkeroChat.Model.User;
import com.samukelo.SkeroChat.Status.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByStatus(Status status);
    Optional<User> findFirstByLogin(String email);
    Optional<User> findByEmailAndPassword(String email,String password);
}
