package me.project;

import java.io.PrintWriter;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        if(args.length == 0) {
            System.out.println("Please input url as an argument");
            return;
        }

        try {

            // 1. Create connection
            URL url = new URL(args[0]);
            URLConnection connection = url.openConnection();

            // 2. Connect
            connection.connect();

            // 3. Show the results of header
            Map<String, List<String>> headerFields = connection.getHeaderFields();

            for(Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                System.out.print(entry.getKey() + ": ");
                for(String item : entry.getValue()) {
                    System.out.print(item + " ");
                }
                System.out.println();
            }

            // 4. Show the content and write it to a file
            System.out.println();
            String encoding = connection.getContentEncoding();
            if(encoding == null) {
                encoding = "utf-8";
            }
            PrintWriter out = new PrintWriter("D:\\JavaProject\\jSpider\\testPage", "utf-8");
            Scanner in = new Scanner(connection.getInputStream(), encoding);
            for(int i = 0; i < 20 && in.hasNextLine(); i++) {
                String line = in.nextLine();
                System.out.println(line);
                out.println(line);
            }
            // flush the buffered string into file ! Else it comes out nothing !
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
