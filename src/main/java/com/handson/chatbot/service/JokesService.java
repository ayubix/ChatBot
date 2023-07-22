package com.handson.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Random;


@Service
public class JokesService {

    @Autowired
    ObjectMapper om;


    public String getJoke(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder().url("https://api.chucknorris.io/jokes/search?query=" + keyword)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        JokesResponse jokesResponse = om.readValue(response.body().string(),JokesResponse.class);
        if(jokesResponse.getTotal() == 0)
            return "Sorry but the site has no jokes with this word ,try something else";
        return "An example joke with the word [" + keyword + "] :" + getRandomJokeByName(jokesResponse);
    }


    static class JokesResponse {
        int total;
        List<JokeObject> result;
        public int getTotal() {
            return total;
        }

        public List<JokeObject> getResult() {
            return result;
        }
    }

    static class JokeObject {

        String value;

        public String getValue() {
            return value;
        }
    }




    public String getRandomJokeByName(JokesResponse jokesResponse){
        int jokesCount = jokesResponse.getTotal();
        Random rand = new Random();
        int scope = rand.nextInt(jokesCount);
        return jokesResponse.getResult().get(scope).getValue();
    }





}
