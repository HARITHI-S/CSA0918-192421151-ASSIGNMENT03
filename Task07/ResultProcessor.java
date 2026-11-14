import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
public class ResultProcessor {
    static class Result {final String file;final boolean ok;final String message;
        Result(String f,boolean o,String m){file=f;ok=o;message=m;}}
    static class ScoreTask implements Callable<Result>{
        final Path file;ScoreTask(Path f){file=f;}
        public Result call(){
            try{
                if(!Files.exists(file))return new Result(file.getFileName().toString(),false,"Missing file");
                int score=Integer.parseInt(Files.readString(file).trim());
                if(score<0||score>100)return new Result(file.getFileName().toString(),false,"Score outside 0-100");
                return new Result(file.getFileName().toString(),true,"Valid score "+score);
            }catch(IOException|NumberFormatException e){
                return new Result(file.getFileName().toString(),false,"Corrupt/unreadable file");
            }
        }
    }
    static final Path DIR=Paths.get("Task07","input");
    static void makeSamples()throws IOException{
        Files.createDirectories(DIR);
        for(int i=1;i<=12;i++)Files.writeString(DIR.resolve("student"+i+".txt"),i==4?"bad-score":String.valueOf(50+i));
        Files.deleteIfExists(DIR.resolve("student12.txt"));
    }
    static void runPool(int n)throws Exception{
        ExecutorService pool=Executors.newFixedThreadPool(n);
        ConcurrentLinkedQueue<Result> results=new ConcurrentLinkedQueue<>();
        List<Future<Result>> futures=new ArrayList<>();long start=System.nanoTime();
        try{
            for(int i=1;i<=12;i++)futures.add(pool.submit(new ScoreTask(DIR.resolve("student"+i+".txt"))));
            for(Future<Result> f:futures)results.add(f.get());
        }finally{pool.shutdown();if(!pool.awaitTermination(10,TimeUnit.SECONDS))pool.shutdownNow();}
        long ms=TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-start);
        int good=0,bad=0;
        for(Result r:results){if(r.ok)good++;else{bad++;System.out.println("Failed: "+r.file+" - "+r.message);}}
        System.out.println("Pool size: "+n+" | Success: "+good+" | Failure: "+bad+" | Time: "+ms+" ms");
    }
    public static void main(String[] args)throws Exception{
        makeSamples();System.out.println("Semester result processing (12 sample files)");
        for(int n:new int[]{1,2,4,8})runPool(n);
        System.out.println("Expected count per run: 10 successes, 2 failures.");
    }
}
