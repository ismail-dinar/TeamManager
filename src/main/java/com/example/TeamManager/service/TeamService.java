package com.example.TeamManager.service;

import com.example.TeamManager.model.Team;
import com.example.TeamManager.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    /**
     * Retrieves a paginated and optionally sorted list of all teams.
     *
     * @param sortParams a map containing sorting parameters where keys are field names and values are directions:
     *                   <ul>
     *                     <li>1 for ascending</li>
     *                     <li>-1 for descending</li>
     *                   </ul>
     * @param page the page number to retrieve (zero-based index)
     * @param size the number of items per page
     * @return a {@link Page} containing the paginated list of teams
     */
    public Page<Team> getAllTeams(Map<String, Integer> sortParams, int page, int size) {
        Sort sort = createSortFromParams(sortParams);
        Pageable pageable = PageRequest.of(page, size, sort);

        return teamRepository.findAll(pageable);
    }

    /**
     * Retrieves a team by its id.
     *
     * @param id the unique identifier of the team
     * @return an {@link Optional} containing the team if found, or {@link Optional#empty()} if no team exists with the given ID
     */
    public Optional<Team> getTeamById(int id) {
        return teamRepository.findById(id);
    }

    /**
     * Adds a new team to the database.
     *
     * @param team the {@link Team} object to be added
     * @return the added {@link Team} object after being saved to the database
     */
    public Team addTeam(Team team) {
       return teamRepository.save(team);
    }

    /**
     * Creates a {@link Sort} object based on sorting parameters.
     *
     * @param sortParams a map where keys are field names to sort by and values are directions:
     *                   <ul>
     *                     <li>1 for ascending</li>
     *                     <li>-1 for descending</li>
     *                   </ul>
     * @return a {@link Sort} object representing the sorting criteria
     */
    private Sort createSortFromParams(Map<String, Integer> sortParams) {
        Sort sort = Sort.unsorted();

        for (Map.Entry<String, Integer> entry : sortParams.entrySet()) {
            String columnName = entry.getKey();
            int direction = entry.getValue();
            Sort.Order order = new Sort.Order(
                    direction == -1 ? Sort.Direction.DESC : Sort.Direction.ASC,
                    columnName
            );
            sort = sort.and(Sort.by(order));
        }
        return sort;
    }
}
