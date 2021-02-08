package nomnom;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.event.*; // Using AWT's event classes and listener interfaces
import javax.swing.*;    
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import java.util.Random;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class NomNom extends javax.swing.JFrame implements ActionListener,KeyListener {
   public Random rand = new Random();
   Timer t = new Timer(5,this);
   private static final int CANVAS_WIDTH = 1360;
   private static final int CANVAS_HEIGHT = 745;
   int Hud=(CANVAS_HEIGHT*7)/100;
   private static int UPDATE_PERIOD = 50; // milliseconds
 
   private DrawCanvas canvas;  // the drawing canvas (an inner class extends JPanel)
 
   // Attributes of moving object
    int x = CANVAS_WIDTH/2, y = CANVAS_HEIGHT/2;  // top-left (x, y)
    
    int Size = 30;        // width and height
       
    int xSpeed = 0, ySpeed = 0; // displacement per step in x, y
    
    int speed = 10;
    int score = 0;
    int c;
    boolean game = true;
    int Lives = 3;
    int pillBuffer = 2 + Size;
    int xPill = rand.nextInt(CANVAS_WIDTH-2*pillBuffer)+pillBuffer;
    int yPill= rand.nextInt(CANVAS_HEIGHT-2*Hud)+Hud;
    String pillType="";
    int pt = rand.nextInt(101); 
    int Sheild = 3;
    int playerScore;
    SaveData data = new SaveData();
    /**
     * Creates new form NewJFrame
     */

    public NomNom() {
      
      t.start();
      canvas = new DrawCanvas();
      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
      this.setContentPane(canvas);
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.pack();
      this.setTitle("NomNom...");
      this.setVisible(true);
      addKeyListener(this);
      setFocusable(true);
      this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
      

 
      // Define an ActionListener to perform update at regular interval
      ActionListener updateTask = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            update();   // update the (x, y) position
            repaint();  // Refresh the JFrame, callback paintComponent()
            
         }
      };
      // Allocate a Timer to run updateTask's actionPerformed() after every delay msec
      new Timer(UPDATE_PERIOD, updateTask).start();

    }

public void update() {
    
        if(pt<90)
            pillType = "Green"; 
        if(pt<25)
            pillType = "Red";
        if(pt<15)
            pillType = "Blue";
        if (pt<20)
            pillType = "Orange";
        if(pt<5)
            pillType = "Cyan";
       
            
      x += xSpeed;
      y += ySpeed;
      if ( x < 0) {
         xSpeed = -xSpeed;
         ySpeed = 0;
         if(Sheild<=0){
         game = false;
         x = 0;}
         Sheild--; 
      }
      if(x > CANVAS_WIDTH - Size-2)
      {
         xSpeed = -xSpeed;
         ySpeed = 0;
         if(Sheild<=0){
         game = false;
         x = CANVAS_WIDTH - Size;}
         Sheild--;
      }
      if (y < Hud) {
         ySpeed = -ySpeed;
         xSpeed = 0;
         if(Sheild<=0){
         game = false;
         y = 0;}
         Sheild--;
      }
      if (y + Size > CANVAS_HEIGHT-2)
      {
         ySpeed = -ySpeed;
         xSpeed = 0;
         if(Sheild<=0){
         game = false;
         y = CANVAS_HEIGHT - Size;}
         Sheild--;
      }
      if(Sheild<0)
          Sheild=0;
      if((xPill>x&&xPill+10<x+Size)&&(yPill>y&&yPill+10<y+Size))
      {
         xPill = rand.nextInt(CANVAS_WIDTH);
         yPill = rand.nextInt(CANVAS_HEIGHT);
         while(xPill<pillBuffer||xPill>CANVAS_WIDTH-pillBuffer)
          xPill = rand.nextInt(CANVAS_WIDTH);
         while(yPill<Hud+pillBuffer||yPill>CANVAS_HEIGHT-pillBuffer)
          yPill = rand.nextInt(CANVAS_HEIGHT);
         if(speed<135&&speed>-135)
         {
             if(speed>0)
                speed+=2;
             if(speed<0)
                 speed-=2;
         }
         else{
             UPDATE_PERIOD-=10;
         }
         
         if(pillType.equals("Cyan"))
         {
             if(speed>20){
             Size -= 5; speed -= 6;}
        
         }
         if(pillType.equals("Blue")&&Sheild<3)
         {Sheild++;
         if(Sheild==3)
         score++;
         }
         if(pillType.equals("Red")&&Lives<3)
         { Lives++;
         if(Lives==3)
         score++;
         }
         if(pillType.equals("Green"))
         {score++;}
         if(pillType.equals("Orange"))
         {speed *= -1;}
             
         if(Size<150)
         {
             Size+=2;
         }
        pt = rand.nextInt(101);
       }
      if(game==false)
      {
          
       speed = 10;
       xSpeed = 0;
       ySpeed = 0;
       game = true;
       x = CANVAS_WIDTH/2; 
       y = CANVAS_HEIGHT/2;
       Size = 30;
       Lives--;
       if(Lives <= 0)
       { score = 0;
         Lives = 3;
         Sheild = 3;
       }
      }
      
      
      try{
          
      if(score>data.pScore)
          data.pScore = score;
          resourceManager.save(data, "Save.Nom");
      }
      catch(Exception e){
          System.out.println("Couldn't save: "+e.getMessage());
      }
}


 
   // Define inner class DrawCanvas, which is a JPanel used for custom drawing
   private class DrawCanvas extends JPanel {
      @Override
      public void paintComponent(Graphics g) {
         super.paintComponent(g);  // paint parent's background
         setBackground(Color.black);
         g.setColor(Color.cyan);
         g.drawLine(0, Hud, CANVAS_WIDTH, Hud);
         g.drawLine(0, CANVAS_HEIGHT-2, CANVAS_WIDTH, CANVAS_HEIGHT-2);
         g.drawLine(0, 0,0, CANVAS_HEIGHT-2);
         g.drawLine(CANVAS_WIDTH, 0,CANVAS_WIDTH, CANVAS_HEIGHT-2);
         g.setColor(Color.red);
         for(int i = 0;i<Lives;i++)
         {
             g.fillOval(10+(i*Hud-5), 5, Hud-10, Hud-10);
         }
         g.setColor(Color.blue);
         for(int i= Sheild;i>0;i--)
         {
             g.fillOval((CANVAS_WIDTH-5) -(i*Hud-5), 5, Hud-10, Hud-10);
         }
         g.setColor(Color.GREEN);
         if(pillType.equals("Cyan"))
             g.setColor(Color.cyan);
         if(pillType.equals("Blue"))
             g.setColor(Color.blue);
         if(pillType.equals("Red"))
             g.setColor(Color.red);
         if(pillType.equals("Orange"))
             g.setColor(Color.orange);
         g.fillOval(xPill, yPill, 10, 10);
         g.setColor(Color.WHITE);
         g.setColor(Color.GREEN);
         g.fillRect(x, y, Size, Size);
         g.drawString("Score: "+score, CANVAS_WIDTH/2, Hud/2);
         
         try{
          data = (SaveData)resourceManager.load("Save.Nom");
          
          playerScore = data.pScore;
          g.drawString("High Score: "+playerScore, CANVAS_WIDTH/2, 7*Hud/8);
        }
        catch(Exception e){
          System.out.println("Couldn't load save data: "+e.getMessage());
        }
         
      }
   }
   public void actionPerformed(ActionEvent e){
 
}

public void keyPressed(KeyEvent e){
c = e.getKeyCode();


if(c == KeyEvent.VK_LEFT||c == KeyEvent.VK_A){
xSpeed = -speed;
ySpeed=0;
}
if (c == KeyEvent.VK_RIGHT||c == KeyEvent.VK_D){
xSpeed = speed;
ySpeed=0;
}
if(c == KeyEvent.VK_UP||c == KeyEvent.VK_W){
ySpeed = -speed;
xSpeed = 0;
}
if (c == KeyEvent.VK_DOWN||c == KeyEvent.VK_S){
ySpeed = speed;
xSpeed = 0;
}

if(c == KeyEvent.VK_ESCAPE){
System.exit(0);
}



}
public void keyTyped(KeyEvent e){}
public void keyReleased(KeyEvent e){

}


public static void main(String args[]) {

SwingUtilities.invokeLater(new Runnable(){
        /* Create and display the form */
       // java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                new NomNom().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JPanel DrawCanvas;
    private javax.swing.JButton jButton1;
    // End of variables declaration                   

   
}