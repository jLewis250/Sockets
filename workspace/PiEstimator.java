import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PiEstimator extends Thread {
	public static void main(String[] args) {
		PiThread thread = new PiThread();
		thread.start();

		JFrame frame = new JFrame("PiEstimator");  
	    JButton runButton = new JButton("Run");  
		JButton pauseButton = new JButton("Pause");  

	    JLabel piLabel = new JLabel("Actual Value of pi: " + Double.toString(Math.PI));
		JLabel estimation = new JLabel("Estimated value of pi: ");
		JLabel trialLabel = new JLabel("Number of Trials: ");

		frame.add(piLabel);
		frame.add(estimation);
		frame.add(trialLabel);
		frame.add(runButton);  
		frame.add(pauseButton); 
		frame.setSize(500,400);
		frame.setLayout(new GridLayout(5, 1));    
	    frame.setVisible(true); 

		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){  
				synchronized(thread) {
					thread.swapRunning();
					thread.notify();
				}
			}
 		}); 

		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){  
				thread.swapRunning();
			}
 		}); 

		int temp = 1000000; 
        while(true) { 
            if(thread.getTrials() > temp) {
                trialLabel.setText("Number of Trials: " + thread.getTrials());
                estimation.setText("Current estimate: " + thread.getPi()); 
                temp+= 1000000; 
            } else { 
                System.out.println("pi is: " + thread.getPi());
            }
        }
	}

}

class PiThread extends Thread {
    public boolean running = true; 

    public static int count = 0; 
    public static int trials = 0; 

    public static double pi = 0; 
    //the following code is just to jog your memory about how labels and buttons work!
	//implement your Pi Estimator as described in the project. You may do it all in main below or you 
	//may implement additional functions if you feel it necessary.
    public void run() { 
        while(true) { 
            System.out.println("currently running");
			//we MUST synchronize on whatever object could be accessed by multiple
			//threads at the same time. In this case it's ourselves.
            synchronized(this) { 
                if(!running) {
                    try {
                        System.out.println("Paused!");
                        wait();
                    } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    	e.printStackTrace();
                    }
                }
            }
            double x = Math.random();
            double y = Math.random(); 
            
            if(((x*x) + (y*y)) < 1){ 
				count++; 
            	trials++; 
            	pi = ((double) count/trials)*4;
			}
        }
    }

    public double getPi() { 
        return pi; 
    }

    public int getTrials() { 
        return trials; 
    }
	
	public void swapRunning(){
		if (running){
			running = false;
		}
		else{
			running = true;
		}
	}	
}