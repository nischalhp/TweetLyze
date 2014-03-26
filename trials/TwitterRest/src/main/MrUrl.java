package main;

import java.net.URL;
import java.util.UUID;

public class MrUrl {

	private URL url;
	private UUID trendId;

	public MrUrl(URL url, UUID id) {

		this.url = url;
		this.trendId = id;
	}
	
	public URL getUrl(){
		return this.url; 
	}
	
	public UUID getId(){
		return this.trendId;
	}

}
