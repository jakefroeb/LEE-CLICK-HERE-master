package com.theironyard.services;

import com.theironyard.entities.Game;
import com.theironyard.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Zach on 10/5/16.
 */
public interface GameRepository extends CrudRepository<Game, Integer>{

    List<Game> findByUser(User user);
    List<Game> findByGenre(String genre); //hibernate method, findBy(columnName)
    List<Game> findByReleaseYear(int releaseYear);

    @Query("SELECT g FROM Game g WHERE g.name LIKE ?1%") //not selecting from table name, using class name because of hibernate
    List<Game> findByNameStartsWith(String name);
}
