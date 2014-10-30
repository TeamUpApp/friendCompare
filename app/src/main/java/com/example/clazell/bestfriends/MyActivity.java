package com.example.clazell.bestfriends;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        //private TextSwitcher heading;
        private Button btnSearch;
        private String[] loveLetters;
        private int amountofLL;

        private TextView heading;

        public float numOfMatches = 0;
        public float unMatchedWords = 0;
        public int totalTexts = 0;

        public float totalWords;
        public float percent = 0;
        private ListView list;

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            list = (ListView) rootView.findViewById(R.id.listView);
            List<Contact> topContacts = ContactUtils.readMessages(getActivity());
            for (Contact c : topContacts) {
                c.setPercent(getPercentageMatch(sortFriendsWords(c.getNumber()), sortUserWords()));
            }
            Collections.sort(topContacts, new Comparator<Contact>() {
                public int compare(Contact c1, Contact c2) {
                    return c1.getPercent() > c2.getPercent() ? -1
                            : c1.getPercent() < c2.getPercent() ? 1 : 0;
                }
            });

            MessageListAdapter adapter = new MessageListAdapter(getActivity(), topContacts);
            list.setAdapter(adapter);
            //heading.setText("match " + numOfMatches + " no match " + unMatchedWords + " Percent: " + percent + "%");
            return rootView;
        }

        private String[] getTopWords(Context con) {

            String[] topwords = new String[25];

            Cursor cursor = con.getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
            String[] usersSMSbody = new String[cursor.getCount()];

            return topwords;
        }

        public float getPercentageMatch(Map<String, Word> fm, Map<String, Word> um) {

            Map<String, Word> countMap = new HashMap<String, Word>();

            Iterator iterator = fm.entrySet().iterator();
            for (Map.Entry<String, Word> entry : fm.entrySet()) {

                Word wordObj = um.get(entry.getKey());

                if (wordObj != null) {

                    wordObj = new Word();
                    wordObj.word = entry.getKey();
                    wordObj.count = 1;
                    countMap.put(entry.getKey(), wordObj);

                } else {
                    unMatchedWords++;
                }
            }
            numOfMatches = countMap.size();
            Iterator it = countMap.entrySet().iterator();
            for (Map.Entry<String, Word> entry : countMap.entrySet()) {
                Log.i("HEY ", "Key : " + entry.getKey() + " Value : "
                        + countMap.get(entry.getKey()).count);
            }
            totalWords = numOfMatches + unMatchedWords;
            Log.i("", "" + totalWords);
            Log.i("", "" + numOfMatches);
            Log.i("", "" + unMatchedWords);
            Log.i("", "" + numOfMatches / totalWords);
            return numOfMatches / totalWords * 100;

        }

        public Map<String, Word> sortFriendsWords(String match) {

            Map<String, Word> countMap = new HashMap<String, Word>();
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
            Log.i("cc ", " " + cursor.getCount());
            String line;
            String matchCheck;
            if (cursor.moveToFirst()) {

                for (int i = 0; i < cursor.getCount(); i++) {
                    line = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                    matchCheck = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    if (match.equalsIgnoreCase(matchCheck)) {

                        String[] words = line.split("\\s+");//"[^A-ZÃ…Ã„Ã–a-zÃ¥Ã¤Ã¶]+"
                        for (String word : words) {
                            if ("".equals(word)) {
                                continue;
                            }

                            Word wordObj = countMap.get(word);
                            if (wordObj == null) {
                                wordObj = new Word();
                                wordObj.word = word;
                                wordObj.count = 0;
                                countMap.put(word, wordObj);
                                // Log.i("WORD YO ",""+ word);
                            }
                            wordObj.count++;
                        }
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();

//            SortedSet<Word> sortedWords = new TreeSet<Word>(countMap.values());
//            int i = 0;
//            for (Word word : sortedWords) {
//                // Log.i("",""  +word.count +" "+ word.word);
//                i++;
//            }

            return countMap;
        }

        public Map<String, Word> sortUserWords() {
            Map<String, Word> countMap = new HashMap<String, Word>();
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);

            String line;

            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    line = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                    String[] words = line.split("[^A-ZÃ…Ã„Ã–a-zÃ¥Ã¤Ã¶]+");
                    for (String word : words) {
                        if ("".equals(word)) {
                            continue;
                        }

                        Word wordObj = countMap.get(word);
                        if (wordObj == null) {
                            wordObj = new Word();
                            wordObj.word = word;
                            wordObj.count = 0;
                            countMap.put(word, wordObj);
                        }

                        wordObj.count++;
                    }

                    cursor.moveToNext();
                }
            }
            cursor.close();


            SortedSet<Word> sortedWords = new TreeSet<Word>(countMap.values());
            int i = 0;
            for (Word word : sortedWords) {
                // Log.i("",""  +word.count +" "+ word.word);
                i++;
            }

            // Log.i("TOTAL",""  +sortedWords.size());
            return countMap;
        }
    }


}


