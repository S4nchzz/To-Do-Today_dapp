package com.to_do_dapp.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ApiResponseReader {
    public static String getResponse(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder sb = new StringBuilder();
        try {
            String readIInIteration = reader.readLine();
            sb.append(readIInIteration);
            while (readIInIteration != null) {
                readIInIteration = reader.readLine();
                sb.append(readIInIteration);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
