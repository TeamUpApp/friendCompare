package com.example.clazell.bestfriends;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by nrobatmeily on 30/10/2014.
 */
public class ContactUtils {


    private Contact contact;

    public static String getContactName(String number, Map<String, String> cMap) {

        Map<String, String> contactMap = cMap;
        String contactName;
        contactName = contactMap.get(number);
        if (contactName != null) {
            return contactName;
        } else {
            return number;
        }
    }


    public static String GetCountryZipCode(Context context) {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    public static List<Contact> readMessages(Context context) {
        Map<String, String> contactMap = getContactList(context);
        Map<String, String> smsMap = new HashMap<String, String>();
        List<Contact> smsList = new LinkedList<Contact>();
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        String[] body = new String[cursor.getCount()];
        String[] number = new String[cursor.getCount()];
        String[] id = new String[cursor.getCount()];
        String[] date = new String[cursor.getCount()];



        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                id[i] = cursor.getString(cursor.getColumnIndexOrThrow("_id")).toString();
                body[i] = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                number[i] = cursor.getString(cursor.getColumnIndexOrThrow("address")).toString();
                date[i] = cursor.getString(cursor.getColumnIndexOrThrow("date")).toString();
                String name = getContactName(number[i], contactMap);
                if (!smsMap.containsKey(name)) {
                    smsMap.put(name, number[i]);
                }
                cursor.moveToNext();
            }
        }

        cursor.close();
        for (Map.Entry<String, String> entry : smsMap.entrySet()) {
            Contact contact = new Contact(entry.getKey(), entry.getValue(), (float) 0);
            smsList.add(contact);
        }

        return smsList;
    }


    public static Map getContactList(Context context) {
        String zip = "+" + GetCountryZipCode(context);
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        Map<String, String> countMap = new HashMap<String, String>();
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if (phoneNumber.startsWith("0")) {
                String codeNumber = zip + phoneNumber.substring(1);
                phoneNumber = codeNumber.replaceAll("\\s", "");
            }

            countMap.put(phoneNumber, name);
        }
        phones.close();

        return countMap;
    }
}
