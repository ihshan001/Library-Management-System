/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author AL MUBDIE
 */
public class Book {

    private String bookID;
    private String title;
    private String author;
    private int yearPublished;
    // Constructor

    public Book(String bookID, String title, String author, int yearPublished) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
    }
    // Getters and Setters

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }
    // Polymorphic method

    public String displayDetails() {
        return "Book ID: " + bookID + "\nTitle: " + title + "\nAuthor: " + author + "\nYearPublished: " + yearPublished;
    }
}
