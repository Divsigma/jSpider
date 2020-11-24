package me.project;

import static org.junit.Assert.assertTrue;

import me.project.parser.DemoParser;
import me.project.parser.Parser;
import me.project.pipeline.ConsolePipeline;
import org.junit.Test;

import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

            new Spider()
                    .parser(new DemoParser(args[0], "utf-8"))
                    .pipeline(new ConsolePipeline())
                    .run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
