package com.peg2s.repositories;

import com.peg2s.models.PersonalRating;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RatingRepository extends CrudRepository<PersonalRating, Long> {
    List<PersonalRating> findByUser_login(String login);
}
