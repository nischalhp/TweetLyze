package models;

import java.net.URL;
import java.util.UUID;

public class UrlDTO {

	private URL url;
	private UUID trendId;

	public UrlDTO(URL url, UUID id) {

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
