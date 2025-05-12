package Objects;

import Classes.ReelzsViewerController;
import javafx.scene.control.CheckBox;


public class Reel extends ReelzsViewerController {
    private int reelId;
    private String reelName;
    private String reelArtist;
    private int reelYear;
    private String reelType;
    private String reelGenre;
    private String reelRating;
    private CheckBox reelIsOriginal;
    private String reelNotes;
    private CheckBox reelDigitallyArchived;


    public Reel() {
        this.reelId = 0;
        this.reelName = "";
        this.reelArtist = "";
        this.reelYear = 2000;
        this.reelType = "";
        this.reelGenre = "";
        this.reelRating = "";
        this.reelIsOriginal = new CheckBox();
        this.reelNotes = "";
        this.reelDigitallyArchived = new CheckBox();

    }

    public Reel(int reelId , String reelName , String reelArtist , int reelYear , String reelType , String reelGenre , String reelRating ,int selectedOriginalStatus   , String reelNotes  ,int selectedArchivedStatus) {
        this.reelId = reelId;
        this.reelName = reelName;
        this.reelArtist = reelArtist;
        this.reelYear = reelYear;
        this.reelType = reelType;
        this.reelGenre = reelGenre;
        this.reelRating = reelRating;
        this.reelIsOriginal = new CheckBox();
        if (selectedOriginalStatus == 0){
            reelIsOriginal.setSelected(false);

        } else if (selectedOriginalStatus == 1){
            reelIsOriginal.setSelected(true);
        }

        this.reelNotes = reelNotes;
        this.reelDigitallyArchived = new CheckBox();
        if(selectedArchivedStatus == 0){
            reelDigitallyArchived.setSelected(false);
        }else if(selectedArchivedStatus == 1){
            reelDigitallyArchived.setSelected(true);
        }




    }



    public int getReelId() {
        return reelId;
    }

    public void setReelId(int reelId) {
        this.reelId = reelId;
    }

    public String getReelName() {
        return reelName;
    }

    public void setReelName(String reelName) {
        this.reelName = reelName;
    }

    public String getReelArtist() {
        return reelArtist;
    }

    public void setReelArtist(String reelArtist) {
        this.reelArtist = reelArtist;
    }

    public int getReelYear() {
        return reelYear;
    }

    public void setReelYear(int reelYear) {
        this.reelYear = reelYear;
    }

    public String getReelType() {
        return reelType;
    }

    public void setReelType(String reelType) {
        this.reelType = reelType;
    }

    public String getReelGenre() {
        return reelGenre;
    }

    public void setReelGenre(String reelGenre) {
        this.reelGenre = reelGenre;
    }

    public String getReelRating() {
        return reelRating;
    }

    public void setReelRating(String reelRating) {
        this.reelRating = reelRating;
    }

    public CheckBox getReelIsOriginal() {
        return reelIsOriginal;
    }

    public void setReelIsOriginal(boolean reelOriginalStatus) {
        this.reelIsOriginal.setSelected(reelOriginalStatus);
    }

    public boolean getReelIsOriginal2(){
        return reelIsOriginal.isSelected();

    }

    public String getReelNotes() {
        return reelNotes;
    }

    public void setReelNotes(String reelNotes) {
        this.reelNotes = reelNotes;
    }






    public boolean getReelDigitallyArchived2(){
        return reelDigitallyArchived.isSelected();
    }

    public CheckBox getReelDigitallyArchived() {
        return reelDigitallyArchived;
    }

    public void setReelDigitallyArchived(boolean archivedStatus) {
        this.reelDigitallyArchived.setSelected(archivedStatus);
    }

}