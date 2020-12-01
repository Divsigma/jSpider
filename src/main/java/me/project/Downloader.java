package me.project;

import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Downloader {

    private List<String> middlewares;

    final private String requestFunction = "handleRequest";

    final private String responseFunction = "handleResponse";

    Downloader(List<String> middlewares) {
        this.middlewares = middlewares;
    }

    public Object download(Request request) {

        Object result = this.throughRequest(request);
        if(result != null) {
            return result;
        }

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

            result = this.throughResponse(response);
            if(result != null) {
                return result;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }

    private Object throughRequest(Request request) {

        try {
            for(String name : this.middlewares) {
                Object result = Class.forName(name)
                        .getMethod(this.requestFunction, Request.class)
                        .invoke(Class.forName(name).newInstance(), request);
                if(result == null) {
                    continue;
                } else if(result.getClass() == Request.class) {
                    System.out.println("Stop at a new Request");
                    return result;
                } else if(result.getClass() == Response.class) {
                    System.out.println("Stop at a Response");
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    private Object throughResponse(Response response) {

        try {
            ListIterator<String> iterator = this.middlewares.listIterator(this.middlewares.size());

            while(iterator.hasPrevious()) {
                String name = iterator.previous();
                Object result = Class.forName(name)
                        .getMethod(this.responseFunction, Response.class)
                        .invoke(Class.forName(name).newInstance(), response);
                if(result == null) {
                    continue;
                } else if(result.getClass() == Request.class) {
                    System.out.println("Stop at a new Request");
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}
