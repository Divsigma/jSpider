package me.project;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Downloader {

    private List<String> middlewares;

    final private String requestFunction = "handleRequest";

    final private String responseFunction = "handleResponse";

    Downloader(List<String> middlewares) {
        this.middlewares = middlewares;
    }

    public Object download(Request request) {

        // 1. Process request through middlewares,
        // it will come out three cases:
        //   (1) process Request in place successfully and return null
        //   (2) get a new Request
        //   (3) get a Response
        // only when it return null do the download keep going
        Object result = this.throughRequest(request);
        if(result != null) {
            return result;
        }

        Response response = new Response(request);

        try {
            // 2. Create connection
            URLConnection connection = new URL(request.getUrl()).openConnection();

            // 3. Connect (and send POST data if needed)
            for(Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            /*
            System.out.println("== Request Header ==");
            Map<String, List<String>> requestHeader = connection.getRequestProperties();
            for(Map.Entry<String, List<String>> entry : requestHeader.entrySet()) {
                System.out.print(entry.getKey() + ": ");
                for (String item : entry.getValue()) {
                    System.out.print(item + " ");
                }
                System.out.println();
            }*/
            if(request.getMethod().equals(Request.Method.POST)) {
                connection.setDoOutput(true);
            }

            connection.connect();

            if(request.getMethod().equals(Request.Method.POST)) {
                String body = request.getBodyString();
                OutputStream os = connection.getOutputStream();
                //
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            // 4. Get Response -- process headers:
            // status code, content-encoding and other header fields
            response.setStatus(Integer.parseInt(connection.getHeaderField(0).split(" ")[1]));

            for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
                if(entry.getKey() != null) {
                    StringJoiner joiner = new StringJoiner("; ");
                    for (String item : entry.getValue()) {
                        joiner.add(item);
                    }
                    response.setHeaderField(entry.getKey(), joiner.toString());
                }
            }

            String[] type = response.getHeaderField("Content-Type").split("charset=");
            if(type.length > 1) {
                response.setCharset(type[1]);
            } else {
                response.setCharset("utf-8");
            }

            // 5. Get Response -- parse body data
            String charset = response.getCharset();
            if(charset.equals("utf-8") || charset.equals("UTF-8")) {
                StringBuilder htmlBuilder = new StringBuilder();
                Scanner in = new Scanner(connection.getInputStream(), charset);
                while(in.hasNextLine()) {
                    htmlBuilder.append(in.nextLine());
                }
                in.close();
                response.setHtml(htmlBuilder.toString());
            } else {
                System.out.println("IT WAS LEFT OUT !!!");
            }

            // 6. Get Response -- process response through middlewares
            // it will come out two cases:
            //   (1) process Response in place successfully and return null
            //   (2) get a new Request
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
                    System.err.println("Stopped at a new Request (from Request) ...");
                    return result;
                } else if(result.getClass() == Response.class) {
                    System.err.println("Stopped at a Response (from Request) ...");
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
                    System.err.println("Stopped at a new Request (from Response) ...");
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}
