package me.project;

import static org.junit.Assert.assertTrue;

import me.project.parser.DemoParser;
import me.project.parser.Parser;
import me.project.pipeline.ConsolePipeline;
import me.project.pipeline.MongoPipeline;
import me.project.scheduler.QueueScheduler;
import org.junit.Test;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    public static void main(String[] args )
    {


        try {

            URLConnection connection = new URL("https://baike.baidu.com/wikitag/api/getlemmas").openConnection();

            connection.setDoOutput(true);

            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            /*connection.setRequestProperty("limit", "24");
            connection.setRequestProperty("timeout", "3000");
            connection.setRequestProperty("filterTags", "[]");
            connection.setRequestProperty("tagId", "76606");
            connection.setRequestProperty("fromLemma", "false");
            connection.setRequestProperty("contentLength", "40");
            connection.setRequestProperty("page", "1");*/

            connection.connect();

            String body = "limit=24&timeout=3000&filterTags=%5B%5D&tagId=76606&fromLemma=false&contentLength=40&page=1";
            OutputStream os = connection.getOutputStream();
            os.write(body.getBytes(StandardCharsets.UTF_8));

            Scanner in = new Scanner(connection.getInputStream(), "utf-8");
            while(in.hasNextLine()) {
                System.out.println(in.nextLine());
            }
            in.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
