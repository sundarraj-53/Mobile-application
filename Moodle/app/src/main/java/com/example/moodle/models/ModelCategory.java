package com.example.moodle.models;

public class ModelCategory {

     private String id, Category,Uid;
     private String timestamp;


    public ModelCategory(){

     }


    public ModelCategory(String id, String Category, String Uid, String timestamp) {
        this.id = id;
        this.Category = Category;
        this.Uid = Uid;
        this.timestamp = timestamp;
    }
    public String getId() {
        System.out.println("getid"+id);
         return id;
    }
    public String getCategory()
    {
        System.out.println("getCategory"+Category);
        return Category;
    }

    public void setId(String id) {
        System.out.println("setid"+id);
         this.id = id;
    }

    public void setCategory(String Category) {
        System.out.println("setCategory"+Category);
         this.Category = Category;
    }

    public void setUid(String Uid) {
        System.out.println("setuid"+Uid);
         this.Uid = Uid;
    }

    public void setTimestamp(String timestamp) {
         System.out.println("settimestamp"+timestamp);

         this.timestamp = timestamp;
    }

    public String getUid()
    {
        System.out.println("getuid"+Uid);
        return Uid;
    }
    public String getTimestamp()
    {
        System.out.println("gettimestamp"+timestamp);
        return timestamp;
    }

}
