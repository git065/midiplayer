/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shre
 */

import javax.sound.midi.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.Queue;
import java.util.LinkedList;
import java.io.File;
import javax.sound.midi.*;


  // this one plays random music with it, but only because there is a listener.

public class MiniMusicPlayer {

    static JFrame f = new JFrame("My First Music Video");
    static MyDrawPanel ml;
 final Sequencer seq;
   private Queue song_queue;
   
   public MiniMusicPlayer (Queue song_queue) {
    Sequencer seq = null;
    try{
        seq = MidiSystem.getSequencer();
        seq.addMetaEventListener( new MetaEventListener()
        {
            public void meta(MetaMessage msg){
            if(msg.getType()==0x2F)
                {
                MiniMusicPlayer.this.play_next_song();
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

    public static void add_midi_files(Queue<String> queue, File top_dir)
    {
        for (File f: top_dir.listFiles())
        {
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
    public static void main(String[] args) 
    {
        //   MiniMusicPlayer mini = new MiniMusicPlayer();
    //       mini.go();
          
       Queue<String> song_queue = new LinkedList<String>();
   // song_queue.add("/home/ernesto/Music/Musica/My Music/march.mid");
    MiniMusicPlayer.add_midi_files(song_queue,new File( "C:/Users/shre/Documents/NetBeansProjects/JavaApplication12/src/music"));
    System.out.println("Songs added!");
    MiniMusicPlayer player = new MiniMusicPlayer(song_queue);
    player.go();
    player.start_playing();
    }
   
 
     public  void setUpGui() {
       ml = new MyDrawPanel();
       f.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);
       f.setContentPane(ml);
       f.setBounds(30,30, 300,300);
       f.setVisible(true);
    }
 

    public void go() {
       setUpGui();

       try {

         // make (and open) a sequencer, make a sequence and track

         Sequencer sequencer = MidiSystem.getSequencer();         
         sequencer.open();
        
         sequencer.addControllerEventListener(ml, new int[] {127});
         Sequence seq = new Sequence(Sequence.PPQ, 24);
         Track track = seq.createTrack();     

         // now make two midi events (containing a midi message)

      int r = 0;
      for (int i = 0; i < 60; i+= 4) {

          r = (int) ((Math.random() * 50) + 1);
         
          track.add(makeEvent(144,1,r,100,i));
        
          track.add(makeEvent(176,1,127,0,i));
         
          track.add(makeEvent(128,1,r,100,i + 2));
       } // end loop
        
          // add the events to the track            
          // add the sequence to the sequencer, set timing, and start

          sequencer.setSequence(seq);
 
          sequencer.start();
          sequencer.setTempoInBPM(120);
      } catch (Exception ex) {ex.printStackTrace();}
  } // close go


   public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
          MidiEvent event = null;
          try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
            
          }catch(Exception e) { }
          return event;
       }



 class MyDrawPanel extends JPanel implements ControllerEventListener {
      
      // only if we got an event do we want to paint
      boolean msg = false;

      public void controlChange(ShortMessage event) {
         msg = true;       
         repaint();         
      }

      public void paintComponent(Graphics g) {
       if (msg) {
            
         Graphics2D g2 = (Graphics2D) g;

         int r = (int) (Math.random() * 250);
         int gr = (int) (Math.random() * 250);
         int b = (int) (Math.random() * 250);

         g.setColor(new Color(r,gr,b));

         int ht = (int) ((Math.random() * 120) + 10);
         int width = (int) ((Math.random() * 120) + 10);

         int x = (int) ((Math.random() * 40) + 10);
         int y = (int) ((Math.random() * 40) + 10);
         
         g.fillRect(x,y,ht, width);
         msg = false;

       } // close if
     } // close method
   }  // close inner class

} // close class
