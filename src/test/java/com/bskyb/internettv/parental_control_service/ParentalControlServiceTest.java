package com.bskyb.internettv.parental_control_service;

import com.bskyb.internettv.exceptions.ApplicationFailureException;
import com.bskyb.internettv.exceptions.InvalidParentalControlException;
import com.bskyb.internettv.exceptions.MovieNotFoundException;
import com.bskyb.internettv.thirdparty.MovieService;
import com.bskyb.internettv.thirdparty.TechnicalFailureException;
import com.bskyb.internettv.thirdparty.TitleNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by kogbe on 06/06/2018.
 */
public class ParentalControlServiceTest {

    private ParentalControlServiceImpl parentalControlService;

    private static final String PEPPA_PIG = "001000";
    private static final String STAR_WARS = "002000";
    private static final String DEADPOOL =  "003000";
    private static final String INVALID_MOVIE_ID =  "XXXXXX";


    @Mock
    private MovieService movieServiceMock;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        parentalControlService = new ParentalControlServiceImpl(movieServiceMock);

    }

    @After
    public void tearDown() throws Exception{}

    @Test
    public void canWatchPeppaPigMovie(){

        try{
            when(movieServiceMock.getParentalControlLevel(PEPPA_PIG)).thenReturn("U");

            boolean canWatch = parentalControlService.canWatchMovie("U",PEPPA_PIG);

            assertTrue("Should return true for method",canWatch);
        }catch (Exception e){
             e.printStackTrace();
        }

    }

    @Test
    public void cannotWatchDeadPoolPgMoviePG(){

        try{
            when(movieServiceMock.getParentalControlLevel(DEADPOOL)).thenReturn("18");

            boolean canWatch = parentalControlService.canWatchMovie("PG",DEADPOOL);

            assertFalse("Should return false for method",canWatch);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void canWatchStarWarsMovie15(){

        try{
            when(movieServiceMock.getParentalControlLevel(STAR_WARS)).thenReturn("12");

            boolean canWatch = parentalControlService.canWatchMovie("15",STAR_WARS);

            assertTrue("Should return true for method",canWatch);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void cannotWatchStarWarsMoviePG(){

        try{
            when(movieServiceMock.getParentalControlLevel(STAR_WARS)).thenReturn("12");

            boolean canWatch = parentalControlService.canWatchMovie("PG",STAR_WARS);

            assertFalse("Should return false for method",canWatch);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test(expected = MovieNotFoundException.class)
    public void movieTitleNotFound() throws Exception{

        try{
            when(movieServiceMock.getParentalControlLevel(INVALID_MOVIE_ID)).thenThrow(TitleNotFoundException.class);

            boolean canWatch = parentalControlService.canWatchMovie("PG",INVALID_MOVIE_ID);

        }catch (Exception e){
            throw e;
        }
    }

    @Test(expected = ApplicationFailureException.class)
    public void applicationFailure() throws Exception{

        try{
            when(movieServiceMock.getParentalControlLevel(PEPPA_PIG)).thenThrow(TechnicalFailureException.class);

            boolean canWatch = parentalControlService.canWatchMovie("PG",PEPPA_PIG);

        }catch (Exception e){
            throw e;
        }
    }

    @Test(expected = InvalidParentalControlException.class)
    public void invalidParentalControl() throws Exception{
            boolean canWatch = parentalControlService.canWatchMovie("XX",PEPPA_PIG);

    }
}
