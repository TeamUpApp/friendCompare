package com.example.clazell.bestfriends;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextSwitcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.app.Fragment;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextSwitcher;
import java.util.HashMap;
import java.util.Iterator;
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

        private TextSwitcher heading;
        private Button btnSearch;
        private String[] loveLetters;
        private int amountofLL;

        public PlaceholderFragment() {


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);

            getMatches(sortFriendsWords("+61406677335"),sortUserWords());
            return rootView;
        }

        private String[] getTopWords(Context con){

            String[] topwords = new String[25];

            Cursor cursor = con.getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
            String[] usersSMSbody = new String[cursor.getCount()];

            return topwords;
        }

        private void getFriendsSMS() {
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);


            String[] friendsSMSbody = new String[cursor.getCount()];
            String[] usersSMSbody = new String[cursor.getCount()];

            String[] number = new String[cursor.getCount()];

            int amountOfSMS = 0;

            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    friendsSMSbody[i] = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();

                    number[i] = cursor.getString(cursor.getColumnIndexOrThrow("address"));

                    amountOfSMS++;
                    cursor.moveToNext();
                }
            }
            cursor.close();


        }

        public void getMatches(Map<String, Word> fm, Map<String, Word> um){

            Map<String, Word> countMap = new HashMap<String, Word>();

            Iterator iterator = fm.entrySet().iterator();
            for (Map.Entry<String, Word> entry : fm.entrySet()) {

                Word wordObj = um.get(entry.getKey());

                if (wordObj != null) {
//                        Word wordObj2 = countMap.get(wordObj);
//                        if (wordObj2 != null) {
//                            wordObj.count++;
//                        }else
                    {
                        wordObj = new Word();
                        wordObj.word = entry.getKey();
                        wordObj.count = 1;
                        countMap.put(entry.getKey(), wordObj);
                    }
                }
            }

            Iterator it = countMap.entrySet().iterator();
            for (Map.Entry<String, Word> entry : countMap.entrySet()) {
                Log.i("HEY ", "Key : " + entry.getKey() + " Value : "
                        + countMap.get(entry.getKey()).count);
            }
        }

        public  Map<String, Word> sortFriendsWords(String match){

            Map<String, Word> countMap = new HashMap<String, Word>();
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
            Log.i("cc "," " + cursor.getCount());
            String line;
            String matchCheck;
            if (cursor.moveToFirst()) {

                for (int i = 0; i < cursor.getCount(); i++) {
                    line = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                    matchCheck = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    // Log.i("check ", match + " " + matchCheck + " num "+cursor.getString(cursor.getColumnIndexOrThrow("address")));
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
//            Iterator iterator = countMap.entrySet().iterator();
//            for (Map.Entry<String, Word> entry : countMap.entrySet()) {
//                Log.i("HEY ","Key : " + entry.getKey() + " Value : "
//                        + countMap.get(entry.getKey()).count);
//            }
            cursor.close();

            SortedSet<Word> sortedWords = new TreeSet<Word>(countMap.values());
            int i = 0;
            for (Word word : sortedWords) {
                // Log.i("",""  +word.count +" "+ word.word);
                i++;
            }
            //Log.i("TOTAL",""  +countMap.size());

            return countMap;
        }

        public  Map<String, Word> sortUserWords(){
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


        public static String getContactName(Context context, String phoneNumber) {
            ContentResolver cr = context.getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
            if (cursor == null) {
                return null;
            }
            String contactName = null;
            if(cursor.moveToFirst()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }

            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            return contactName;
        }
    }

    public static class Word implements Comparable<Word>
    {
        String word;
        int count;

        @Override
        public int hashCode()
        {
            return word.hashCode();
        }

        @Override
        public boolean equals(Object obj)
        {
            return word.equals(((Word)obj).word);
        }

        @Override
        public int compareTo(Word b)
        {
            return b.count - count;
        }
    }

}


