package com.voterapp.voter.vapp.repository;

import com.voterapp.voter.vapp.model.OptionVoteCount;
import com.voterapp.voter.vapp.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT NEW com.voterapp.voter.vapp.model.OptionVoteCount(v.option.optionId, count(v.voteId)) FROM votes v WHERE v.poll.pollId = :pollId GROUP BY v.option.optionId")
    List<OptionVoteCount> countByPollIdGroupByOptionId(@Param("pollId") Long pollId);

    @Query("SELECT NEW com.voterapp.voter.vapp.model.OptionVoteCount(v.option.optionId, count(v.voteId)) FROM votes  v WHERE v.poll.pollId in :pollIds GROUP BY v.option.optionId")
    List<OptionVoteCount> countByPollIdInGroupByOptionId(@Param("pollIds") List<Long> pollIds);
}
