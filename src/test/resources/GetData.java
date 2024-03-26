import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class GetData {
    public static ArrayList<String[]> readFromCsv(String pathToFile){
        ArrayList<String[]> res = new ArrayList<String[]>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathToFile))) {
            String delimiter = ",";
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(delimiter);
                res.add(columns);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
