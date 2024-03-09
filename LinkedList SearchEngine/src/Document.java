public class Document {
    String name;
    String description;
    Document next;

    public Document(String name, String description) {
        this.name = name;
        this.description = description;
        this.next = null;
    }
}