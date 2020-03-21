package com.voterapp.voter.vapp.repository;

import com.voterapp.voter.vapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Query for a user by username
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * Query a user by email
     *
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * Enable querying for all users with username like provided
     * @param username
     * @return
     */
    List<User> findByUsernameContainingIgnoreCase(String username);
}
