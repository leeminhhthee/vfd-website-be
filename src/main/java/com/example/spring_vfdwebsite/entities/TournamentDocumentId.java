package com.example.spring_vfdwebsite.entities;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.Hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentDocumentId implements Serializable {

    private static final long serialVersionUID = 552538177286132061L;

    @NotNull
    @Column(name = "tournament_id", nullable = false)
    private Integer tournamentId;
    
    @NotNull
    @Column(name = "document_id", nullable = false)
    private Integer documentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;

        TournamentDocumentId entity = (TournamentDocumentId) o;

        return Objects.equals(this.tournamentId, entity.tournamentId) &&
               Objects.equals(this.documentId, entity.documentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tournamentId, documentId);
    }
}
