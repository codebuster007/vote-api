package com.voterapp.voter.vapp.model;

import com.voterapp.voter.vapp.util.DateAudit;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "poll_id",
                "user_id"
        })
})
@EqualsAndHashCode(callSuper = true)
@Entity(name = "votes")
@Accessors(chain = true)
@Data
public class Vote extends DateAudit {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 4573187835958695624L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "poll_id", referencedColumnName = "pollId", nullable = false)
    private Poll poll;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_id", referencedColumnName = "optionId", nullable = false)
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "userId",nullable = false)
    private User user;
}
