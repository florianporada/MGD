package de.hdmstuttgart.mi7.mgd.game;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import de.hdmstuttgart.mi7.mgd.MGDExerciseActivity;

import java.io.*;

/**
 * Created by florianporada on 14.09.15.
 */
public class FileWriter {

    private static final String TAG = MGDExerciseActivity.class.getName();
    private static final String FILENAME = "myFile.txt";
    private Context context;

    public FileWriter(Game game){
        context = game.getContext();
    }

    public void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }

    }

    public String readFromFile() {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(FILENAME);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }

        return ret;
    }


}
