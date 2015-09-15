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
    String string="";

    private static final String TAG = MGDExerciseActivity.class.getName();
    private static final String FILENAME = "myFile.txt";
    private Context context;

    public FileWriter(Game game) {
        context = game.getContext();
    }

    public void writeToFile(int levelCounter, int killCounter) {
        score2 = readFromFile();
        Calendar k = Calendar.getInstance();
        int day = k.get(Calendar.DAY_OF_MONTH);
        int month = k.get(Calendar.MONTH);
        int year = k.get(Calendar.YEAR);
        String date = day + "/" + month + "/" + year;
        System.out.println(score2[0][0]);
        if (score2[0][0] == nv)
        {
            System.out.println("Datei ist n.V");
            string = date + "," + levelCounter + "," + killCounter + ";";
            for (int z = 0; z < 9; z++)
            {
                string += nv + "," + nv + "," + nv + ";";
            }
        } else {
            for (int y = 9; y > -1; y--)
            {
                if (y == 9 && score2[y][1] == nv)
                {
                    score2[y][0] = date;
                    score2[y][1] = String.valueOf(levelCounter);
                    score2[y][2] = String.valueOf(killCounter);
                }
                else
                {
                    String[] min, temp = new String[3];
                    min = new String[3];
                    min[0] = date;
                    min[1] = String.valueOf(killCounter);
                    min[2] = String.valueOf(levelCounter);
                    if (score2[y][1] == nv)
                    {
                        temp = score2[y];
                        score2[y] = min;
                        score2[y + 1] = temp;
                    }
                    else
                    {
                        if(score2[y][1] == nv){
                            temp = score2[y];
                            score2[y] = min;
                            score2[y + 1] = temp;
                        }
                        else if (score2[y][1] != null)
                        {
                            int p = 1000;
                            try {
                                p = Integer.parseInt(score2[y][2]);
                            }catch (Exception e){

                            }
                            if (p <= levelCounter) {
                                temp = score2[y];
                                score2[y] = min;
                                score2[y + 1] = temp;
                            }
                        }
                    }
                }
            }

            for (int z = 0; z < 10; z++) {
                string = string + score2[z][0] + "," + score2[z][1] + "," + score2[z][2] + ";";
            }
        }
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



