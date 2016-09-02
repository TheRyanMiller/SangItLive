package com.rtmillerprojects.sangitlive.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ryan on 8/25/2016.
 */
public class ArtistResults {


    /**
     * @itemsPerPage : 30
     * @page : 1
     * @total : 836
     * artist : [{"@mbid":"d92b3e1f-999a-4348-9d82-d1905f295bdb","@name":"!ngvar Presented By Radio Juicy","@sortName":"!ngvar Radio Juicy","url":"http://www.setlist.fm/setlists/ngvar-presented-by-radio-juicy-53df6389.html"},{"@disambiguation":"","@mbid":"52549879-6372-421d-9447-47338e301ed8","@name":"\"Ostankino\" TV and Radio Russian Folk Instruments Orchestra","@sortName":"\"Ostankino\" TV and Radio Russian Folk Instruments Orchestra","url":"http://www.setlist.fm/setlists/ostankino-tv-and-radio-russian-folk-instruments-orchestra-5bd8e318.html"},{"@mbid":"c236cc76-d92a-4224-8f50-927593800c29","@name":"1976 Official Radio Borowitch Orchestra","@sortName":"1976 Official Radio Borowitch Orchestra","url":"http://www.setlist.fm/setlists/1976-official-radio-borowitch-orchestra-3d5196f.html"},{"@disambiguation":"","@mbid":"202accab-5618-4fd2-b609-5613a5fca556","@name":"2 Bit Radio","@sortName":"2 Bit Radio","url":"http://www.setlist.fm/setlists/2-bit-radio-63d9463f.html"},{"@disambiguation":"Channel 4 of the British Broadcasting Corporation radio division","@mbid":"eda922db-3f45-4238-9457-351ecbfa4f55","@name":"BBC Radio 4","@sortName":"4, BBC Radio","url":"http://www.setlist.fm/setlists/bbc-radio-4-6bdfd2ea.html"},{"@mbid":"d4a8eb93-ff8c-4106-8e5c-652ce665691d","@name":"8bit bEtty vs. Drown Radio","@sortName":"8bit bEtty vs. Drown Radio","url":"http://www.setlist.fm/setlists/8bit-betty-vs-drown-radio-63d47a4b.html"},{"@disambiguation":"industrial","@mbid":"0ee44671-aae5-4ec6-8287-eb6f0b86ec46","@name":"A Gasping Radio","@sortName":"A Gasping Radio","url":"http://www.setlist.fm/setlists/a-gasping-radio-7bdb921c.html"},{"@mbid":"8a7809f0-0fef-490d-9698-b02e523327b9","@name":"A Radio With Guts","@sortName":"A Radio With Guts","url":"http://www.setlist.fm/setlists/a-radio-with-guts-13d7e1ad.html"},{"@disambiguation":"","@mbid":"e44dd7c7-0b1b-43a1-bf95-2f10e0258809","@name":"ABC Radio Orchestra","@sortName":"ABC Radio Orchestra","url":"http://www.setlist.fm/setlists/abc-radio-orchestra-33db58b9.html"},{"@disambiguation":"","@mbid":"a62ac3ca-b022-4056-97d7-2e7c855116f2","@name":"AM Radio","@sortName":"AM Radio","url":"http://www.setlist.fm/setlists/am-radio-73d67a09.html"},{"@disambiguation":"","@mbid":"3df352c3-30e8-4d57-b67a-b8326ae4dc7a","@name":"Acapulco Radio","@sortName":"Acapulco Radio","url":"http://www.setlist.fm/setlists/acapulco-radio-53d17771.html"},{"@mbid":"d46e3775-1411-4fe0-b80d-5ac2f5f255e1","@name":"Agitated Radio Pilot","@sortName":"Agitated Radio Pilot","url":"http://www.setlist.fm/setlists/agitated-radio-pilot-3d4d1ab.html"},{"@disambiguation":"","@mbid":"9079fc9e-ea44-42cc-92e3-ecc47dbaf3e8","@name":"Airship Entertainment Galvanic Players","@sortName":"Airship Entertainment Galvanic Players","url":"http://www.setlist.fm/setlists/airship-entertainment-galvanic-players-3bdba45c.html"},{"@disambiguation":"","@mbid":"f4259b17-1762-4f4b-902a-ff07a8fa4f66","@name":"Alien Radio","@sortName":"Alien Radio","url":"http://www.setlist.fm/setlists/alien-radio-13def9d9.html"},{"@mbid":"932cb290-4bea-48a9-9c3d-f91ac6f951dd","@name":"Alien Radio Station","@sortName":"Alien Radio Station","url":"http://www.setlist.fm/setlists/alien-radio-station-5bd093d0.html"},{"@disambiguation":"","@mbid":"53d2a4d1-556e-4701-a8ac-5fcb74a8e393","@name":"All India Radio","@sortName":"All India Radio","url":"http://www.setlist.fm/setlists/all-india-radio-4bd2cfd2.html"},{"@mbid":"53a78f51-01c7-45a9-9111-05e3364d7cd6","@name":"All Night Radio","@sortName":"All Night Radio","url":"http://www.setlist.fm/setlists/all-night-radio-73d75e05.html"},{"@disambiguation":"","@mbid":"1b9767b0-3ba3-4055-9e67-fd7bb3e99ffb","@name":"All-Union Radio and Television Symphony Orchestra","@sortName":"All Union Radio and Television Symphony Orchestra","url":"http://www.setlist.fm/setlists/all-union-radio-and-television-symphony-orchestra-3db6173.html"},{"@disambiguation":"","@mbid":"123b6335-d563-4947-a434-a796f8a03c11","@name":"Alternative Radio","@sortName":"Alternative Radio","url":"http://www.setlist.fm/setlists/alternative-radio-43c45377.html"},{"@mbid":"76634b8e-3c2b-4bd2-a76a-03acc30c2561","@name":"Amadeus Chamber Orchestra of the Polish Radio","@sortName":"Amadeus Chamber Orchestra of the Polish Radio","url":"http://www.setlist.fm/setlists/amadeus-chamber-orchestra-of-the-polish-radio-53d1cf59.html"},{"@mbid":"bb003e19-4f76-4d0b-86e3-82144f9db142","@name":"Amadeus Chamber Orchestra of the Polish Radio, Agnieszka Duczmal","@sortName":"Amadeus Chamber Orchestra of the Polish Radio, Duczmal, Agnieszka","url":"http://www.setlist.fm/setlists/amadeus-chamber-orchestra-of-the-polish-radio-agnieszka-duczmal-43c51f27.html"},{"@mbid":"5a8c082b-fed1-4b68-bfc9-046390ede18b","@name":"American Radio Flyer","@sortName":"American Radio Flyer","url":"http://www.setlist.fm/setlists/american-radio-flyer-6bd3ea2e.html"},{"@disambiguation":"","@mbid":"73aa4cb6-ab08-44d4-9b01-afd4f47305b6","@name":"American Radio Relay League","@sortName":"American Radio Relay League","url":"http://www.setlist.fm/setlists/american-radio-relay-league-1bc60d00.html"},{"@mbid":"7a22c46a-4ff3-4854-9317-5bd98acddab2","@name":"Amin PaYnE Presented By Radio Juicy","@sortName":"Amin PaYnE Radio Juicy","url":"http://www.setlist.fm/setlists/amin-payne-presented-by-radio-juicy-43df638f.html"},{"@disambiguation":"","@mbid":"c55a378e-6b5c-499e-a35e-d17ed607f9f3","@name":"Anthony Steel & The Radio Revellers","@sortName":"Anthony Steel & Radio Revellers, The","url":"http://www.setlist.fm/setlists/anthony-steel-and-the-radio-revellers-3bdbf03c.html"},{"@disambiguation":"","@mbid":"fa5b5152-f1a2-4c9e-af48-d0ac38ccfa5e","@name":"Radio Antics","@sortName":"Antics, Radio","url":"http://www.setlist.fm/setlists/radio-antics-23c56447.html"},{"@disambiguation":"","@mbid":"e24bce87-f3ca-4bb0-9d22-3e9d298ef7fd","@name":"Archives Radio France","@sortName":"Archives Radio France","url":"http://www.setlist.fm/setlists/archives-radio-france-13dc713d.html"},{"@disambiguation":"","@mbid":"aa3f1043-0fa5-470d-b0d6-d3818a1374ef","@name":"Archives de la radio géorgienne","@sortName":"Archives de la radio géorgienne","url":"http://www.setlist.fm/setlists/archives-de-la-radio-georgienne-6bdf7e2e.html"},{"@mbid":"19f42137-adcc-4227-b9c8-18a88fa19068","@name":"Julian Arguelles and The Frankfurt Radio Big Band","@sortName":"Arguelles, Julian and Frankfurt Radio Big Band, The","url":"http://www.setlist.fm/setlists/julian-arguelles-and-the-frankfurt-radio-big-band-13d5e109.html"},{"@mbid":"46330813-24dc-4e26-86b0-1d1ffc82f8ec","@name":"Asemieskuoro ja Radio-orkesteri","@sortName":"Asemieskuoro ja Radio-orkesteri","url":"http://www.setlist.fm/setlists/asemieskuoro-ja-radio-orkesteri-63d5d2bf.html"}]
     */

    private ArtistsBean artists;

    public ArtistsBean getArtists() {
        return artists;
    }

    public void setArtists(ArtistsBean artists) {
        this.artists = artists;
    }

    public static class ArtistsBean {
        @SerializedName("@itemsPerPage")
        private String itemsPerPage;
        @SerializedName("@page")
        private String page;
        @SerializedName("@total")
        private String total;
        /**
         * @mbid : d92b3e1f-999a-4348-9d82-d1905f295bdb
         * @name : !ngvar Presented By Radio Juicy
         * @sortName : !ngvar Radio Juicy
         * url : http://www.setlist.fm/setlists/ngvar-presented-by-radio-juicy-53df6389.html
         */

        private List<ArtistBean> artist;

        public String getItemsPerPage() {
            return itemsPerPage;
        }

        public void setItemsPerPage(String itemsPerPage) {
            this.itemsPerPage = itemsPerPage;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<ArtistBean> getArtist() {
            return artist;
        }

        public void setArtist(List<ArtistBean> artist) {
            this.artist = artist;
        }

        public static class ArtistBean {
            @SerializedName("@mbid")
            private String mbid;
            @SerializedName("@name")
            private String name;
            @SerializedName("@sortName")
            private String sortName;
            private String url;

            public String getMbid() {
                return mbid;
            }

            public void setMbid(String mbid) {
                this.mbid = mbid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSortName() {
                return sortName;
            }

            public void setSortName(String sortName) {
                this.sortName = sortName;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
