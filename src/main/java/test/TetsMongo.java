package test;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;


public class TetsMongo {

	public static void main(String[] args) {
		
//Connection url -->
		String url = "mongodb://localhost:27017/config";
		
		/*
		 * BASIC OBJECTS OF Java MongoDB Driver:
		 * 							
		 * 1.MongoClient mongoClient;
		 * 2.MongoDBDatabase database;
		 * 3.MongoCollection collection;
		 * 4.Document document;
		 * 5.Bson bson;									
		 */
		
//MongoClient instance --> created using MongoClients which extracts connection settings fron url
		MongoClient mongoClient = MongoClients.create(url);
		
		//MongoIterable instance --> iterabel object
		MongoIterable<String> databaseIterable = mongoClient.listDatabaseNames();
		
		//iterable instance gets exhausted -> fill in array
		List<String> dbnames = new ArrayList<String>();
		for(String name : databaseIterable) {
			dbnames.add(name);
		}
		System.out.println("dbs = " + dbnames);
		
		
//MongoDatabase instance --> use this object to access, create, drop, read preferences
		MongoDatabase database = mongoClient.getDatabase("config");
		
//MongoCollection insatnce --> 
		MongoCollection<Document> collection = database.getCollection("IplData");
		
		
		//<-----------------BUILDERS---------------------------->
		
		/*
		 * Builder Classes:
		 * 
		 * 1.Filters
		 * 2.Projections
		 * 3.Sorts
		 * 4.Aggregation
		 * 5.Updates
		 * 6.Indexes
		 */
		
		
		//Filters------------->
		//import com.mongodb.client.model.Filters
		
		
		//using documents
		Document DFilter = new Document("label", "MI");
		Document DBuilder = (Document)collection.find(DFilter)
										.limit(1)
										.iterator()
										.tryNext();
		
		System.out.println(DBuilder.getString("city"));
		
		//using Bson
		Bson BFilter = eq("label","MI");
		Document BBuilder = collection.find(BFilter)
										.limit(1)
										.iterator()
										.tryNext();
		
		System.out.println(BBuilder.getString("city"));
		
		
		
		
		//using and
		
		Document Dfilter = new Document("label", new Document("$all", Arrays.asList("MI", "DC")));
		List<Document> DResults = new ArrayList<Document>();
		collection.find(DFilter)
					.into(DResults);
		
		for(Document d: DResults) {
			System.out.println(d);
		}
		
		
		System.out.println();
		//Projections-------------->
		
		//using Document
		Document DFilter2 = new Document("coach", "Mahela Jaywardene");
		Document DProjResult = collection.find(DFilter2)
										.limit(1)
										.projection(new Document("city", 1).append("name", 1))
										.iterator()
										.tryNext();
		
		
		System.out.println(DProjResult.getString("city"));
		System.out.println(DProjResult.getString("name"));
		System.out.println(DProjResult.getString("label")); //returns null as not projected
		
		
		//using Bson Document
		Bson BFilter2 = eq("coach", "Mahela Jaywardene");
		Document BProjResult = (Document)collection.find(BFilter2)
											.limit(1)
											.projection(fields(include("city","name")
													))
											.iterator()
											.tryNext();
		
		System.out.println(BProjResult.keySet().containsAll(Arrays.asList("_id", "city", "name")));
		
		
		
		//excludeId()------->
		
		Document BProjExcludeId =   collection.find(BFilter2)
	            								.limit(1)
	            								.projection(fields(include("title", "year"), excludeId()))
	            								.iterator()
	            								.tryNext();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

	}

}

