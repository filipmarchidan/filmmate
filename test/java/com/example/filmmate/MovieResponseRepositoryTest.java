package com.example.filmmate;

import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.filmmate.data.models.media.MediaHelper;
import com.example.filmmate.data.models.media.MediaResponse;
import com.example.filmmate.repositories.MovieResponseRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class MovieResponseRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantRule = new InstantTaskExecutorRule() ;

    MovieResponseRepository movieResponseRepository;

    MockWebServer mockWebServer;
    MediaResponse response;

    String mediaResponseJSON;



    @Before
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        movieResponseRepository = new MovieResponseRepository(mockWebServer.getPort());

        mediaResponseJSON = "{\"page\":2,\"results\":[{\"adult\":false,\"backdrop_path\":\"/qi6Edc1OPcyENecGtz8TF0DUr9e.jpg\",\"genre_ids\":[27,9648,53],\"id\":423108,\"original_language\":\"en\",\"original_title\":\"The Conjuring: The Devil Made Me Do It\",\"overview\":\"Paranormal investigators Ed and Lorraine Warren encounter what would become one of the most sensational cases from their files. The fight for the soul of a young boy takes them beyond anything they'd ever seen before, to mark the first time in U.S. history that a murder suspect would claim demonic possession as a defense.\",\"popularity\":5355.58,\"poster_path\":\"/xbSuFiJbbBWCkyCCKIMfuDCA4yV.jpg\",\"release_date\":\"2021-05-25\",\"title\":\"The Conjuring: The Devil Made Me Do It\",\"video\":false,\"vote_average\":8.3,\"vote_count\":1832}],\"total_pages\":500,\"total_results\":10000}";
        mockWebServer.enqueue(new MockResponse().setBody(mediaResponseJSON));
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }


    @Test
    public void getPopularMoviesTest() throws InterruptedException {
        mockWebServer.url("4/discover/movie");

        CountDownLatch latch = new CountDownLatch(1);

        movieResponseRepository.getPopularMovies("api", "en-US", "popularity.desc", false);

        LiveData<MediaResponse> mediaResponseMutableLiveData = movieResponseRepository.getPopularMovieLiveData();
        mediaResponseMutableLiveData.observeForever(new Observer<MediaResponse>() {
            @Override
            public void onChanged(MediaResponse mediaResponse) {
                response = mediaResponse;
                latch.countDown();
                mediaResponseMutableLiveData.removeObserver(this);
            }
        });


        latch.await(1, TimeUnit.SECONDS);
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(2, response.getPage());
        assertNotNull(response.getMediaHelpers());
    }

    @Test
    public void getTopRatedMoviesTest() throws InterruptedException {
        mockWebServer.url("3/movie/top_rated");

        CountDownLatch latch = new CountDownLatch(1);

        movieResponseRepository.getTopRatedMovies("api", "en-US");

        LiveData<MediaResponse> mediaResponseMutableLiveData = movieResponseRepository.getTopRatedMovieLiveData();
        mediaResponseMutableLiveData.observeForever(new Observer<MediaResponse>() {
            @Override
            public void onChanged(MediaResponse mediaResponse) {
                response = mediaResponse;
                latch.countDown();
                mediaResponseMutableLiveData.removeObserver(this);
            }
        });


        latch.await(1, TimeUnit.SECONDS);
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(2, response.getPage());
        assertNotNull(response.getMediaHelpers());
    }

    @Test
    public void getUpcomingMovies() throws InterruptedException {
        mockWebServer.url("3/movie/upcoming");

        CountDownLatch latch = new CountDownLatch(1);

        movieResponseRepository.getUpcomingMovies("api", "en-US");

        LiveData<MediaResponse> mediaResponseMutableLiveData = movieResponseRepository.getUpcomingMovieLiveData();
        mediaResponseMutableLiveData.observeForever(new Observer<MediaResponse>() {
            @Override
            public void onChanged(MediaResponse mediaResponse) {
                response = mediaResponse;
                latch.countDown();
                mediaResponseMutableLiveData.removeObserver(this);
            }
        });


        latch.await(1, TimeUnit.SECONDS);
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(2, response.getPage());
        assertNotNull(response.getMediaHelpers());
    }

    @Test
    public void getRecommendedMovies() throws InterruptedException {
        mockWebServer.url("3/movie/423108/recommendations");

        CountDownLatch latch = new CountDownLatch(1);

        movieResponseRepository.getRecommendedMovies(Arrays.asList("423108"),"api", "en-US");

        LiveData<List<MediaHelper>> mediaMutableLiveData = movieResponseRepository.getRecommendedMovieLiveData();

        mediaMutableLiveData.observeForever(new Observer<List<MediaHelper>>() {
            @Override
            public void onChanged(List<MediaHelper> mediaHelpers) {
                assertNotNull(mediaHelpers);

                latch.countDown();
                mediaMutableLiveData.removeObserver(this);
            }
        });
        latch.await(1, TimeUnit.SECONDS);
        shadowOf(Looper.getMainLooper()).idle();

    }


}
