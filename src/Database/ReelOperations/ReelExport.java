package Database.ReelOperations;


import Objects.Reel;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReelExport extends Reel {

    public ReelExport(){



    }

    public void writeStandardCsv( ObservableList<Reel> reelzsList) {

        Writer writer = null;
        FileChooser csvExport = new FileChooser();
        csvExport.setInitialDirectory(new File("."));
        csvExport.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files" , "*csv"));
        File file2 = csvExport.showSaveDialog(null);
        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes ,  ReelDigitallyArchived Status" + "\n";
        try {

            writer = new BufferedWriter(new FileWriter(file2));
            writer.write(headers);

            String hiddenBackupPath;
            for (Reel reel : reelzsList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() + "," +
                        "," + ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";

                writer.write(text);


            }
            writer.close();

            String OriginalPath = file2.toPath().toString();
            int pathInsertIndex = OriginalPath.indexOf(".");

            hiddenBackupPath = convertOriginalPathToHiddenBackupPath(file2.toPath().toString() , pathInsertIndex);
            standardCsvBackup(hiddenBackupPath);

        } catch (IOException ex) {

            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();


        }


    }

    public String convertOriginalPathToHiddenBackupPath(String s , int pathInsertIndex) {
        String convertedPath;
        int newPathInsertIndex = pathInsertIndex - 1;
        String backUpText = "backUp";
        StringBuffer resString = new StringBuffer(s);
        resString.insert(newPathInsertIndex , backUpText);
        convertedPath = resString.toString();
        System.out.println(convertedPath);

        return convertedPath;
    }

    public void standardCsvBackup(String hiddenBackupPath) {
        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes  , ReelDigitallyArchived Status" + "\n";
        Writer writer = null;

        try {
           File file3 = new File(hiddenBackupPath);
            writer = new BufferedWriter(new FileWriter(file3));
            writer.write(headers);
            for (Reel reel : reelzsList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() + "," +
                        "," + ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";

                writer.write(text);
                Path path = Paths.get(hiddenBackupPath);
                Files.setAttribute(path , "dos:hidden" ,
                        true , LinkOption.NOFOLLOW_LINKS);

            }
            writer.close();

        } catch (IOException ex) {


            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();

        }
        successfulReelzsExport.setContentText("reelzs successfully exported");
        successfulReelzsExport.showAndWait();

    }

    public void writeCdCsv(ObservableList<Reel> reelzsList){
        reelzsCdExportList.removeAll(reelzsCdExportList);

        for (Reel reel : reelzsList) {

            if (reel.getReelType().contains("cd")) {

                reelzsCdExportList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating() ,
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) ,
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

            }

        }

        Writer writer = null;
        FileChooser csvExport = new FileChooser();
        csvExport.setInitialDirectory(new File("."));
        csvExport.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files" , "*csv"));
        File file4 = csvExport.showSaveDialog(null);
        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes , ReelDigitallyArchived Status" + "\n";
        try {

            writer = new BufferedWriter(new FileWriter(file4));
            writer.write(headers);

            String hiddenBackupPath;
            for (Reel reel : reelzsCdExportList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() + ","
                        + "," + ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";
                System.out.println(text);
                writer.write(text);


            }
            writer.close();

            String OriginalPath = file4.toPath().toString();
            int pathInsertIndex = OriginalPath.indexOf(".");

            hiddenBackupPath = convertOriginalPathToHiddenBackupPath(file4.toPath().toString() , pathInsertIndex);
            cdCsvExportBackup(hiddenBackupPath);

        } catch (IOException ex) {

            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();

        }



    }

    public void cdCsvExportBackup(String hiddenBackupPath){

        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes  , ReelDigitallyArchived Status" + "\n";
        Writer writer = null;

        try {
            File file5 = new File(hiddenBackupPath);
            writer = new BufferedWriter(new FileWriter(file5));
            writer.write(headers);
            for (Reel reel : reelzsCdExportList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() + "," +
                         "," + ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";

                writer.write(text);
                Path path = Paths.get(hiddenBackupPath);
                Files.setAttribute(path , "dos:hidden" ,
                        true , LinkOption.NOFOLLOW_LINKS);

            }
            writer.close();

        } catch (IOException ex) {


            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();

        }
        successfulReelzsExport.setContentText("reelzs successfully exported");
        successfulReelzsExport.showAndWait();


    }

    public void writeDvdCsv(ObservableList<Reel> reelzsList){

        reelzsDvdExportList.removeAll(reelzsDvdExportList);

        for (Reel reel : reelzsList) {

            if (reel.getReelType().contains("dvd")) {

                reelzsDvdExportList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating() ,
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) ,
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

            }

        }

        Writer writer = null;
        FileChooser csvExport = new FileChooser();
        csvExport.setInitialDirectory(new File("."));
        csvExport.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files" , "*csv"));
        File file6 = csvExport.showSaveDialog(null);
        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes  , ReelDigitallyArchived Status" + "\n";
        try {

            writer = new BufferedWriter(new FileWriter(file6));
            writer.write(headers);

            String hiddenBackupPath;
            for (Reel reel : reelzsDvdExportList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() + "," +
                          ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";
                System.out.println(text);
                writer.write(text);


            }
            writer.close();

            String OriginalPath = file6.toPath().toString();
            int pathInsertIndex = OriginalPath.indexOf(".");

            hiddenBackupPath = convertOriginalPathToHiddenBackupPath(file6.toPath().toString() , pathInsertIndex);
            dvdCsvExportBackup(hiddenBackupPath);

        } catch (IOException ex) {

            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();


        }


    }

    public void dvdCsvExportBackup(String hiddenBackupPath){

        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes , ReelDigitallyArchived Status" + "\n";
        Writer writer = null;

        try {
            File file7 = new File(hiddenBackupPath);
            writer = new BufferedWriter(new FileWriter(file7));
            writer.write(headers);
            for (Reel reel : reelzsDvdExportList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() +
                         "," + ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";

                writer.write(text);
                Path path = Paths.get(hiddenBackupPath);
                Files.setAttribute(path , "dos:hidden" ,
                        true , LinkOption.NOFOLLOW_LINKS);

            }
            writer.close();

        } catch (IOException ex) {


            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();

        }
        successfulReelzsExport.setContentText("reelzs successfully exported");
        successfulReelzsExport.showAndWait();

    }


}
