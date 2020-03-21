package com.voterapp.voter.vapp.repository;

import com.voterapp.voter.vapp.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {

    List<Poll> findAllByCreatedBy(String username);

}
