package ExceptionsPackage;
import java.io.FileReader;
import java.io.IOException;

public class ResourceManagement {
    public void manageResources() {
        try (FileReader fileReader = new FileReader("FileForResourceManagement/try-with-resources Statement.txt")) {
            // Use the fileReader resource
            int data;
            while ((data = fileReader.read()) != -1) {
                System.out.print((char) data);
            }
        } catch (IOException e) {
            System.out.println("Error handling file operations: " + e.getMessage());
        }
        // The fileReader resource is automatically closed here
    }
}
