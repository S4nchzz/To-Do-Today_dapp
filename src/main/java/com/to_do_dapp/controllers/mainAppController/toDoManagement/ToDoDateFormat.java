package com.to_do_dapp.controllers.mainAppController.toDoManagement;

import org.json.JSONObject;

public class ToDoDateFormat {
    private final JSONObject jsonObject;

    // Date spplited elements
    private String entireDate;
    
    private String ddmmyyy;
    private int yy;
    private int mm;
    private int dd;

    private String hhmmss;
    private int hh;
    private int mn;
    private int ss;

    public ToDoDateFormat(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        splitDate();
    }

    private void splitDate() {
        JSONObject jsonDate = new JSONObject(jsonObject.getString("date"));
        
        this.ddmmyyy = jsonDate.getString("date");
        this.hhmmss = jsonDate.getString("time");

        StringBuilder sb = new StringBuilder();

        sb.delete(0, sb.length());

        int delimiterYMDCount = 0;
        for (int k = 0; k < ddmmyyy.length(); k++) {
            sb.append(ddmmyyy.charAt(k));
            if (delimiterYMDCount == 0 && ddmmyyy.charAt(k + 1) == '-') {
                this.yy = Integer.valueOf(sb.toString());
                sb.delete(0, sb.length());
                delimiterYMDCount++;
                k++;
            } else if (delimiterYMDCount == 1 && ddmmyyy.charAt(k + 1) == '-') {
                this.mm = Integer.valueOf(sb.toString());
                sb.delete(0, sb.length());
                delimiterYMDCount++;
                k++;
            } else if (delimiterYMDCount == 2 && ddmmyyy.length() - 1 == k) {
                this.dd = Integer.valueOf(sb.toString());
                sb.delete(0, sb.length());
                delimiterYMDCount++;
            }
        }

        // Delete one time more the content 
        sb.delete(0, sb.length());

        int delimiterHMSCount = 0;
        for (int k = 0; k < hhmmss.length(); k++) {
            sb.append(hhmmss.charAt(k));
            if (delimiterHMSCount == 0 && hhmmss.charAt(k + 1) == ':') {
                this.hh = Integer.valueOf(sb.toString());
                sb.delete(0, sb.length());
                delimiterHMSCount++;
                k++;
            } else if (delimiterHMSCount == 1 && hhmmss.charAt(k + 1) == ':') {
                this.mn = Integer.valueOf(sb.toString());
                sb.delete(0, sb.length());
                delimiterHMSCount++;
                k++;
            } else if (delimiterHMSCount == 2 && hhmmss.length() - 1 == k) {
                this.ss = Integer.valueOf(sb.toString());
                sb.delete(0, sb.length());
                delimiterHMSCount++;
            }
        }

    }

    public String getEntireDate() {
        return entireDate;
    }

    public String getEntireDateJSONFormat() {
        JSONObject dateOnJson = new JSONObject();
        dateOnJson.put("date", ddmmyyy);
        dateOnJson.put("time", hhmmss);
        
        return dateOnJson.toString();
    }

    public String getYymmdd() {
        return ddmmyyy;
    }

    public int getYy() {
        return yy;
    }

    public int getMm() {
        return mm;
    }

    public int getDd() {
        return dd;
    }

    public String getHhmmss() {
        return hhmmss;
    }

    public int getHh() {
        return hh;
    }

    public int getMn() {
        return mn;
    }

    public int getSs() {
        return ss;
    }
}
