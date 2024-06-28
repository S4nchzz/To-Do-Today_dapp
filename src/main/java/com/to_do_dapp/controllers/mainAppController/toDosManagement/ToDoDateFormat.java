package com.to_do_dapp.controllers.mainAppController.toDosManagement;

import org.json.JSONObject;

public class ToDoDateFormat {
    private final JSONObject jsonObject;

    // Date spplited elements
    private String entireDate;
    
    private String yymmdd;
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
        this.entireDate = jsonObject.getString("Date");
        
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (entireDate.charAt(i) != ' ' && i < entireDate.length()) { 
            sb.append(entireDate.charAt(i));
            i++;
        }

        this.yymmdd = sb.toString();
        sb.delete(0, sb.length());
        
        i++; // Next position after the space ' '
        for (; i < entireDate.length(); i++) {
            sb.append(entireDate.charAt(i));
        }

        this.hhmmss = sb.toString();

        sb.delete(0, sb.length());

        int delimiterYMDCount = 0;
        for (int k = 0; k < yymmdd.length(); k++) {
            sb.append(yymmdd.charAt(k));
            if (delimiterYMDCount == 0 && yymmdd.charAt(k + 1) == '-') {
                this.yy = Integer.valueOf(sb.toString());
                sb.delete(0, sb.length());
                delimiterYMDCount++;
                k++;
            } else if (delimiterYMDCount == 1 && yymmdd.charAt(k + 1) == '-') {
                this.mm = Integer.valueOf(sb.toString());
                sb.delete(0, sb.length());
                delimiterYMDCount++;
                k++;
            } else if (delimiterYMDCount == 2 && yymmdd.length() - 1 == k) {
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

    public String getFormatForToDoElement() {
        switch (mm) {
            case 1:
                return dd + " JAN";
            case 2:
                return dd + " FEB";
            case 3:
                return dd + " MAR";
            case 4:
                return dd + " APR";
            case 5:
                return dd + " MAY";
            case 6:
                return dd + " JUN";
            case 7:
                return dd + " JUL";
            case 8:
                return dd + " AUG";
            case 9:
                return dd + " SEP";
            case 10:
                return dd + " OCT";
            case 11:
                return dd + " NOV";
            case 12:
                return dd + " DEC";

            default:
                return "...";
        }
    }

    public String getEntireDate() {
        return entireDate;
    }

    public String getYymmdd() {
        return yymmdd;
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
