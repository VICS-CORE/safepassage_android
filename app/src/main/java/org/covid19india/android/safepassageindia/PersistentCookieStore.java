package org.covid19india.android.safepassageindia;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class PersistentCookieStore implements CookieStore {
    private CookieStore store;

    public PersistentCookieStore(Context context) {
        store = new CookieManager().getCookieStore();
        SharedPreferences sf = context.getSharedPreferences("session_cookie", Context.MODE_PRIVATE);
//        String p=sf.getString("Set-Cookie","NA");
        Map<String, ?> allEntries = sf.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
    }

    @Override
    public void add(URI uri, HttpCookie httpCookie) {
        store.add(uri, httpCookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return store.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return store.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return store.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie httpCookie) {
        return store.remove(uri, httpCookie);
    }

    @Override
    public boolean removeAll() {
        return store.removeAll();
    }
}
