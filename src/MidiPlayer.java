/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shre
 */
import java.util.Queue;
import java.util.LinkedList;
import java.io.File;
import javax.sound.midi.*;

public class MidiPlayer{
    final Sequencer seq;
    private Queue song_queue;

    public MidiPlayer (Queue song_queue) {
    Sequencer seq = null;
    try{
        seq = MidiSystem.getSequencer();
        seq.addMetaEventListener( new MetaEventListener()
        {
            public void meta(MetaMessage msg){
            if(msg.getType()==0x2F)
                {
                MidiPlayer.this.play_next_song();
                }
            }
        });
    }
    catch (Exception e){}
    this.seq=seq;
    this.song_queue=song_queue;

    }

    private void play_next_song(){
    String next_song = (String)song_queue.remove();
    System.out.printf("Creating sequence for %s", next_song);
    try{
    Sequence sequence = MidiSystem.getSequence(new File(next_song));
    this.seq.setSequence(sequence);
    System.out.println("Playing");
    this.seq.start();}
    catch(Exception e){
    System.out.println("Error!");
    e.printStackTrace();
    play_next_song();
    }
    }
    public void start_playing () {
    try{this.seq.open();}catch(Exception e){}
    System.out.println("Starting to play");
    this.play_next_song();
    }
    public static void main(String[] args){
    Queue<String> song_queue = new LinkedList<String>();
   // song_queue.add("/home/ernesto/Music/Musica/My Music/march.mid");
    MidiPlayer.add_midi_files(song_queue,new File( "C:/Users/shre/Documents/NetBeansProjects/JavaApplication12/src/music"));
    System.out.println("Songs added!");
    MidiPlayer player = new MidiPlayer(song_queue);
    player.start_playing();
    };

    public static void add_midi_files(Queue<String> queue, File top_dir){
    for (File f: top_dir.listFiles()){
        if (f.isDirectory())
        add_midi_files(queue, f);
        else if (f.isFile() && f.getName().matches(".*midi?"))
        {
            System.out.println(f);
            queue.add(f.getAbsolutePath());
        }
        else
        {}
        //System.out.printf("%s doesn't match!", f.getName());
    }
    

    }
}
