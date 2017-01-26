package com.mantovani.alarmesms;

import org.json.*;
import java.util.ArrayList;

/**
 * Created by Mantovani on 25-Jan-17.
 *
 * Defines a rule for triggering the alarm when receiving a SMS
 */

public class Rule {

    private ArrayList<String> listOfSenders;
    private ArrayList<String> listOfPatterns;

    public Rule() {

    }

    public Rule(String json) {
        addFromJsonString(json);
    }

    public void addSender(String sender) {
        listOfSenders.add(sender);
    }

    public void addPattern(String pattern) {
        listOfPatterns.add(pattern);
    }

    public void clearRules() {
        listOfPatterns.clear();
        listOfSenders.clear();
    }

    /**
     * Checks if the sender and the message of the SMS received match this rule's criteria
     * @param sender telephone number of the SMS sender
     * @param message SMS body message
     * @return true if sender and message are contained in the rule
     */
    public boolean matchesCriteria(String sender, String message) {
        if (listOfSenders.contains(sender)) {
            for (String pattern : listOfPatterns) {
                // Checks if pattern is a substring of the message
                if (message.contains(pattern)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Converts the internal attributes to a string representation of the JSON
     * @return string representation of the JSON
     */
    public String convertToJsonString() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray arrayOfSenders = new JSONArray(listOfSenders);
            JSONArray arrayOfPatterns = new JSONArray(listOfPatterns);

            jsonObject.put("listOfSenders", arrayOfSenders);
            jsonObject.put("listOfPatterns", arrayOfPatterns);
            return jsonObject.toString(2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets a string representation of a JSON and add it to the internal object attributes
     * @param json a string representation of a json
     */
    public void addFromJsonString(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray arrayOfSenders = jsonObject.getJSONArray("listOfSenders");
            listOfSenders.addAll(getAllJsonArrayElements(arrayOfSenders));

            JSONArray arrayOfPatterns = jsonObject.getJSONArray("listOfPatterns");
            listOfPatterns.addAll(getAllJsonArrayElements(arrayOfPatterns));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Iterate through an JSONArray to extract all string values
     * @param array an JSONArray element
     * @return an array of strings
     */
    private ArrayList<String> getAllJsonArrayElements(JSONArray array) throws JSONException {

        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }

        return list;
    }
}
