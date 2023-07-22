package com.handson.chatbot.service;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





@Service
public class ChessService {


    final String[] CHESS_TOPICS = new String[]{"opening","middle game","endgames","tactics","strategy","master games","pawns","king","positional game","gambits"};







    public static final Pattern CHESS_PATTERN = Pattern.compile("<h3 class=\\\"course-title\\\">([^<]+)<\\/h3>[\\s\\S]*?<p class=\\\"course-desc\\\">([^<]+)<\\/p>[\\s\\S]*?<span class=\\\"icon-font-chess course-info-icon lessons\\\"><\\/span>\\s*(.*?)\\s*Lessons");

    public String searchCourse(String keyword) throws IOException {
        if(isKeywordValid(keyword) == false)
            return "Sorry but your keyword does not match any chess course , try again with new keyword related to one of those topics  : opening, middle , endgames ,tactics ,strategy";
        return "Chess.com courses in " + keyword + ":   " + parseCoursesHtml(getCoursesHtml(keyword));
    }



    public boolean isKeywordValid(String keyword){
        boolean result = false;
        int size = CHESS_TOPICS.length;
        for (int i = 0; i < size ; i++) {
            if(keyword.equals(CHESS_TOPICS[i])){
                result = true;
                break;
            }
        }

        return result;

    }



    private String parseCoursesHtml(String html) {
        String res = "";
        Matcher matcher = CHESS_PATTERN.matcher(html);
        while (matcher.find()) {
            res += "Course title : " + matcher.group(1) + " , Course description : " + matcher.group(2) + ", number of lessons : " + matcher.group(3) + "\n";
        }
        return res;
    }





    private String getCoursesHtml(String keyword) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://www.chess.com/lessons/all-lessons?keyword="+keyword)
                .method("GET", null)
                .addHeader("authority", "www.chess.com")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .addHeader("accept-language", "he-IL,he;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("cache-control", "max-age=0")
                .addHeader("cookie", "visitorid=%3Af89b%3Affff%3A217.132.135.237; usprivacy=1---; ad_clicker=false; _cc_id=9e2f97c9f4401d3727ecaf901eb72c76; _pxvid=139b355e-2b07-11ed-8bef-434d6256764c; cf_clearance=riEvjq5oYBfuDFUNFIiEx51QJEV865z5Q6.KY3u8lkE-1662154503-0-150; me=%7B%22deviceId%22%3A%22dde6fcc9-b550-465d-9da1-1d6c37c7f632R%22%7D; cto_bundle=pS2YuV9lMHMyJTJGRjFBSXVBM2h2RmNyRjdqUlA3V0lPWllQbThEWVplS1ZUVFByJTJGbmklMkJnTUZGeWslMkJzRXRYZkR0NGRaSUlHZGZFWllJT2ZTYnR4QkZmZGolMkJJYyUyQmlBa2Q1R3NsVkZoSlM5ZFElMkY2NGxlNlpmekwzSGd0clZBelJleDFsRkxTOG5wWkJsMTFiUnlnRlZCRXFjdGdJQSUzRCUzRA; amplitude_id_5cc41a1e56d0ee35d4c85d1d4225d2d1chess.com=eyJkZXZpY2VJZCI6ImRkZTZmY2M5LWI1NTAtNDY1ZC05ZGExLTFkNmMzN2M3ZjYzMlIiLCJ1c2VySWQiOiIxNjE1Njc1MzciLCJvcHRPdXQiOmZhbHNlLCJzZXNzaW9uSWQiOjE2ODE3MjI5OTY0MzMsImxhc3RFdmVudFRpbWUiOjE2ODE3MjQzMTgyOTksImV2ZW50SWQiOjYsImlkZW50aWZ5SWQiOjEsInNlcXVlbmNlTnVtYmVyIjo3fQ%3D%3D; _pw_fingerprint=%22b0c3591d4666b32f5f54578460f83982%22; _awl=2.1687901490.5-6f5fdfcfdfc7daf9e4bdff80b03c6386-6763652d6575726f70652d7765737431-0; _pbjs_userid_consent_data=3524755945110770; _sharedid=fc6bf214-3ada-45d3-85ff-be10a073afe7; _gid=GA1.2.1932965266.1688673428; GCLB=CLPmq6OVpKm_pgE; PHPSESSID=254cfff7c76cc45dc482f06458ff6a24; amp_5cc41a=dde6fcc9-b550-465d-9da1-1d6c37c7f632R...1h5hu8b8f.1h5hu8b8l.19a.kb.1tl; asset_push=20230717033401%3B5fe99%2C76718%2Ce20d2; __cf_bm=3bvunncMI2lVUt_mMfyJn173Vb8CVyCrYO4ZPEM3O4U-1689600455-0-AevKz5p7r+Vei9i7ZhuRQsTdSPcujGvY6AVf8vvtKavHkYdTMfw+WB02y5ahM/AGMP4WBkvCsjaUXPPs55B4WLRKfIp+BFU0n3F+yz2f22Mq; ATTRIBUTION_V1=%7B%22initialAttribution%22%3A%7B%22source%22%3A%22unknown%22%2C%22medium%22%3A%22unknown%22%2C%22campaign%22%3Anull%2C%22term%22%3Anull%2C%22content%22%3Anull%2C%22route%22%3A%22%5C%2F%22%2C%22referer%22%3A%22unknown%22%2C%22version%22%3A%221.0.0%22%2C%22createDateTime%22%3A%221659952988%22%7D%2C%22lastAttribution%22%3A%7B%22source%22%3A%22unknown%22%2C%22medium%22%3A%22unknown%22%2C%22campaign%22%3Anull%2C%22term%22%3Anull%2C%22content%22%3Anull%2C%22route%22%3A%22%5C%2Flessons%5C%2Fall-lessons%3Fkeyword%3Dtactics%22%2C%22referer%22%3A%22unknown%22%2C%22version%22%3A%221.0.0%22%2C%22createDateTime%22%3A%221689600468%22%7D%7D; _ga=GA1.1.901944735.1651158490; _ga_Q0CBHRQJH8=GS1.1.1689600462.645.1.1689600467.55.0.0; _ga_NP7V31R49N=GS1.1.1689600467.324.0.1689600467.0.0.0; ATTRIBUTION_V1=%7B%22initialAttribution%22%3A%7B%22source%22%3A%22unknown%22%2C%22medium%22%3A%22unknown%22%2C%22campaign%22%3Anull%2C%22term%22%3Anull%2C%22content%22%3Anull%2C%22route%22%3A%22%5C%2F%22%2C%22referer%22%3A%22unknown%22%2C%22version%22%3A%221.0.0%22%2C%22createDateTime%22%3A%221659952988%22%7D%2C%22lastAttribution%22%3A%7B%22source%22%3A%22unknown%22%2C%22medium%22%3A%22unknown%22%2C%22campaign%22%3Anull%2C%22term%22%3Anull%2C%22content%22%3Anull%2C%22route%22%3A%22%5C%2Flessons%5C%2Fall-lessons%3Fkeyword%3Dendgames%22%2C%22referer%22%3A%22unknown%22%2C%22version%22%3A%221.0.0%22%2C%22createDateTime%22%3A%221689600661%22%7D%7D; __cf_bm=djmY9jI0V8BQ7cmlQqCs.eHkruabgSkXWsSI2ySPlkc-1689600662-0-AX8S2Yj5BmwBNZFkndJI6CQbxHRH7B31m3S7GVrFTyAgtkdVYAdYxLL0X6XCtyNQ3zBR8vmov5rmH6aljV0C9sschQdmBCTmYOGChbZxp3yt; amplitude_id_5cc41a1e56d0ee35d4c85d1d4225d2d1chess.com=eyJkZXZpY2VJZCI6ImRkZTZmY2M5LWI1NTAtNDY1ZC05ZGExLTFkNmMzN2M3ZjYzMlIiLCJ1c2VySWQiOiIxNjE1Njc1MzciLCJvcHRPdXQiOmZhbHNlLCJzZXNzaW9uSWQiOjE2ODE3MjI5OTY0MzMsImxhc3RFdmVudFRpbWUiOjE2ODE3MjQzMTgyOTksImV2ZW50SWQiOjYsImlkZW50aWZ5SWQiOjEsInNlcXVlbmNlTnVtYmVyIjo3fQ%3D%3D; me=%7B%22deviceId%22%3A%22dde6fcc9-b550-465d-9da1-1d6c37c7f632R%22%7D; asset_push=20230717033401%3B5fe99%2C76718%2Ce20d2; visitorid=%3Af89b%3Affff%3A217.132.135.237")
                .addHeader("sec-ch-ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "none")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}