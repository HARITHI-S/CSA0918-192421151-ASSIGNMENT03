import java.io.*;
import java.nio.file.*;
public class ReportGenerator {
    static final Path DIR=Paths.get("Task03","data");
    static void samples() throws IOException {
        Files.createDirectories(DIR);
        for(int i=1;i<=3;i++) Files.writeString(DIR.resolve("store"+i+".txt"),"Store "+i+" sales: "+(i*100)+"\n");
    }
    static void manual() {
        BufferedWriter writer=null;
        try {
            writer=Files.newBufferedWriter(DIR.resolve("manual-report.txt"));
            for(int i=1;i<=3;i++) {
                BufferedReader reader=null;
                try {
                    reader=Files.newBufferedReader(DIR.resolve("store"+i+".txt"));
                    String line=reader.readLine();
                    if(i==2) throw new IOException("Simulated failure at store2.txt");
                    writer.write(line); writer.newLine();
                } catch(IOException e) { System.out.println("Manual version error: "+e.getMessage()); break; }
                finally { if(reader!=null) try{reader.close();}catch(IOException e){System.out.println(e.getMessage());} }
            }
        } catch(IOException e){System.out.println("Writer error: "+e.getMessage());}
        finally {if(writer!=null) try{writer.close();}catch(IOException e){System.out.println(e.getMessage());}}
    }
    static void safe() {
        try(BufferedWriter writer=Files.newBufferedWriter(DIR.resolve("safe-report.txt"))) {
            for(int i=1;i<=3;i++) {
                try(BufferedReader reader=Files.newBufferedReader(DIR.resolve("store"+i+".txt"))) {
                    String line=reader.readLine();
                    if(i==2) throw new IOException("Simulated failure at store2.txt");
                    writer.write(line); writer.newLine();
                } catch(IOException e){System.out.println("Try-with-resources error: "+e.getMessage());break;}
            }
        } catch(IOException e){System.out.println("Safe writer error: "+e.getMessage());}
    }
    public static void main(String[] args)throws IOException {
        samples(); manual(); safe();
        System.out.println("Both report methods finished; resources are closed.");
        System.out.println("Check Task03/data/ for generated reports.");
    }
}
