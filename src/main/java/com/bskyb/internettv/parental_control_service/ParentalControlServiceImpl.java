package com.bskyb.internettv.parental_control_service;

import com.bskyb.internettv.exceptions.ApplicationFailureException;
import com.bskyb.internettv.exceptions.InvalidParentalControlException;
import com.bskyb.internettv.exceptions.MovieNotFoundException;
import com.bskyb.internettv.thirdparty.MovieService;
import com.bskyb.internettv.thirdparty.TechnicalFailureException;
import com.bskyb.internettv.thirdparty.TitleNotFoundException;

import java.util.HashMap;

/**
 * Created by kogbe on 06/06/2018.
 */
public class ParentalControlServiceImpl implements  ParentalControlService {

    private  MovieService movieService;

    private HashMap<String, Integer> controlLevels;

    private ParentalControlServiceImpl(){
        this.controlLevels = new HashMap();
        this.controlLevels.put("U",0);
        this.controlLevels.put("PG",1);
        this.controlLevels.put("12",2);
        this.controlLevels.put("15",3);
        this.controlLevels.put("18",4);
    }

    public ParentalControlServiceImpl(MovieService movieService){
        this();
        this.movieService = movieService;
    }


    public boolean canWatchMovie(String customerParentalControlLevel, String movieId) throws ApplicationFailureException,InvalidParentalControlException,MovieNotFoundException {

        if(!this.controlLevels.containsKey(customerParentalControlLevel)){
            throw new InvalidParentalControlException();
        }

        try{

            String  movieLevel = movieService.getParentalControlLevel(movieId);
            return  (compareLevels(customerParentalControlLevel,movieLevel) >= 0);

        }catch (TitleNotFoundException e){
            throw  new MovieNotFoundException();
        }catch (TechnicalFailureException e){
           throw new ApplicationFailureException("Unexpected error from movie service",e);
        }catch (Exception e){
            throw  new ApplicationFailureException("Unexpected application error",e);
        }

    }

    private  int compareLevels (String parentLevel, String movieLevel){
            Integer parentLevelValue = controlLevels.get(parentLevel);
            Integer movieLevelValue =  controlLevels.get(movieLevel);
            return parentLevelValue.compareTo(movieLevelValue);
    }
}
