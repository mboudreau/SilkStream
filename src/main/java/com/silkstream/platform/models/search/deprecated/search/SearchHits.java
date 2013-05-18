package com.silkstream.platform.models.search.deprecated.search;


public class SearchHits {
	private int found;
    private int start;
    private SearchHit[] hit;

    public int getFound() {
        return found;
    }

    public void setFound(int found) {
        this.found = found;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public SearchHit[] getHit() {
        return hit;
    }

    public void setHit(SearchHit[] hit) {
        this.hit = hit;
    }
}
