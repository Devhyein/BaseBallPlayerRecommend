package com.ssafy.bigdata.dto;

public class SearchRequest {
    private String searchText;
    private String teams;
    private String positions;

    public SearchRequest() {
	}

	public SearchRequest(String searchText, String teams, String positions) {
		this.searchText = searchText;
		this.teams = teams;
		this.positions = positions;
	}

	@Override
	public String toString() {
		return "SearchRequest [positions=" + positions + ", searchText=" + searchText + ", teams=" + teams + "]";
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getTeams() {
		return teams;
	}

	public void setTeams(String teams) {
		this.teams = teams;
	}

	public String getPositions() {
		return positions;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}
    
}
