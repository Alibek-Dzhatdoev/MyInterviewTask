package com.dzhatdoev.myinterviewtask.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "quote_id", nullable = false)
    private Quote quote;

    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    private Person voter;

    @Column(name = "vote_for", nullable = false)
    private Boolean voteFor;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime voteDateTime;

    @Override
    public String toString() {
        return "|quote = " + quote +
                "|, |voter = " + voter +
                "|, |voteFor = " + voteFor;
    }
}