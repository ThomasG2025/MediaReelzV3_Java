package Database.ReelOperations;


import Objects.Reel;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

public class ReelSearch extends Reel {

    private TableView<Reel> reelzsTable;


    public ReelSearch(TableView<Reel> reelzsTable){
        this.reelzsTable =reelzsTable;

    }

    public void standardReelzsSearchMethod(TextField searchReelzsTextField , FilteredList<Reel> filteredData , ObservableList<Reel> reelzsList) {


        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);
        reelzsTable.setItems(reelzsList);

        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredData.setPredicate((Predicate<? super Reel>) reel -> {
        System.out.println("new value standard search"+ newValue);
                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }

                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelRating().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }

                return reel.getReelNotes().toLowerCase().contains(lowerCaseFilter);
            }));


            SortedList<Reel> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

    public void cdSearchMethod(TextField searchReelzsTextField , FilteredList<Reel> filteredCdReelzs , ObservableList<Reel> reelzsList){

        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        for (Reel reel : reelzsList) {

            if (reel.getReelType().contains("cd")) {

                reelzsCdList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

                reelzsTable.setItems(reelzsCdList);

            }

        }



        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredCdReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return reel.getReelNotes().toLowerCase().contains(lowerCaseFilter);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredCdReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

    public void dvdSearchMethod(TextField searchReelzsTextField , FilteredList<Reel> filteredDvdReelzs , ObservableList<Reel> reelzsList){

        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);


        for (Reel reel : reelzsList) {

            if (reel.getReelType().toLowerCase().contains("dvd")) {

                reelzsDvdList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

                reelzsTable.setItems(reelzsDvdList);

            }

        }



        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredDvdReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return reel.getReelNotes().toLowerCase().contains(lowerCaseFilter);


            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredDvdReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });



    }

    public void artistSearchMethod(TextField searchReelzsTextField , FilteredList<Reel> filteredArtists ) {

        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);



        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredArtists.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (reel.getReelArtist().toLowerCase().contains(newValue)) {
                    return true;
                }

                return reel.getReelArtist().toLowerCase().contains(lowerCaseFilter);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredArtists);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

    public void yearSearchMethod(TextField searchReelzsTextField , FilteredList<Reel> filteredYears ) {


        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);


       searchReelzsTextField.setOnKeyPressed(e -> {
           searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredYears.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }


                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }

                return String.valueOf(reel.getReelYear()).contains(newValue);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredYears);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

    public void isOriginalMethod(TextField searchReelzsTextField , FilteredList<Reel> filteredOriginalReelzs , ObservableList<Reel> reelzsList){

        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        for (Reel reel : reelzsList) {

            if (ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) == 1) {

                reelzsIsOriginalList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

                reelzsTable.setItems(reelzsIsOriginalList);

            }

        }



        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredOriginalReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return reel.getReelNotes().toLowerCase().contains(lowerCaseFilter);
            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredOriginalReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

    public void isNotOriginalMethod(TextField searchReelzsTextField , FilteredList<Reel> filteredNotOriginalReelzs , ObservableList<Reel> reelzsList){

        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        for (Reel reel : reelzsList) {

            if (ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) == 0) {

                reelzsNotOriginalList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

                reelzsTable.setItems(reelzsNotOriginalList);

            }

        }



        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredNotOriginalReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
               return reel.getReelNotes().toLowerCase().contains(lowerCaseFilter);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredNotOriginalReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

    public void typeSearchMethod(TextField searchReelzsTextField , FilteredList<Reel> filteredTypes ) {

        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);


       searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredTypes.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (reel.getReelType().toLowerCase().contains(newValue)) {
                    return true;
                }

                return reel.getReelType().toLowerCase().contains(lowerCaseFilter);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredTypes);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

    public void genreSearchMethod(TextField searchReelzsTextField , FilteredList<Reel> filteredGenres) {

        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);


       searchReelzsTextField.setOnKeyPressed(e -> {
           searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredGenres.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (reel.getReelGenre().toLowerCase().contains(newValue)) {
                    return true;
                }

                return reel.getReelGenre().toLowerCase().contains(lowerCaseFilter);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredGenres);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }



    public void digitallyArchivedSearchMethod(TextField searchReelzsTextField , FilteredList<Reel> filteredArchivedReelzs , ObservableList<Reel> reelzsList){

        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        for (Reel reel : reelzsList) {

            if (ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) == 1) {

                reelzsArchivedList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

                reelzsTable.setItems(reelzsArchivedList);

            }

        }

        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredArchivedReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }

                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return reel.getReelNotes().toLowerCase().contains(lowerCaseFilter);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredArchivedReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

    public void notDigitallyArchivedSearchMethod(TextField searchReelzsTextField , FilteredList<Reel> filteredNotArchivedReelzs , ObservableList<Reel> reelzsList){
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        for (Reel reel : reelzsList) {

            if (ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) == 0) {

                reelzsNotArchivedList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

                reelzsTable.setItems(reelzsNotArchivedList);

            }

        }

        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredNotArchivedReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }

                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if(reel.getReelRating().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return reel.getReelNotes().toLowerCase().contains(lowerCaseFilter);



            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredNotArchivedReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }










}
