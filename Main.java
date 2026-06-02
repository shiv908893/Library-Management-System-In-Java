import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// ========== INTERFACES ==========
interface BookOperations {
    void addBook(Book book);
    void displayAllBooks();
    Book searchBook(String query);
    void issueBook(String title, String userName) throws Exception;
    void returnBook(String title) throws Exception;
    void renewBook(String title) throws Exception;
    double calculateFine(String title);
}
interface MagazineOperations {
    void addMagazine(Magazine magazine);
    void displayAllMagazines();
    void issueMagazine(String title, String userName) throws Exception;
    void returnMagazine(String title) throws Exception;
}
interface JournalOperations {
    void addJournal(Journal journal);
    void displayAllJournals();
    void issueJournal(String title, String userName) throws Exception;
    void returnJournal(String title) throws Exception;
}
abstract class LibraryItem {
    private String title;
    private String issuedTo;
    private LocalDate dueDate;
    private boolean isIssued;
    public LibraryItem(String title) {
        this.title = title;
        this.isIssued = false;
        this.issuedTo = null;
        this.dueDate = null;
    }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIssuedTo() { return issuedTo; }
    public void setIssuedTo(String issuedTo) { this.issuedTo = issuedTo; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public boolean isIssued() { return isIssued; }
    public void setIssued(boolean issued) { isIssued = issued; }
    public abstract void displayDetails();
}
class Book extends LibraryItem implements BookOperations {
    private String author;
    private String isbn;
    private int copiesAvailable;
    public Book(String title, String author, String isbn, int copiesAvailable) {
        super(title);
        this.author = author;
        this.isbn = isbn;
        this.copiesAvailable = copiesAvailable;
    }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getCopiesAvailable() { return copiesAvailable; }
    public void setCopiesAvailable(int copiesAvailable) { this.copiesAvailable = copiesAvailable; }
    public void displayDetails() {
        System.out.printf("Book -> Title: %-25s | Author: %-20s | ISBN: %-13s | Copies: %-2d%n", 
                getTitle(), author, isbn, copiesAvailable);
    }
   public void addBook(Book book) {}
   public void displayAllBooks() {}
   public Book searchBook(String query) { return null; }
  public void issueBook(String title, String userName) {}
  public void returnBook(String title) {}
  public void renewBook(String title) {}
  public double calculateFine(String title) { return 0.0; }
}

class Magazine extends LibraryItem implements MagazineOperations {
    private String issueNumber;
    private String publisher;
 public Magazine(String title, String issueNumber, String publisher) {
        super(title);
        this.issueNumber = issueNumber;
        this.publisher = publisher;
    }
  public String getIssueNumber() { return issueNumber; }
  public String getPublisher() { return publisher; }
  public void displayDetails() {
        String status = isIssued() ? "Issued to " + getIssuedTo() : "Available";
        System.out.printf("Magazine -> Title: %-22s | Issue: %-8s | Publisher: %-15s | Status: %s%n", 
                getTitle(), issueNumber, publisher, status);
    }
   public void addMagazine(Magazine magazine) {}
   public void displayAllMagazines() {}
   public void issueMagazine(String title, String userName) {}
   public void returnMagazine(String title) {}
}
class Journal extends LibraryItem implements JournalOperations {
    private String volume;
    private String issue;
    private String publisher;
    public Journal(String title, String volume, String issue, String publisher) {
        super(title);
        this.volume = volume;
        this.issue = issue;
        this.publisher = publisher;
    }
    public String getVolume() { return volume; }
    public String getIssue() { return issue; }
    public String getPublisher() { return publisher; }
    public void displayDetails() {
        String status = isIssued() ? "Issued to " + getIssuedTo() : "Available";
        System.out.printf("Journal -> Title: %-23s | Vol: %-5s | Issue: %-5s | Publisher: %-15s | Status: %s%n", 
                getTitle(), volume, issue, publisher, status);
    }
   public void addJournal(Journal journal) {}
   public void displayAllJournals() {}
   public void issueJournal(String title, String userName) {}
   public void returnJournal(String title) {}
}
class Library implements BookOperations, MagazineOperations, JournalOperations {
    private String libraryName;
    private String address;
    private List<Book> books;
    private List<Magazine> magazines;
    private List<Journal> journals;
    private static final int BOOK_LENDING_DAYS = 14;
    private static final int PERIODICAL_LENDING_DAYS = 7;
    private static final double FINE_RATE_PER_DAY = 5.0;
    public Library(String libraryName, String address) {
        this.libraryName = libraryName;
        this.address = address;
        this.books = new ArrayList<>();
        this.magazines = new ArrayList<>();
        this.journals = new ArrayList<>();
        loadSampleData();
    }
    private void loadSampleData() {
        books.add(new Book("Effective Java", "Joshua Bloch", "978-0134685991", 5));
        books.add(new Book("Clean Code", "Robert C. Martin", "978-0132350884", 2));
        books.add(new Book("The Pragmatic Programmer", "Andy Hunt", "978-0135957059", 3));
        magazines.add(new Magazine("National Geographic", "May 2026", "NG Media"));
        magazines.add(new Magazine("Time Magazine", "Vol. 204", "Time USA"));
        journals.add(new Journal("Nature", "628", "8002", "Springer Nature"));
        journals.add(new Journal("IEEE Software", "41", "3", "IEEE Computer Society"));
    }
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Success: Book registered successfully!");
    }
    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in library database.");
            return;
        }
        books.forEach(Book::displayDetails);
    }
    public Book searchBook(String query) {
        return books.stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(query) || b.getAuthor().equalsIgnoreCase(query))
                .findFirst()
                .orElse(null);
    }
    public void issueBook(String title, String userName) throws Exception {
        Book book = searchBook(title);
        if (book == null) throw new Exception("Error: Book not found in catalog!");
        if (book.getCopiesAvailable() <= 0) throw new Exception("Error: No copies currently available!");
        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        book.setIssued(true);
        book.setIssuedTo(userName);
        book.setDueDate(LocalDate.now().plusDays(BOOK_LENDING_DAYS));
        System.out.println("Success: Book issued to " + userName + ". Due Date: " + book.getDueDate());
    }
    public void returnBook(String title) throws Exception {
        Book book = searchBook(title);
        if (book == null) throw new Exception("Error: Book not found in catalog!");
        double fine = calculateFine(title);
        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
        book.setIssued(false);
        book.setIssuedTo(null);
        book.setDueDate(null);
        System.out.print("Success: Book returned.");
        if (fine > 0) {
            System.out.println(" [Overdue Fine Pending: Rs. " + fine + "]");
        } else {
            System.out.println(" [No outstanding fines]");
        }
    }
    public void renewBook(String title) throws Exception {
        Book book = searchBook(title);
        if (book == null) throw new Exception("Error: Book not found!");
        if (!book.isIssued()) throw new Exception("Error: This book is not currently issued to anyone!");
        book.setDueDate(LocalDate.now().plusDays(BOOK_LENDING_DAYS));
        System.out.println("Success: Book renewed successfully. New Due Date: " + book.getDueDate());
    }
    public double calculateFine(String title) {
        Book book = searchBook(title);
        if (book == null || book.getDueDate() == null || !book.isIssued()) return 0.0;
        long daysOverdue = ChronoUnit.DAYS.between(book.getDueDate(), LocalDate.now());
        return daysOverdue > 0 ? daysOverdue * FINE_RATE_PER_DAY : 0.0;
    }
    public void addMagazine(Magazine magazine) {
        magazines.add(magazine);
        System.out.println("Success: Magazine registered successfully!");
    }
    public void displayAllMagazines() {
        if (magazines.isEmpty()) {
            System.out.println("No magazines in library database.");
            return;
        }
        magazines.forEach(Magazine::displayDetails);
    }
    public void issueMagazine(String title, String userName) throws Exception {
        Magazine magazine = magazines.stream()
                .filter(m -> m.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
        if (magazine == null) throw new Exception("Error: Magazine not found.");
        if (magazine.isIssued()) throw new Exception("Error: Magazine is already checked out.");
        magazine.setIssued(true);
        magazine.setIssuedTo(userName);
        magazine.setDueDate(LocalDate.now().plusDays(PERIODICAL_LENDING_DAYS));
        System.out.println("Success: Magazine issued. Due Date: " + magazine.getDueDate());
    }
    public void returnMagazine(String title) throws Exception {
        Magazine magazine = magazines.stream()
                .filter(m -> m.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
        if (magazine == null) throw new Exception("Error: Magazine not found.");
        if (!magazine.isIssued()) throw new Exception("Error: Magazine was not checked out.");
        magazine.setIssued(false);
        magazine.setIssuedTo(null);
        magazine.setDueDate(null);
        System.out.println("Success: Magazine checked back in.");
    }
    public void addJournal(Journal journal) {
        journals.add(journal);
        System.out.println("Success: Journal registered successfully!");
    }
    public void displayAllJournals() {
        if (journals.isEmpty()) {
            System.out.println("No journals in library database.");
            return;
        }
        journals.forEach(Journal::displayDetails);
    }
    public void issueJournal(String title, String userName) throws Exception {
        Journal journal = journals.stream()
                .filter(j -> j.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
        if (journal == null) throw new Exception("Error: Journal not found.");
        if (journal.isIssued()) throw new Exception("Error: Journal is already checked out.");
        journal.setIssued(true);
        journal.setIssuedTo(userName);
        journal.setDueDate(LocalDate.now().plusDays(PERIODICAL_LENDING_DAYS));
        System.out.println("Success: Journal issued. Due Date: " + journal.getDueDate());
    }
    public void returnJournal(String title) throws Exception {
        Journal journal = journals.stream()
                .filter(j -> j.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
        if (journal == null) throw new Exception("Error: Journal not found.");
        if (!journal.isIssued()) throw new Exception("Error: Journal was not checked out.");
        journal.setIssued(false);
        journal.setIssuedTo(null);
        journal.setDueDate(null);
        System.out.println("Success: Journal checked back in.");
    }
    public String getLibraryName() { return libraryName; }
    public String getAddress() { return address; }
}
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Library library = new Library("Metropolis Central Library", "742 Evergreen Terrace");
public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" Welcome to " + library.getLibraryName());
        System.out.println(" Location: " + library.getAddress());
        System.out.println("=================================================");
        while (true) {
            displayMainMenu();
            int choice = readIntegerInput();
            try {
                switch (choice) {
                    case 1:
                        handleAddItem();
                        break;
                    case 2:
                        handleSearchBook();
                        break;
                    case 3:
                        handleIssueItem();
                        break;
                    case 4:
                        handleReturnItem();
                        break;
                    case 5:
                        handleRenewBook();
                        break;
                    case 6:
                        handleCalculateFine();
                        break;
                    case 7:
                        handleDisplayAllItems();
                        break;
                    case 8:
                        System.out.println("Thank you for using the Library Management System!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please pick between 1 and 8.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n------------- MAIN MENU -------------");
        System.out.println("1. Add Library Item");
        System.out.println("2. Search Book (by Title/Author)");
        System.out.println("3. Issue Item");
        System.out.println("4. Return Item");
        System.out.println("5. Renew Book");
        System.out.println("6. Calculate Fine (Books)");
        System.out.println("7. Display All Items");
        System.out.println("8. Exit");
        System.out.print("Enter choice: ");
    }
    private static void handleAddItem() {
        System.out.println("\nChoose item type to add:");
        System.out.println("1. Book");
        System.out.println("2. Magazine");
        System.out.println("3. Journal");
        System.out.print("Enter choice: ");
        int type = readIntegerInput();
        switch (type) {
            case 1:
                System.out.print("Enter Title: ");
                String title = scanner.nextLine();
                System.out.print("Enter Author: ");
                String author = scanner.nextLine();
                System.out.print("Enter ISBN: ");
                String isbn = scanner.nextLine();
                System.out.print("Enter Available Copies: ");
                int copies = readIntegerInput();
                library.addBook(new Book(title, author, isbn, copies));
                break;
            case 2:
                System.out.print("Enter Title: ");
                String magTitle = scanner.nextLine();
                System.out.print("Enter Issue Number: ");
                String issueNo = scanner.nextLine();
                System.out.print("Enter Publisher: ");
                String publisher = scanner.nextLine();
                library.addMagazine(new Magazine(magTitle, issueNo, publisher));
                break;
            case 3:
                System.out.print("Enter Title: ");
                String jTitle = scanner.nextLine();
                System.out.print("Enter Volume: ");
                String volume = scanner.nextLine();
                System.out.print("Enter Issue: ");
                String jIssue = scanner.nextLine();
                System.out.print("Enter Publisher: ");
                String jPublisher = scanner.nextLine();
                library.addJournal(new Journal(jTitle, volume, jIssue, jPublisher));
                break;
            default:
                System.out.println("Operation canceled: Invalid item choice.");
        }
    }
    private static void handleSearchBook() {
        System.out.print("\nEnter Book Title or Author to search: ");
        String query = scanner.nextLine();
        Book book = library.searchBook(query);
        if (book != null) {
            System.out.println("\n--- Search Result ---");
            book.displayDetails();
        } else {
            System.out.println("No matching books found.");
        }
    }
    private static void handleIssueItem() throws Exception {
        System.out.println("\nSelect item type to issue:");
        System.out.println("1. Book | 2. Magazine | 3. Journal");
        System.out.print("Enter choice: ");
        int type = readIntegerInput();
        System.out.print("Enter Item Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter User Name: ");
        String userName = scanner.nextLine();
        if (type == 1) {
            library.issueBook(title, userName);
        } else if (type == 2) {
            library.issueMagazine(title, userName);
        } else if (type == 3) {
            library.issueJournal(title, userName);
        } else {
            System.out.println("Invalid choice.");
        }
    }
    private static void handleReturnItem() throws Exception {
        System.out.println("\nSelect item type to return:");
        System.out.println("1. Book | 2. Magazine | 3. Journal");
        System.out.print("Enter choice: ");
        int type = readIntegerInput();
        System.out.print("Enter Item Title: ");
        String title = scanner.nextLine();
        if (type == 1) {
            library.returnBook(title);
        } else if (type == 2) {
            library.returnMagazine(title);
        } else if (type == 3) {
            library.returnJournal(title);
        } else {
            System.out.println("Invalid choice.");
        }
    }
    private static void handleRenewBook() throws Exception {
        System.out.print("\nEnter Book Title to renew: ");
        String title = scanner.nextLine();
        library.renewBook(title);
    }
    private static void handleCalculateFine() {
        System.out.print("\nEnter Book Title to calculate fine: ");
        String title = scanner.nextLine();
        double fine = library.calculateFine(title);
        System.out.println("Calculated Outstanding Fine: Rs. " + fine);
    }
    private static void handleDisplayAllItems() {
        System.out.println("\n================= LIBRARY CATALOG ==================");
        System.out.println("\n--- BOOKS ---");
        library.displayAllBooks();
        System.out.println("\n--- MAGAZINES ---");
        library.displayAllMagazines();
        System.out.println("\n--- JOURNALS ---");
        library.displayAllJournals();
        System.out.println("====================================================");
    }
    private static int readIntegerInput() {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                return input;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}

