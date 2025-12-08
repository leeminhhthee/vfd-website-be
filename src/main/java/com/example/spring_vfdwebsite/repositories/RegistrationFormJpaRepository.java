package com.example.spring_vfdwebsite.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.spring_vfdwebsite.dtos.registrationFormDTOs.TeamRegistrationDto;
import com.example.spring_vfdwebsite.entities.RegistrationForm;

@Repository
public interface RegistrationFormJpaRepository extends JpaRepository<RegistrationForm, Integer> {

    boolean existsByTournament_IdAndEmail(Integer tournamentId, String email);

    @Query("""
                SELECT new com.example.spring_vfdwebsite.dtos.registrationFormDTOs.TeamRegistrationDto(
                    r.id,
                    r.teamName,
                    r.coach,
                    r.registrationUnit,
                    r.numberAthletes
                )
                FROM RegistrationForm r
                WHERE r.tournament.id = :tournamentId
                  AND r.status = com.example.spring_vfdwebsite.entities.enums.RegistrationStatusEnum.ACCEPTED
            """)
    List<TeamRegistrationDto> findAcceptedTeamsDto( @Param("tournamentId")Integer tournamentId);

}
