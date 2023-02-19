package com.dzhatdoev.myinterviewtask.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @JsonBackReference
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person author;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "quote", orphanRemoval = true, fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();

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
