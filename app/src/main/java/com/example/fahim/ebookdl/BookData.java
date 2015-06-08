package com.example.fahim.ebookdl;

/**
 * Created by fahim on 6/8/15.
 */
public class BookData {

        String bookID; 		// Id of the book
        String bookTitle; 	// title of the book
        String subTitle; 	// sub title of the book
        String description; // discription of the book
        String author; 		// author of the book
        String ISBN; 		// isbn number of the book
        String pageNum; 		// page number of the book
        String year; 			// publishing year of the book
        String Publisher; 	// publisher of the book
        String image; 		// image url of the book image


        // constractor of bookData class
        public BookData() {
            // if you need anything fill the method
        }

        // constructor of the book to store the information;

        public BookData(String bookID, String bookTitle, String subTitle,
                        String description, String author, String ISBN, String pageNum,
                        String year, String publisher, String image) {
            super();
            this.bookID = bookID;
            this.bookTitle = bookTitle;
            this.subTitle = subTitle;
            this.description = description;
            this.author = author;
            this.ISBN = ISBN;
            this.pageNum = pageNum;
            this.year = year;
            Publisher = publisher;
            this.image = image;
        }



        // Getter and setter of all the veriable....

        public String getBookID() {
            return bookID;
        }
        public void setBookID(String bookID) {
            this.bookID = bookID;
        }
        public String getBookTitle() {
            return bookTitle;
        }
        public void setBookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
        }
        public String getSubTitle() {
            return subTitle;
        }
        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getAuthor() {
            return author;
        }
        public void setAuthor(String author) {
            this.author = author;
        }
        public String getISBN() {
            return ISBN;
        }
        public void setISBN(String iSBN) {
            ISBN = iSBN;
        }
        public String getPageNum() {
            return pageNum;
        }
        public void setPageNum(String pageNum) {
            this.pageNum = pageNum;
        }
        public String getYear() {
            return year;
        }
        public void setYear(String year) {
            this.year = year;
        }
        public String getPublisher() {
            return Publisher;
        }
        public void setPublisher(String publisher) {
            Publisher = publisher;
        }
        public String getImage() {
            return image;
        }
        public void setImage(String image) {
            this.image = image;
        }


}
