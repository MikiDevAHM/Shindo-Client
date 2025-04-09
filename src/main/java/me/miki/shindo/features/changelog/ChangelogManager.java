package me.miki.shindo.features.changelog;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.miki.shindo.helpers.multithreading.Multithreading;
import me.miki.shindo.helpers.network.HttpHelper;
import me.miki.shindo.helpers.network.JsonHelper;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChangelogManager {

	private CopyOnWriteArrayList<Changelog> changelogs = new CopyOnWriteArrayList<Changelog>();
	
	public ChangelogManager() {
		Multithreading.runAsync(() -> loadChangelog());
	}
	
	private void loadChangelog() {

		// Example at https://files.soarclient.com/data/changelog.json
		JsonObject jsonObject = HttpHelper.readJson("::github resources url::", null);
		
		if(jsonObject != null) {
			
			JsonArray jsonArray = JsonHelper.getArrayProperty(jsonObject, "changelogs");
			
			if(jsonArray != null) {
				
				Iterator<JsonElement> iterator = jsonArray.iterator();
				
				while(iterator.hasNext()) {
					
					JsonElement jsonElement = (JsonElement) iterator.next();
					Gson gson = new Gson();
					JsonObject changelogJsonObject = gson.fromJson(jsonElement, JsonObject.class);
					
					changelogs.add(new Changelog(JsonHelper.getStringProperty(changelogJsonObject, "text", "null"),
							ChangelogType.getTypeById(JsonHelper.getIntProperty(changelogJsonObject, "type", 999))));
				}
			}
		}
	}

	public CopyOnWriteArrayList<Changelog> getChangelogs() {
		return changelogs;
	}
}
