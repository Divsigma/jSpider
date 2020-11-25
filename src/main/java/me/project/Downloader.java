package me.project;

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
            //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
            //connection.setRequestProperty("Referer", "https://www.cnblogs.com/");
            //connection.setRequestProperty("cookie", "_ga=GA1.2.156721159.1596030976; sc_is_visitor_unique=rx11857110.1596502132.D9ACCEBDD2C54F87B5C95DEB7CA3A307.1.1.1.1.1.1.1.1.1; UM_distinctid=173d8b765e7226-0708cbe4462b14-3323765-144000-173d8b765e8214; CNZZDATA1265203196=2090931422-1597067675-https%253A%252F%252Fwww.baidu.com%252F%7C1597067675; Hm_lvt_39b794a97f47c65b6b2e4e1741dcba38=1601287552; CNZZDATA4606621=cnzz_eid%3D1446730251-1603633150-https%253A%252F%252Fwww.google.com%252F%26ntime%3D1603633150; CNZZDATA1274015536=475676864-1603758599-https%253A%252F%252Fwww.baidu.com%252F%7C1603758599; __utma=226521935.156721159.1596030976.1603674476.1603935666.2; __utmz=226521935.1603935666.2.2.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; Hm_lvt_fa5cea7cd463480c76a420af9fa8973e=1604021838; Hm_lvt_e5efabe75b1828de14e598fc53ceb2f9=1604230360,1604486489; CNZZDATA1276603899=1579172752-1604542399-https%253A%252F%252Fwww.google.com%252F%7C1604542399; __gads=ID=1e81aa8a44e3a123:T=1596783683:R:S=ALNI_MbZFJ1mD-GB_J4_PFcRWwLG_oEpmA; _gid=GA1.2.594657533.1606219854");
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
