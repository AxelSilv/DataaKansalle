package org.axel.dataakansalle;

import java.util.ArrayList;

public class SearchHistoryStorage {
    private static SearchHistoryStorage instance;
    private final ArrayList<String> searchHistory = new ArrayList<>();

    public static SearchHistoryStorage getInstance() {
        if (instance == null) {
            instance = new SearchHistoryStorage();
        }
        return instance;
    }

    public void addSearch(String term) {
        if (!term.isEmpty()) {
            searchHistory.add(0, term);
            if (searchHistory.size() > 10) {
                searchHistory.remove(searchHistory.size() - 1);
            }
        }
    }

    public ArrayList<String> getSearchHistory() {
        return searchHistory;
    }
}