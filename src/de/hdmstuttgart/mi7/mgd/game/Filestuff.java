package de.hdmstuttgart.mi7.mgd.game;

import android.content.Context;

import java.io.*;
import java.util.Calendar;

/**
 * Created by Christoph on 13.09.2015.
 */
public class Filestuff {
    private Game game;
    String[][] score2 = new String[10][3];

    public Filestuff(Game game){
        this.game= game;
    }


    public String[][] getScore(){
        String date, highscore="";
        int pionts,lvl;
        String[] scores;

        try {
            Calendar k = Calendar.getInstance();
            int day = k.get(Calendar.DAY_OF_MONTH);
            int month = k.get(Calendar.MONTH);
            int year = k.get(Calendar.YEAR);

            File f = game.getContext().getFileStreamPath("drivingsim2");
            if (f.length() == 0) {
                String a = "--N.V.--";
                for (int i= 0;i<10;i++){
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
                        int piont, lvls;
                        String[] temp = new String[3];
                        String datum;

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
                }}} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return score2;
    }
    public void setScore(int levelCounter, int pionts) {
        for (int y = 0; y < 10; y++) {

            if (score2[y][1] == "--N.V.--" || pionts >= Integer.parseInt(score2[y][1])) {
                Calendar k = Calendar.getInstance();
                int day = k.get(Calendar.DAY_OF_MONTH);
                int month = k.get(Calendar.MONTH);
                int year = k.get(Calendar.YEAR);
                String date = day + "/" + month + "/" + year;
                if (y == 0) {
                    score2[y][0] = date;
                    score2[y][1] = String.valueOf(pionts);
                    score2[y][2] = String.valueOf(levelCounter);
                } else {
                    String[] tmp = new String[3];
                    tmp[0] = score2[y][0];
                    tmp[1] = score2[y][1];
                    tmp[2] = score2[y][2];
                    score2[y][0] = date;
                    score2[y][1] = "20";
                    score2[y][2] = String.valueOf(levelCounter);
                    score2[(y - 1)] = tmp;
                }
                String string = "";
                for (int z = 0; z < 10; z++) {
                    string = string + score2[z][0] + "," + score2[z][1] + "," + score2[z][2] + ";";
                }
                try {
                    File f = new File("drivingsim2");
                    f.delete();
                    FileOutputStream outputStream = game.getContext().openFileOutput("drivingsim2", Context.MODE_PRIVATE);
                    outputStream.write(string.getBytes());
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
