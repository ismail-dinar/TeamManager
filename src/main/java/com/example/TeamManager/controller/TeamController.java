package com.example.TeamManager.controller;

import com.example.TeamManager.model.Team;
import com.example.TeamManager.service.TeamService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "api")
public class TeamController {
    @Autowired
    public TeamService teamService;

    /**
     * Handles HTTP GET requests to fetch a team by its ID.
     *
     * @param id the unique identifier of the team to retrieve, extracted from the path variable
     * @return an {@link Optional} containing the team if found, or {@link Optional#empty()} if no team exists with the given ID
     */
    @RequestMapping(path="/team/{id}", method = RequestMethod.GET)
    public Optional<Team> getTeamById(@PathVariable(value = "id") int id) {
        log.info("Received request to fetch team by id {}", id);
        return teamService.getTeamById(id);
    }

    /**
     * Handles HTTP GET requests to fetch a paginated list of teams, with optional sorting.
     *
     * @param sort an optional parameter specifying sorting criteria in the format "field:direction",
     *             where "direction" is 1 for ascending or -1 for descending (e.g., "name:1,acronym:-1");
     *             if null or empty, no sorting is applied
     * @param page the page number to retrieve (zero-based index); defaults to 0 if not provided
     * @param size the number of teams per page; defaults to a predefined value if not provided
     * @return a {@link Page} containing the requested teams, adhering to the pagination and sorting parameters
     */
    @RequestMapping(path="/teams", method = RequestMethod.GET)
    public Page<Team> getTeams(@RequestParam(required = false) String sort, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "20") int size) {
        log.info("Received request to fetch all teams");
        log.info("Sort is {}", sort);
        log.info("Page is {}", page);
        log.info("Size is {}", size);

        Map<String, Integer> sortParams = new HashMap<>();
        if(sort != null && !sort.isEmpty()) {
            String[] sorting = sort.split(",");

            for (String s : sorting) {
                // TODO: Implement validation to ensure the keys correspond to attributes in the Team class.
                String[] sortOrder = s.split(":");
                sortParams.put(sortOrder[0], Integer.parseInt(sortOrder[1]));
            }
        }

        return teamService.getAllTeams(sortParams, page, size);
    }


    /**
     * Handles HTTP POST requests to add a new team to the system.
     *
     * @param team the team object to be added, validated against the defined constraints
     * @param bindingResult contains the results of validating the `team` object; used to check for errors
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *           <li>A 200 OK response with the added team if the operation is successful</li>
     *           <li>A 400 Bad Request response with validation error messages if the team is invalid</li>
     *         </ul>
     */
    @RequestMapping(path="/team", method = RequestMethod.POST)
    public ResponseEntity<?> addTeam(@Valid @RequestBody Team team, BindingResult bindingResult) {
        log.info("Received request to add a team {}", team);
        if (bindingResult.hasErrors()) {
            log.error("Received request with errors: {}", bindingResult.getAllErrors());
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        Team savedTeam = teamService.addTeam(team);
        log.info("Added team {}", savedTeam);
        return ResponseEntity.ok(savedTeam);
    }
}
