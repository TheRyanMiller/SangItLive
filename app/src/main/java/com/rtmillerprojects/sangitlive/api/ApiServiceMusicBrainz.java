package com.rtmillerprojects.sangitlive.api;

import com.rtmillerprojects.sangitlive.model.ArtistResults;
import com.rtmillerprojects.sangitlive.model.MusicBrainzArtistResults;
import com.rtmillerprojects.sangitlive.model.SetlistsByArtists;
import com.rtmillerprojects.sangitlive.model.musicbrainzaritstbrowse.ArtistBrowseResults;
import com.rtmillerprojects.sangitlive.model.musicbrainzartistresourcemodel.MusicBrainzArtistResourceResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ryan on 8/28/2016.
 */
public interface ApiServiceMusicBrainz {
    final static String BASEURL = "http://musicbrainz.org";
    final static String APPSETTINGS = "/ws/2/";
    final static String SEARCHARTISTSENDPOINT = "/ws/2/artist/{mbid}";
    final static String BROWSEARTISTSENDPOINT = "/ws/2/artist/";

    //http://musicbrainz.org/ws/2/artist/?query=artist:the+rolling+stones&fmt=json
    // Artists
    @GET(SEARCHARTISTSENDPOINT)
    Call<MusicBrainzArtistResults> searchArtists (@Query("artistMbid") String artistKey, @Query("fmt") String format);
    //Artist Image
    @GET(SEARCHARTISTSENDPOINT) //?inc=url-rels
    Call<MusicBrainzArtistResourceResult> searchArtistImage (@Path("mbid") String mbid, @Query("fmt") String format, @Query("inc") String resource);

    @GET(BROWSEARTISTSENDPOINT) //?inc=url-rels
    Call<ArtistBrowseResults> browseArtists (@Query("query") String searchString, @Query("fmt") String format);

}
