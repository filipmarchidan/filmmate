package com.example.filmmate;

import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.filmmate.data.models.discovery.SearchMediaResponse;
import com.example.filmmate.data.models.media.MediaResponse;
import com.example.filmmate.data.models.setup.PersonResponse;
import com.example.filmmate.repositories.SearchMediaRepository;
import com.example.filmmate.repositories.TVResponseRepository;

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
public class SearchMediaRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantRule = new InstantTaskExecutorRule() ;

    SearchMediaRepository searchMediaRepository;

    MockWebServer mockWebServer;

    MediaResponse response;
    SearchMediaResponse searchResponse;
    PersonResponse personResponse;

    String mediaResponseJSON;
    String searchResponseJSON;
    String personResponseJSON;

    @Before
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        searchMediaRepository = new SearchMediaRepository(mockWebServer.getPort());

        mediaResponseJSON = "{\"page\":2,\"results\":[{\"adult\":false,\"backdrop_path\":\"/qi6Edc1OPcyENecGtz8TF0DUr9e.jpg\",\"genre_ids\":[27,9648,53],\"id\":423108,\"original_language\":\"en\",\"original_title\":\"The Conjuring: The Devil Made Me Do It\",\"overview\":\"Paranormal investigators Ed and Lorraine Warren encounter what would become one of the most sensational cases from their files. The fight for the soul of a young boy takes them beyond anything they'd ever seen before, to mark the first time in U.S. history that a murder suspect would claim demonic possession as a defense.\",\"popularity\":5355.58,\"poster_path\":\"/xbSuFiJbbBWCkyCCKIMfuDCA4yV.jpg\",\"release_date\":\"2021-05-25\",\"title\":\"The Conjuring: The Devil Made Me Do It\",\"video\":false,\"vote_average\":8.3,\"vote_count\":1832}],\"total_pages\":500,\"total_results\":10000}";
        searchResponseJSON = "{\"page\":1,\"results\":[{\"backdrop_path\":\"/qJxzjUjCpTPvDHldNnlbRC4OqEh.jpg\",\"first_air_date\":\"2019-02-15\",\"genre_ids\":[10759,10765,18],\"id\":75006,\"media_type\":\"tv\",\"name\":\"The Umbrella Academy\",\"origin_country\":[\"US\"],\"original_language\":\"en\",\"original_name\":\"The Umbrella Academy\",\"overview\":\"A dysfunctional family of superheroes comes together to solve the mystery of their father's death, the threat of the apocalypse and more.\",\"popularity\":151.703,\"poster_path\":\"/scZlQQYnDVlnpxFTxaIv2g0BWnL.jpg\",\"vote_average\":8.7,\"vote_count\":6946}],\"total_pages\":1,\"total_results\":1}";
        personResponseJSON = "{\"page\":4,\"results\":[{\"adult\":false,\"gender\":1,\"id\":54693,\"known_for\":[{\"adult\":false,\"backdrop_path\":\"/ik2D3KqxFD0O0Bc3Wv1CZm8sOg8.jpg\",\"genre_ids\":[35,18,10749,10402],\"id\":313369,\"media_type\":\"movie\",\"original_language\":\"en\",\"original_title\":\"La La Land\",\"overview\":\"Mia, an aspiring actress, serves lattes to movie stars in between auditions and Sebastian, a jazz musician, scrapes by playing cocktail party gigs in dingy bars, but as success mounts they are faced with decisions that begin to fray the fragile fabric of their love affair, and the dreams they worked so hard to maintain in each other threaten to rip them apart.\",\"poster_path\":\"/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg\",\"release_date\":\"2016-11-29\",\"title\":\"La La Land\",\"video\":false,\"vote_average\":7.9,\"vote_count\":13321},{\"adult\":false,\"backdrop_path\":\"/xYz4u1jWCKyXGzddMJeOL845DTr.jpg\",\"genre_ids\":[28,12,14],\"id\":1930,\"media_type\":\"movie\",\"original_language\":\"en\",\"original_title\":\"The Amazing Spider-Man\",\"overview\":\"Peter Parker is an outcast high schooler abandoned by his parents as a boy, leaving him to be raised by his Uncle Ben and Aunt May. Like most teenagers, Peter is trying to figure out who he is and how he got to be the person he is today. As Peter discovers a mysterious briefcase that belonged to his father, he begins a quest to understand his parents' disappearance – leading him directly to Oscorp and the lab of Dr. Curt Connors, his father's former partner. As Spider-Man is set on a collision course with Connors' alter ego, The Lizard, Peter will make life-altering choices to use his powers and shape his destiny to become a hero.\",\"poster_path\":\"/dQ8TOCYgP9pzQvSb1cmaalYqdb5.jpg\",\"release_date\":\"2012-06-23\",\"title\":\"The Amazing Spider-Man\",\"video\":false,\"vote_average\":6.6,\"vote_count\":12922},{\"adult\":false,\"backdrop_path\":\"/gXboplsdDKprKA46IptKwDgY6Nr.jpg\",\"genre_ids\":[18,35],\"id\":194662,\"media_type\":\"movie\",\"original_language\":\"en\",\"original_title\":\"Birdman or (The Unexpected Virtue of Ignorance)\",\"overview\":\"A fading actor best known for his portrayal of a popular superhero attempts to mount a comeback by appearing in a Broadway play. As opening night approaches, his attempts to become more altruistic, rebuild his career, and reconnect with friends and family prove more difficult than expected.\",\"poster_path\":\"/lvWL5ZRlYFh7M7fOvYswcRqyprI.jpg\",\"release_date\":\"2014-08-27\",\"title\":\"Birdman or (The Unexpected Virtue of Ignorance)\",\"video\":false,\"vote_average\":7.5,\"vote_count\":10511}],\"known_for_department\":\"Acting\",\"name\":\"Emma Stone\",\"popularity\":41.083,\"profile_path\":\"/2hwXbPW2ffnXUe1Um0WXHG0cTwb.jpg\"}],\"total_pages\":1,\"total_results\":5}";
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getSearchMovieTest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setBody(mediaResponseJSON));
        mockWebServer.url("4/search/movie");

        CountDownLatch latch = new CountDownLatch(1);

        searchMediaRepository.getSearchMovie("api", "en-US", "query", false);

        LiveData<MediaResponse> mediaResponseMutableLiveData = searchMediaRepository.getMediaResponseLiveData();
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
    public void getSearchTVTest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setBody(mediaResponseJSON));
        mockWebServer.url("4/search/tv");

        CountDownLatch latch = new CountDownLatch(1);

        searchMediaRepository.getSearchTV("api", "en-US", "query", false);

        LiveData<MediaResponse> mediaResponseMutableLiveData = searchMediaRepository.getMediaResponseLiveData();
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
    public void getSearchMediaTest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setBody(searchResponseJSON));
        mockWebServer.url("4/search/multi");

        CountDownLatch latch = new CountDownLatch(1);

        searchMediaRepository.getSearchMedia("api", "en-US", "query", false);

        LiveData<SearchMediaResponse> mediaResponseMutableLiveData = searchMediaRepository.getSearchMediaResponseLiveData();
        mediaResponseMutableLiveData.observeForever(new Observer<SearchMediaResponse>() {
            @Override
            public void onChanged(SearchMediaResponse mediaResponse) {
                searchResponse = mediaResponse;
                latch.countDown();
                mediaResponseMutableLiveData.removeObserver(this);
            }
        });


        latch.await(1, TimeUnit.SECONDS);
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(1, searchResponse.getPage());
        assertNotNull(searchResponse.getSearchMedia());
    }

    @Test
    public void getSearchPeopleTest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setBody(personResponseJSON));
        mockWebServer.url("4/search/person");

        CountDownLatch latch = new CountDownLatch(1);

        searchMediaRepository.getSearchPeople("api", "en-US", "query", false);

        LiveData<PersonResponse> personResponseMutableLiveData = searchMediaRepository.getPersonResponseLiveData();

        personResponseMutableLiveData.observeForever(new Observer<PersonResponse>() {
            @Override
            public void onChanged(PersonResponse response) {
                personResponse = response;
                latch.countDown();
                personResponseMutableLiveData.removeObserver(this);
            }
        });

        latch.await(1, TimeUnit.SECONDS);
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(4, personResponse.getPage());
        assertNotNull(personResponse.getPeople());
    }
}
