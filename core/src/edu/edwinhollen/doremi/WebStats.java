package edu.edwinhollen.doremi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Edwin Hollen on 4/2/16.
 */
public class WebStats {
    private final static String baseUrl = "http://www.doremi.dev";
    private final static class Server{
        public final static String submitTime = "submitTime";
        public final static String newPlayer = "newPlayer";
    }
    private final static Json json = new Json();

    public static void submitTime(final PuzzleStatistics puzzleStatistics){
        if(DoReMi.preferences.getString("player_id", null) == null || DoReMi.preferences.getString("player_key", null) == null){
            newPlayer(new Runnable() {
                @Override
                public void run() {
                    submitTime(puzzleStatistics);
                }
            });
            return;
        }

        class SubmitTimeResponse{
            String success, error;
        }

        class SubmitTimeRequest{
            String player_id, player_key, difficulty;
            long start_time, end_time;

            public SubmitTimeRequest(String player_id, String player_key, String difficulty, long start_time, long end_time) {
                this.player_id = player_id;
                this.player_key = player_key;
                this.difficulty = difficulty;
                this.start_time = start_time;
                this.end_time = end_time;
            }
        }

        makeRequest(Server.submitTime, json.toJson(new SubmitTimeRequest(
                DoReMi.preferences.getString("player_id"),
                DoReMi.preferences.getString("player_key"),
                Puzzle.Difficulty.valueOf(DoReMi.preferences.getString("difficulty")).toString().toLowerCase(),
                puzzleStatistics.getStartTime(),
                puzzleStatistics.getEndTime()
        )), new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                SubmitTimeResponse response = json.fromJson(SubmitTimeResponse.class, httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }

    public static void newPlayer(final Runnable callback){
        class NewPlayerResponse{
            private String player_id;
            private String player_key;
        }
        makeRequest(Server.newPlayer, null, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                NewPlayerResponse r = json.fromJson(NewPlayerResponse.class, httpResponse.getResultAsString());
                System.out.println(String.format("New player %s", r.player_id));

                DoReMi.preferences.putString("player_id", r.player_id);
                DoReMi.preferences.putString("player_key", r.player_key);

                callback.run();
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }

    private static void makeRequest(final String to, final String content, final Net.HttpResponseListener listener){
        (new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequestBuilder builder = new HttpRequestBuilder();
                Net.HttpRequest request = builder.newRequest()
                        .method(Net.HttpMethods.GET)
                        .url(String.format("%s/%s", baseUrl, to))
                        .content(content)
                        .build();
                Gdx.net.sendHttpRequest(request, listener);
            }
        })).run();


    }

    private static String getPlayerId(){
        return DoReMi.preferences.getString("player_id");
    }

    private static String getPlayerKey(){
        return DoReMi.preferences.getString("player_key");
    }
}
