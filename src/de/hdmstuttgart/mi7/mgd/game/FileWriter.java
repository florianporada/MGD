package de.hdmstuttgart.mi7.mgd.game;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import de.hdmstuttgart.mi7.mgd.MGDExerciseActivity;

import java.io.*;
import java.util.Calendar;

/**
 * Created by florianporada on 14.09.15.
 */
public class FileWriter {

    //Global placeholder
    String nv = "-N.V.-";
    String[][] score2 = new String[10][3];

    private static final String TAG = MGDExerciseActivity.class.getName();
    private static final String FILENAME = "myFile.txt";
    private Context context;

    public FileWriter(Game game) {
        context = game.getContext();
    }

    public void writeToFile(String string) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(string);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }

    }

    public String[][] readFromFile() {
        String highscore = "";
        String[] scores;
        try {
            InputStream inputStream = context.openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            highscore = stringBuilder.toString();

            scores = highscore.split(";");
            if (scores.length < 10) {
                System.out.println("Score ist zu kurz");
                for (int i = 0; i < 10; i++) {
                    for (int y = 0; y < 3; y++) {
                        score2[i][y] = nv;
                    }
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    String[] temp = new String[3];
                    if (scores[i].split(",").length <3) {
                        System.out.println("temp ist zu kurz");
                        temp[0] = nv;
                        temp[1] = nv;
                        temp[2] = nv;
                    } else {
                        temp = scores[i].split(",");
                    }
                    for (int y = 0; y < 3; y++) {
                        score2[i][y] = temp[y];
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        if(score2[0][0] == null || score2[0][0] == nv){
            for (int i=0;i<10;i++){

                score2[i][0] = nv;
                score2[i][1] = nv;
                score2[i][2] = nv;

            }
        }
        return score2;
    }
}



