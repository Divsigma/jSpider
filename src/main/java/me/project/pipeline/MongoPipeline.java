package me.project.pipeline;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import me.project.item.Item;
import org.bson.Document;

public class MongoPipeline implements Pipeline {

    @Override
    public void process(Item item) {

        MongoClient mongo = MongoClients.create("mongodb://127.0.0.1:27017");
        MongoCollection<Document> collection = mongo.getDatabase("jspider_test").getCollection("passage");
        collection.insertOne(Document.parse(item.toString()));
        System.out.println("== Finish inserting item ==");

        Document doc = collection.find(Filters.eq("id", 1)).first();
        if(doc != null) {
            System.out.println(doc.toJson());
        } else {
            System.err.println("FAIL TO GET DOC with id equaling 1");
        }


    }

}
