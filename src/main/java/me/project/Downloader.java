package me.project;

import me.project.passable.Request;
import me.project.passable.Response;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Downloader {

    Downloader() {
    }

    public Response download(Request request) {

        Response response = new Response();

        try {
            // 1. Create connection
            String url = request.getUrl();
            URLConnection connection = new URL(url).openConnection();
            // 2. Connect
            for(Map.Entry<String, String> entry : request.getHeader().entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            System.out.println("== Request Header ==");
            Map<String, List<String>> requestHeader = connection.getRequestProperties();
            for(Map.Entry<String, List<String>> entry : requestHeader.entrySet()) {
                System.out.print(entry.getKey() + ": ");
                for (String item : entry.getValue()) {
                    System.out.print(item + " ");
                }
                System.out.println();
            }

            connection.connect();

            // 3. Get the results of header and body
            response.setHeader(connection.getHeaderFields());

            String encoding = connection.getContentEncoding(); // not this field
            StringBuilder htmlBuilder = new StringBuilder();
            if(encoding == null) encoding = "utf-8";
            response.setEncoding(encoding);

            Scanner in = new Scanner(connection.getInputStream(), encoding);
            while(in.hasNextLine()) {
                htmlBuilder.append(in.nextLine());
            }
            in.close();

            response.setHtml(htmlBuilder.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }

}
