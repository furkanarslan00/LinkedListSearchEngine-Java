import java.io.*;
import java.util.Scanner;

public class SearchEngine {
   
	private LinkedList documents = new LinkedList();

    public void clearDocumentList() {
        documents.clear();
    }

    public void loadDocumentsFromFile(String filePath) {
        clearDocumentList();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            StringBuilder currentDocument = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("<") && line.endsWith(">")) {
                    if (currentDocument.length() > 0) {
                        documents.append(currentDocument.toString());
                        currentDocument.setLength(0);
                    }
                    currentDocument.append(line).append("\n");
                } else {
                    currentDocument.append(line).append("\n");
                }
            }

            if (currentDocument.length() > 0) {
                documents.append(currentDocument.toString());
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchAndWriteResults(String queryString, String outputFilePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            String[] queryTerms = queryString.split(",");
            LinkedList<String> matchingDocuments = new LinkedList<>();

            for (String term : queryTerms) {
                term = term.trim();
                if (term.startsWith("!")) {
                    term = term.substring(1);
                    Node<String> currentDocument = documents.head;
                    while (currentDocument != null) {
                        String document = currentDocument.data;
                        if (document.contains(term)) {
                            matchingDocuments.delete(document);
                        }
                        currentDocument = currentDocument.next;
                    }
                } else {
                    Node<String> currentDocument = documents.head;
                    while (currentDocument != null) {
                        String document = currentDocument.data;
                        if (document.contains(term)) {
                            matchingDocuments.append(document);
                        }
                        currentDocument = currentDocument.next;
                    }
                }
            }

            writer.write("Search Query: <" + queryString + ">");
            writer.newLine();
            Node<String> matchedDocument = matchingDocuments.head;
            while (matchedDocument != null) {
                writer.write(matchedDocument.data);
                writer.newLine();
                matchedDocument = matchedDocument.next;
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeDocumentFromListAndFile(String query, String filePath) {
        LinkedList<String> documentsToKeep = new LinkedList();
        LinkedList<String> allDocuments = new LinkedList();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            StringBuilder currentDocument = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("<") && line.endsWith(">")) {
                    if (currentDocument.length() > 0) {
                        allDocuments.append(currentDocument.toString());
                        currentDocument.setLength(0);
                    }
                    currentDocument.append(line).append("\n");
                } else {
                    currentDocument.append(line).append("\n");
                }
            }

            if (currentDocument.length() > 0) {
                allDocuments.append(currentDocument.toString());
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Node<String> current = allDocuments.head;
        while (current != null) {
            String document = current.data;
            if (!document.contains(query)) {
                documentsToKeep.append(document);
            }
            current = current.next;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            current = documentsToKeep.head;
            while (current != null) {
                writer.write(current.data);
                writer.newLine();
                current = current.next;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;

        while (!exit) {
            System.out.println("Choose an action:");
            System.out.println("1. Load Documents");
            System.out.println("2. Search Documents");
            System.out.println("3. Remove Document");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter the path to the info file: ");
                    String filePath = scanner.nextLine();
                    searchEngine.loadDocumentsFromFile(filePath);
                    System.out.println("Successfull...");
                    break;
                case 2:
                    System.out.print("Enter the search query: ");
                    String queryString = scanner.nextLine();
                    System.out.print("Enter the output file path: ");
                    String outputFilePath = scanner.nextLine();
                    searchEngine.searchAndWriteResults(queryString, outputFilePath);
                    System.out.println("Successfull...");
                    break;
                case 3:
                    System.out.print("Enter the document to remove: ");
                    String documentToRemove = scanner.nextLine();
                    System.out.print("Enter the file path for removal: ");
                    String removeFilePath = scanner.nextLine();
                    searchEngine.removeDocumentFromListAndFile(documentToRemove, removeFilePath);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }

        scanner.close();
    }
}
