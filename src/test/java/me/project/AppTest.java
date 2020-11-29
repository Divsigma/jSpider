package me.project;

import static org.junit.Assert.assertTrue;

import me.project.parser.DemoParser;
import me.project.parser.Parser;
import me.project.pipeline.ConsolePipeline;
import me.project.pipeline.MongoPipeline;
import org.junit.Test;

import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
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
        if (args.length == 0) {
            System.out.println("Please input url as an argument");
            return;
        }

        try {

            List<String> downloaderMiddleware = new LinkedList<>(Arrays.asList(
                    "me.project.middleware.SetCookie",
                    "me.project.middleware.SetUserAgent"
            ));

            new Spider()
                    .parser(new DemoParser(args[0], "utf-8"))
                    .setDownloaderMiddleware(downloaderMiddleware)
                    .pipeline(new ConsolePipeline())
                    .run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
