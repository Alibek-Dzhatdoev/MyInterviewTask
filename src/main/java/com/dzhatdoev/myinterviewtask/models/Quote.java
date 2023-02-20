package com.dzhatdoev.myinterviewtask.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "quote")
public class Quote {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Quote must not be empty")
    @Size(min = 2, max = 500, message = "Quote must be between 2 and 500 characters")
    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person author;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Column(name = "pos_or_neg_votes")
    private int posOrNegVotes;

    public Quote(String text, Person author) {
        this.text = text;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote = (Quote) o;
        return text.equals(quote.text) && author.equals(quote.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, author);
    }

    public void incrementVotesFor() {
        posOrNegVotes++;
    }

    public void incrementVotesAgainst() {
        posOrNegVotes--;
    }

    @Override
    public String toString() {
        return
                "text = " + text + '\'' +
                        ", author = " + author +
                        ", posOrNegVotes = " + posOrNegVotes;
    }
}
