package com.example.bookinder.models;

public class ItemBookData {
    private String card_id;
    private String title;
    private String method;
    private String book_image;
    private String author;
    private String price;
    private String genre;
    private String seller_id;
    private String seller_name;
    private String seller_image;
    private String isLiked;

    public ItemBookData(String card_id, String title, String method, String book_image,
                        String author, String price, String genre, String seller_id,
                        String seller_name, String seller_image, String isLiked) {
        this.card_id = card_id;
        this.title = title;
        this.method = method;
        this.book_image = book_image;
        this.author = author;
        this.price = price;
        this.genre = genre;
        this.seller_id = seller_id;
        this.seller_name = seller_name;
        this.seller_image = seller_image;
        this.isLiked = isLiked;
    }

    public String getBookName() {
        return title;
    }

    public void setBookName(String bookName) {
        this.title = bookName;
    }

    public String getBookMethod() {
        return method;
    }

    public void setBookMethod(String bookMethod) {
        this.method = bookMethod;
    }

    public String getBookImage() {
        return book_image;
    }

    public void setBookImage(String bookImage) {
        this.book_image = bookImage;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuthorName() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getOwnerId() {
        return seller_id;
    }

    public String getOwnerImage() {
        return seller_image;
    }

    public String getOwnerName() {
        return seller_name;
    }
    public String getCard_id() {return card_id;}
    public String getIsLiked() {return isLiked;}
}
