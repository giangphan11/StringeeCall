package vn.com.phanbagiang.myapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * ‐ @created_by giangpb on 4/1/2021
 */
public class DataSource {

    public static List<Note> getData(){
        List<Note> lstData = new ArrayList<>();
        lstData.add(new Note(1, "Giang Phan"));
        lstData.add(new Note(2, "Long Giang"));
        lstData.add(new Note(3, "Hoài Giang"));
        lstData.add(new Note(4, "Hà Giang"));
        lstData.add(new Note(5, "Phan Bá Giang"));
        lstData.add(new Note(6, "Giang Phan Bá"));
        return lstData;
    }
}
