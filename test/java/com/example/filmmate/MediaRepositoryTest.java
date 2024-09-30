package com.example.filmmate;

import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.filmmate.data.models.media.MediaModel;
import com.example.filmmate.data.models.media.MediaResponse;
import com.example.filmmate.repositories.MediaRepository;
import com.example.filmmate.repositories.MovieResponseRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class MediaRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantRule = new InstantTaskExecutorRule() ;

    MockWebServer mockWebServer;

    MediaRepository mediaRepository;

    String responseJSON;
    MediaModel mediaModel;

    @Before
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mediaRepository = new MediaRepository(mockWebServer.getPort());

        responseJSON = "{\"adult\":false,\"backdrop_path\":\"/8ChCpCYxh9YXusmHwcE9YzP0TSG.jpg\",\"belongs_to_collection\":{\"id\":837007,\"name\":\"Cruella Collection\",\"poster_path\":null,\"backdrop_path\":null},\"budget\":200000000,\"genres\":[{\"id\":35,\"name\":\"Comedy\"},{\"id\":80,\"name\":\"Crime\"}],\"homepage\":\"https://movies.disney.com/cruella\",\"id\":337404,\"imdb_id\":\"tt3228774\",\"original_language\":\"en\",\"original_title\":\"Cruella\",\"overview\":\"In 1970s London amidst the punk rock revolution, a young grifter named Estella is determined to make a name for herself with her designs. She befriends a pair of young thieves who appreciate her appetite for mischief, and together they are able to build a life for themselves on the London streets. One day, Estella’s flair for fashion catches the eye of the Baroness von Hellman, a fashion legend who is devastatingly chic and terrifyingly haute. But their relationship sets in motion a course of events and revelations that will cause Estella to embrace her wicked side and become the raucous, fashionable and revenge-bent Cruella.\",\"popularity\":4774.831,\"poster_path\":\"/rTh4K5uw9HypmpGslcKd4QfHl93.jpg\",\"production_companies\":[{\"id\":2,\"logo_path\":\"/wdrCwmRnLFJhEoH8GSfymY85KHT.png\",\"name\":\"Walt Disney Pictures\",\"origin_country\":\"US\"}],\"production_countries\":[{\"iso_3166_1\":\"US\",\"name\":\"United States of America\"}],\"release_date\":\"2021-05-26\",\"revenue\":129300000,\"runtime\":134,\"spoken_languages\":[{\"english_name\":\"English\",\"iso_639_1\":\"en\",\"name\":\"English\"}],\"status\":\"Released\",\"tagline\":\"Hello Cruel World\",\"title\":\"Cruella\",\"video\":false,\"vote_average\":8.6,\"vote_count\":2694}";
        mockWebServer.enqueue(new MockResponse().setBody(responseJSON));
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getDetailsMovieTest() throws InterruptedException {
        mockWebServer.url("3/movie/337404");

        CountDownLatch latch = new CountDownLatch(1);

        mediaRepository.getDetailsMovie(337404, "api", "en-US");

        LiveData<MediaModel> mediaResponseMutableLiveData = mediaRepository.getMediaModelLiveData();

        mediaResponseMutableLiveData.observeForever(new Observer<MediaModel>() {
            @Override
            public void onChanged(MediaModel response) {
                mediaModel = response;
                latch.countDown();
                mediaResponseMutableLiveData.removeObserver(this);
            }
        });


        latch.await(1, TimeUnit.SECONDS);
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(337404, mediaModel.getId());
        assertNotNull(mediaModel.getGenre());

    }

    @Test
    public void getDetailsTVTest() throws InterruptedException {
        mockWebServer.url("3/tv/337404");

        CountDownLatch latch = new CountDownLatch(1);

        mediaRepository.getDetailsTV(337404, "api", "en-US");

        LiveData<MediaModel> mediaResponseMutableLiveData = mediaRepository.getMediaModelLiveData();

        mediaResponseMutableLiveData.observeForever(new Observer<MediaModel>() {
            @Override
            public void onChanged(MediaModel response) {
                mediaModel = response;
                latch.countDown();
                mediaResponseMutableLiveData.removeObserver(this);
            }
        });


        latch.await(1, TimeUnit.SECONDS);
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(337404, mediaModel.getId());
        assertNotNull(mediaModel.getGenre());

    }
}
