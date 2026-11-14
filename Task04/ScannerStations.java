public class ScannerStations {
    static class Station extends Thread {
        final int scripts;
        Station(String name,int scripts){super(name);this.scripts=scripts;}
        public void run(){
            try {for(int i=0;i<scripts;i++)Thread.sleep(40);
                System.out.println(getName()+" scanned "+scripts+" scripts.");
            } catch(InterruptedException e){Thread.currentThread().interrupt();}
        }
    }
    public static void main(String[] args)throws InterruptedException{
        Station[] s={new Station("Station-1",5),new Station("Station-2",4),
            new Station("Station-3",3),new Station("Station-4",2)};
        for(Station t:s)t.start();
        for(Station t:s)t.join();
        System.out.println("All scanner stations completed.");
        for(Station t:s)System.out.println(t.getName()+" alive: "+t.isAlive());
    }
}
