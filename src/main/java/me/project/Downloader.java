package me.project;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Downloader {

    private String url;

    Downloader() {
    }

    public Response download(String url) {
        Response response = new Response();

        try {
            // 1. Create connection
            URLConnection connection = new URL(url).openConnection();
            // 2. Connect
            connection.connect();
            // 3. Get the results of header and body
            response.setHeader(connection.getHeaderFields());

            String encoding = connection.getContentEncoding(); // not this field
            StringBuffer htmlBuffer = new StringBuffer();
            if(encoding == null) encoding = "utf-8";
            response.setEncoding(encoding);

            Scanner in = new Scanner(connection.getInputStream(), encoding);
            while(in.hasNextLine()) {
                htmlBuffer.append(in.nextLine());
            }
            response.setHtml(htmlBuffer.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }

}
