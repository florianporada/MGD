package de.hdmstuttgart.mi7.mgd.game;

import android.content.Context;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Calendar;

/**
 * Created by Christoph on 13.09.2015.
 */
public class Filestuff {
    private Game game;
    String[][] score2 = new String[10][3];
    String string = "";

    public Filestuff(Game game){
        this.game= game;
    }


    public String[][] getScore(){
        String highscore="";
        String[] scores;

        try {
            FileInputStream fIn = game.getContext().openFileInput("drivingsim2");
            FileChannel channel = fIn.getChannel();
            if (channel.size() == 0) {
                String a = "--N.V.--";
<<<<<<< HEAD
                for (int i=0;i<10;i++){
=======
                for (int i= 0;i<10;i++){
>>>>>>> 255c09b974fd06b8bc1ce8f852152a34d93a13b4
                    score2[i][0] = a;
                    score2[i][1] = a;
                    score2[i][2] = a;

                }
            } else {
                FileInputStream inputStream = game.getContext().openFileInput("drivingsim2");
                int c;
                while( (c = inputStream.read()) != -1){
                    highscore = highscore + Character.toString((char)c);
                }
                scores = highscore.split(";");
                if(scores.length==0){
                    for (int i=0;i<10;i++){
                        for (int y=0;y<3;y++){
                            score2[i][y]="--N.V.--";
                        }
                    }
                }else{
                    for (int i= 0;i<10;i++){
                        String[] temp = new String[3];
                        if (scores[i].split(",").length<=3){
                            temp[0] = "--N.V.--";
                            temp[1] = "--N.V.--";
                            temp[2] = "--N.V.--";
                        }else{
                            temp = scores[i].split(",");
                        }
                        for (int y = 0; y<temp.length;y++){
                            score2[i][y] = temp[y];
                        }
                    }
                }
            inputStream.close();
            }} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return score2;
    }
<<<<<<< HEAD
    public void setScore(int levelCounter, int pionts)
    {
        Calendar k = Calendar.getInstance();
        int day = k.get(Calendar.DAY_OF_MONTH);
        int month = k.get(Calendar.MONTH);
        int year = k.get(Calendar.YEAR);
        String date = day + "/" + month + "/" + year;
        if(score2[0][0]=="--N.V.--"){
            for (int y = 0; y < 10; y++)
            {
                if (y == 0 && score2[y][1]=="--N.V.--")
                {
=======
    public void setScore(int levelCounter, int pionts) {
        for (int y = 0; y < 10; y++) {

            if (score2[y][1] == "--N.V.--") {
                Calendar k = Calendar.getInstance();
                int day = k.get(Calendar.DAY_OF_MONTH);
                int month = k.get(Calendar.MONTH);
                int year = k.get(Calendar.YEAR);
                String date = day + "/" + month + "/" + year;
                if (y == 0) {
>>>>>>> 335dc3abac853ad9331787ea8af10355a70f4b90
                    score2[y][0] = date;
                    score2[y][1] = String.valueOf(pionts);
                    score2[y][2] = String.valueOf(levelCounter);
                } else
                {
                    String[] min, temp;
                    min = score2[y];
                    if (score2[y][1] == "--N.V.--")
                    {
                        temp = score2[y];
                        score2[y] = min;
                        score2[y-1] = temp;
                    }else
                    {
                        int b = 0;
                        for (int i = 0; i < 10; i++) {
                            if (Integer.parseInt(score2[i][1]) < pionts) {
                                min = score2[i];
                                b = i;

                            }
                        }
                        if (score2[b][1] != score2[y][1]) {
                            temp = score2[y];
                            score2[y] = min;
                            score2[b] = temp;
                        }
                    }
                }
            }
            for (int z = 0; z < 10; z++) {
                string = string + score2[z][0] + "," + score2[z][1] + "," + score2[z][2] + ";";
            }
        }else
        {
            string = date+","+pionts+","+levelCounter+";";
            for (int z = 0; z < 9; z++) {
                string = string + "--N.V.--,--N.V.--,--N.V.--;";
            }
        }

        try {
            game.getContext().deleteFile("drivingsim2");
            FileOutputStream outputStream = game.getContext().openFileOutput("drivingsim2", Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
