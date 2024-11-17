package com.example.TeamManager.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Please provide a team name")
    private String name;

    @NotBlank(message = "Please provide a team acronym")
    private String acronym;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> players;

    @NotNull(message = "Please provide a team budget")
    @Min(value = 1, message = "User ID must be greater than 0")
    private long budget;
}
