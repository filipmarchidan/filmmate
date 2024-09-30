package com.example.filmmate;

import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.filmmate.data.models.media.MediaHelper;
import com.example.filmmate.data.models.media.MediaResponse;
import com.example.filmmate.repositories.MovieResponseRepository;
import com.example.filmmate.repositories.TVResponseRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class TVResponseRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantRule = new InstantTaskExecutorRule() ;

    TVResponseRepository tvResponseRepository;

    MockWebServer mockWebServer;
    MediaResponse response;

    String mediaResponseJSON;

    @Before
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        tvResponseRepository = new TVResponseRepository(mockWebServer.getPort());

        mediaResponseJSON = "{\"page\":2,\"results\":[{\"adult\":false,\"backdrop_path\":\"/qi6Edc1OPcyENecGtz8TF0DUr9e.jpg\",\"genre_ids\":[27,9648,53],\"id\":423108,\"original_language\":\"en\",\"original_title\":\"The Conjuring: The Devil Made Me Do It\",\"overview\":\"Paranormal investigators Ed and Lorraine Warren encounter what would become one of the most sensational cases from their files. The fight for the soul of a young boy takes them beyond anything they'd ever seen before, to mark the first time in U.S. history that a murder suspect would claim demonic possession as a defense.\",\"popularity\":5355.58,\"poster_path\":\"/xbSuFiJbbBWCkyCCKIMfuDCA4yV.jpg\",\"release_date\":\"2021-05-25\",\"title\":\"The Conjuring: The Devil Made Me Do It\",\"video\":false,\"vote_average\":8.3,\"vote_count\":1832}],\"total_pages\":500,\"total_results\":10000}";
        mockWebServer.enqueue(new MockResponse().setBody(mediaResponseJSON));
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }


    @Test
    public void getPopularTVTest() throws InterruptedException {
        mockWebServer.url("4/discover/tv");

        CountDownLatch latch = new CountDownLatch(1);

        tvResponseRepository.getPopularTV("api", "en-US", "popularity.desc");

        LiveData<MediaResponse> mediaResponseMutableLiveData = tvResponseRepository.getPopularTVLiveData();
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
    public void getTopRatedTVTest() throws InterruptedException {
        mockWebServer.url("4/tv/top_rated");

        CountDownLatch latch = new CountDownLatch(1);

        tvResponseRepository.getTopRatedTV("api", "en-US");

        LiveData<MediaResponse> mediaResponseMutableLiveData = tvResponseRepository.getTopRatedTVLiveData();
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
    public void getOnAirTVTest() throws InterruptedException {
        mockWebServer.url("3/tv/on_the_air");

        CountDownLatch latch = new CountDownLatch(1);

        tvResponseRepository.getOnAirTV("api", "en-US");

        LiveData<MediaResponse> mediaResponseMutableLiveData = tvResponseRepository.getOnAirTVLiveData();
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
    public void getRecommendedTV() throws InterruptedException {
        mockWebServer.url("3/tv/423108/recommendations");

        CountDownLatch latch = new CountDownLatch(1);

        tvResponseRepository.getRecommendedTV(Arrays.asList("423108"),"api", "en-US");

        LiveData<List<MediaHelper>> mediaMutableLiveData = tvResponseRepository.getRecommendedTVLiveData();

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
