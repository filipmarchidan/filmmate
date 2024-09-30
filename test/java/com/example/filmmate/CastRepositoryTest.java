package com.example.filmmate;

import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.filmmate.data.models.discovery.CastMediaResponse;
import com.example.filmmate.data.models.media.MediaResponse;
import com.example.filmmate.data.models.setup.PersonResponse;
import com.example.filmmate.repositories.CastRepository;
import com.example.filmmate.repositories.SearchMediaRepository;

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
public class CastRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantRule = new InstantTaskExecutorRule() ;

    MockWebServer mockWebServer;

    CastRepository castRepository;

    PersonResponse personResponse;
    CastMediaResponse castMediaResponse;

    String personResponseJSON;
    String castMediaResponseJSON;

    @Before
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        castRepository = new CastRepository(mockWebServer.getPort());

        castMediaResponseJSON = "{\"id\":337404,\"cast\":[{\"adult\":false,\"gender\":1,\"id\":54693,\"known_for_department\":\"Acting\",\"name\":\"Emma Stone\",\"original_name\":\"Emma Stone\",\"popularity\":41.083,\"profile_path\":\"/2hwXbPW2ffnXUe1Um0WXHG0cTwb.jpg\",\"cast_id\":0,\"character\":\"Estella / Cruella\",\"credit_id\":\"59a50d419251412f02004a64\",\"order\":0},{\"adult\":false,\"gender\":1,\"id\":7056,\"known_for_department\":\"Acting\",\"name\":\"Emma Thompson\",\"original_name\":\"Emma Thompson\",\"popularity\":7.764,\"profile_path\":\"/xr8Ki3CIqweWWqS5q0kUYdiK6oQ.jpg\",\"cast_id\":11,\"character\":\"The Baroness\",\"credit_id\":\"5d3f84f060b58d00106bb4b0\",\"order\":1}]}";
        personResponseJSON = "{\"page\":4,\"results\":[{\"adult\":false,\"gender\":1,\"id\":54693,\"known_for\":[{\"adult\":false,\"backdrop_path\":\"/ik2D3KqxFD0O0Bc3Wv1CZm8sOg8.jpg\",\"genre_ids\":[35,18,10749,10402],\"id\":313369,\"media_type\":\"movie\",\"original_language\":\"en\",\"original_title\":\"La La Land\",\"overview\":\"Mia, an aspiring actress, serves lattes to movie stars in between auditions and Sebastian, a jazz musician, scrapes by playing cocktail party gigs in dingy bars, but as success mounts they are faced with decisions that begin to fray the fragile fabric of their love affair, and the dreams they worked so hard to maintain in each other threaten to rip them apart.\",\"poster_path\":\"/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg\",\"release_date\":\"2016-11-29\",\"title\":\"La La Land\",\"video\":false,\"vote_average\":7.9,\"vote_count\":13321},{\"adult\":false,\"backdrop_path\":\"/xYz4u1jWCKyXGzddMJeOL845DTr.jpg\",\"genre_ids\":[28,12,14],\"id\":1930,\"media_type\":\"movie\",\"original_language\":\"en\",\"original_title\":\"The Amazing Spider-Man\",\"overview\":\"Peter Parker is an outcast high schooler abandoned by his parents as a boy, leaving him to be raised by his Uncle Ben and Aunt May. Like most teenagers, Peter is trying to figure out who he is and how he got to be the person he is today. As Peter discovers a mysterious briefcase that belonged to his father, he begins a quest to understand his parents' disappearance – leading him directly to Oscorp and the lab of Dr. Curt Connors, his father's former partner. As Spider-Man is set on a collision course with Connors' alter ego, The Lizard, Peter will make life-altering choices to use his powers and shape his destiny to become a hero.\",\"poster_path\":\"/dQ8TOCYgP9pzQvSb1cmaalYqdb5.jpg\",\"release_date\":\"2012-06-23\",\"title\":\"The Amazing Spider-Man\",\"video\":false,\"vote_average\":6.6,\"vote_count\":12922},{\"adult\":false,\"backdrop_path\":\"/gXboplsdDKprKA46IptKwDgY6Nr.jpg\",\"genre_ids\":[18,35],\"id\":194662,\"media_type\":\"movie\",\"original_language\":\"en\",\"original_title\":\"Birdman or (The Unexpected Virtue of Ignorance)\",\"overview\":\"A fading actor best known for his portrayal of a popular superhero attempts to mount a comeback by appearing in a Broadway play. As opening night approaches, his attempts to become more altruistic, rebuild his career, and reconnect with friends and family prove more difficult than expected.\",\"poster_path\":\"/lvWL5ZRlYFh7M7fOvYswcRqyprI.jpg\",\"release_date\":\"2014-08-27\",\"title\":\"Birdman or (The Unexpected Virtue of Ignorance)\",\"video\":false,\"vote_average\":7.5,\"vote_count\":10511}],\"known_for_department\":\"Acting\",\"name\":\"Emma Stone\",\"popularity\":41.083,\"profile_path\":\"/2hwXbPW2ffnXUe1Um0WXHG0cTwb.jpg\"}],\"total_pages\":1,\"total_results\":5}";
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getTopRatedCastTest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setBody(personResponseJSON));
        mockWebServer.url("3/person/popular");

        CountDownLatch latch = new CountDownLatch(1);

        castRepository.getTopRatedCast("api", "en-US");

        LiveData<PersonResponse> mediaResponseMutableLiveData = castRepository.getCastResponseMutableLiveData();
        mediaResponseMutableLiveData.observeForever(new Observer<PersonResponse>() {
            @Override
            public void onChanged(PersonResponse response) {
                personResponse = response;
                latch.countDown();
                mediaResponseMutableLiveData.removeObserver(this);
            }
        });


        latch.await(1, TimeUnit.SECONDS);
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(4, personResponse.getPage());
        assertNotNull(personResponse.getPeople());
    }

    @Test
    public void getMovieCastMembersTest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setBody(castMediaResponseJSON));
        mockWebServer.url("3/movie/337404/credits");

        CountDownLatch latch = new CountDownLatch(1);

        castRepository.getMovieCastMembers(337404, "api", "en-US");

        LiveData<CastMediaResponse> mediaResponseMutableLiveData = castRepository.getCastMediaResponseMutableLiveData();
        mediaResponseMutableLiveData.observeForever(new Observer<CastMediaResponse>() {
            @Override
            public void onChanged(CastMediaResponse response) {
                castMediaResponse = response;
                latch.countDown();
                mediaResponseMutableLiveData.removeObserver(this);
            }
        });

        latch.await(1, TimeUnit.SECONDS);
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(337404, castMediaResponse.getId());
        assertNotNull(castMediaResponse.getCast());
    }

    @Test
    public void getTVCastMembersTest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setBody(castMediaResponseJSON));
        mockWebServer.url("3/tv/337404/credits");

        CountDownLatch latch = new CountDownLatch(1);

        castRepository.getTVCastMembers(337404, "api", "en-US");

        LiveData<CastMediaResponse> mediaResponseMutableLiveData = castRepository.getCastMediaResponseMutableLiveData();
        mediaResponseMutableLiveData.observeForever(new Observer<CastMediaResponse>() {
            @Override
            public void onChanged(CastMediaResponse response) {
                castMediaResponse = response;
                latch.countDown();
                mediaResponseMutableLiveData.removeObserver(this);
            }
        });

        latch.await(1, TimeUnit.SECONDS);
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(337404, castMediaResponse.getId());
        assertNotNull(castMediaResponse.getCast());
    }
}
