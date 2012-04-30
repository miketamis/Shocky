package pl.shockah.shocky.sql;

import pl.shockah.HTTPQuery;
import pl.shockah.JSONObject;
import pl.shockah.shocky.Data;

public class SQL {
	public static String raw(String query) {return queryRaw(query);}
	public static JSONObject select(QuerySelect query) {return queryJSON(query.getSQLQuery());}
	public static String insert(QueryInsert query) {return queryInsertId(query.getSQLQuery());}
	public static String delete(QueryDelete query) {return queryRaw(query.getSQLQuery());}
	
	public static String queryRaw(String query) {
		HTTPQuery q = new HTTPQuery(Data.config.getString("main-sqlurl")+"?"+HTTPQuery.parseArgs("type","raw","q",query),"GET");
		q.connect(true,false);
		String s = q.readWhole();
		q.close();
		return s;
	}
	public static String queryInsertId(String query) {
		HTTPQuery q = new HTTPQuery(Data.config.getString("main-sqlurl")+"?"+HTTPQuery.parseArgs("type","insertid","q",query),"GET");
		q.connect(true,false);
		String s = q.readWhole();
		q.close();
		return s;
	}
	public static JSONObject queryJSON(String query) {
		HTTPQuery q = new HTTPQuery(Data.config.getString("main-sqlurl")+"?"+HTTPQuery.parseArgs("type","json","q",query),"GET");
		q.connect(true,false);
		JSONObject j = JSONObject.deserialize(q.readWhole());
		q.close();
		return j;
	}
}