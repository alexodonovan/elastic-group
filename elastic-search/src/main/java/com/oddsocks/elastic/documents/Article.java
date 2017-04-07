package com.oddsocks.elastic.documents;

public class Article {

	private String id;

	private String title;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// @Field(type = FieldType.Nested)
	// private List<Author> authors;

}
