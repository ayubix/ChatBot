package com.handson.chatbot.controller;

import com.handson.chatbot.service.AmazonService;
import com.handson.chatbot.service.ChessService;
import com.handson.chatbot.service.JokesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.HashMap;

@Service
@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    AmazonService amazonService;

    @Autowired
    JokesService jokesService;




    @Autowired
    ChessService chessService;


    @RequestMapping(value = "/chess", method = RequestMethod.GET)
    public ResponseEntity<?> getChessInfo(@RequestParam String keyword) throws IOException {
        return new ResponseEntity<>(chessService.searchCourse(keyword), HttpStatus.OK);
    }







    @RequestMapping(value = "/amazon", method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@RequestParam String keyword) throws IOException {
        return new ResponseEntity<>(amazonService.searchProducts(keyword), HttpStatus.OK);
    }




    @RequestMapping(value = "/jokes", method = RequestMethod.GET)
    public ResponseEntity<?> getJokes(@RequestParam String keyword) throws IOException {
        return new ResponseEntity<>(jokesService.getJoke(keyword), HttpStatus.OK);
    }




    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> getBotResponse(@RequestBody BotQuery query) throws IOException {
        HashMap<String, String> params = query.getQueryResult().getParameters();
        String res = "Not found";
        if (params.containsKey("keyword")) {
            res = jokesService.getJoke(params.get("keyword"));
        } else if (params.containsKey("product")) {
            res = amazonService.searchProducts(params.get("product"));
        }
            else if(params.containsKey("courseTopic")) {
                res = chessService.searchCourse(params.get("courseTopic"));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }




    static class BotQuery {
        QueryResult queryResult;


        public QueryResult getQueryResult() {
            return queryResult;
        }
    }



    static class QueryResult {
        HashMap<String, String> parameters;

        public HashMap<String, String> getParameters() {
            return parameters;
        }
    }











}